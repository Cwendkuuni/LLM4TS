import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    checkGlobalThis = new CheckGlobalThis(compiler);
  }

  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation_ReturnFalse() {
    Node node = new Node(Token.FUNCTION);
    JSDocInfoBuilder jsDocBuilder = new JSDocInfoBuilder(false);
    jsDocBuilder.recordThisType();
    node.setJSDocInfo(jsDocBuilder.build());
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    assertFalse(checkGlobalThis.shouldTraverse(t, node, null));
  }

  @Test
  public void testShouldTraverse_ConstructorFunction_ReturnFalse() {
    Node node = new Node(Token.FUNCTION);
    JSDocInfoBuilder jsDocBuilder = new JSDocInfoBuilder(false);
    jsDocBuilder.recordConstructor();
    node.setJSDocInfo(jsDocBuilder.build());
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    assertFalse(checkGlobalThis.shouldTraverse(t, node, null));
  }

  @Test
  public void testShouldTraverse_OverrideFunction_ReturnFalse() {
    Node node = new Node(Token.FUNCTION);
    JSDocInfoBuilder jsDocBuilder = new JSDocInfoBuilder(false);
    jsDocBuilder.recordOverride();
    node.setJSDocInfo(jsDocBuilder.build());
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    assertFalse(checkGlobalThis.shouldTraverse(t, node, null));
  }

  @Test
  public void testShouldTraverse_InterfaceFunction_ReturnFalse() {
    Node node = new Node(Token.FUNCTION);
    JSDocInfoBuilder jsDocBuilder = new JSDocInfoBuilder(false);
    jsDocBuilder.recordInterface();
    node.setJSDocInfo(jsDocBuilder.build());
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    assertFalse(checkGlobalThis.shouldTraverse(t, node, null));
  }

  @Test
  public void testShouldTraverse_OtherFunctionWithParentBlock_ReturnTrue() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    assertTrue(checkGlobalThis.shouldTraverse(t, node, parent));
  }

  @Test
  public void testShouldTraverse_OtherFunctionWithParentScript_ReturnTrue() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    assertTrue(checkGlobalThis.shouldTraverse(t, node, parent));
  }

  @Test
  public void testShouldTraverse_OtherFunctionWithParentName_ReturnTrue() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    assertTrue(checkGlobalThis.shouldTraverse(t, node, parent));
  }

  @Test
  public void testShouldTraverse_OtherFunctionWithParentAssign_ReturnTrue() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    assertTrue(checkGlobalThis.shouldTraverse(t, node, parent));
  }

  @Test
  public void testShouldTraverse_OtherFunctionWithParentStringKey_ReturnTrue() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.STRING_KEY);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    assertTrue(checkGlobalThis.shouldTraverse(t, node, parent));
  }

  @Test
  public void testVisit_ThisNode_ShouldReportGlobalThis() {
    Node node = new Node(Token.THIS);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    checkGlobalThis.visit(t, node, null);
    assertTrue(compiler.getErrorManager().hasErrors());
  }

  @Test
  public void testVisit_ThisNodeWithAssignLhsChild_ShouldReportGlobalThis() {
    Node node = new Node(Token.THIS);
    checkGlobalThis.assignLhsChild = new Node(Token.ASSIGN);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    checkGlobalThis.visit(t, node, null);
    assertTrue(compiler.getErrorManager().hasErrors());
    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testVisit_ThisNodeWithParentPropertyAccess_ShouldReportGlobalThis() {
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(node.cloneTree());
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    checkGlobalThis.visit(t, node, parent);
    assertTrue(compiler.getErrorManager().hasErrors());
  }

  @Test
  public void testVisit_NonThisNode_ShouldNotReportGlobalThis() {
    Node node = new Node(Token.NAME);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    checkGlobalThis.visit(t, node, null);
    assertFalse(compiler.getErrorManager().hasErrors());
  }

}