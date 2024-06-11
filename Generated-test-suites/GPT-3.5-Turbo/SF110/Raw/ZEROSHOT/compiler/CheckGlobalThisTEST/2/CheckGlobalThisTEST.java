import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_FunctionWithConstructorAnnotation_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordConstructor();
    functionNode.setJSDocInfo(jsDoc);
    assertFalse(checkGlobalThis.shouldTraverse(t, functionNode, null));
  }

  @Test
  public void testShouldTraverse_FunctionWithInterfaceAnnotation_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordInterface();
    functionNode.setJSDocInfo(jsDoc);
    assertFalse(checkGlobalThis.shouldTraverse(t, functionNode, null));
  }

  @Test
  public void testShouldTraverse_FunctionWithThisTypeAnnotation_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    functionNode.setJSDocInfo(jsDoc);
    assertFalse(checkGlobalThis.shouldTraverse(t, functionNode, null));
  }

  @Test
  public void testShouldTraverse_FunctionWithOverrideAnnotation_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordOverride();
    functionNode.setJSDocInfo(jsDoc);
    assertFalse(checkGlobalThis.shouldTraverse(t, functionNode, null));
  }

  @Test
  public void testShouldTraverse_FunctionInsideBlock_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    assertFalse(checkGlobalThis.shouldTraverse(t, functionNode, parent));
  }

  @Test
  public void testShouldTraverse_FunctionInsideScript_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    assertFalse(checkGlobalThis.shouldTraverse(t, functionNode, parent));
  }

  @Test
  public void testShouldTraverse_FunctionInsideName_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    assertFalse(checkGlobalThis.shouldTraverse(t, functionNode, parent));
  }

  @Test
  public void testShouldTraverse_FunctionInsideAssignment_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    assertFalse(checkGlobalThis.shouldTraverse(t, functionNode, parent));
  }

  @Test
  public void testShouldTraverse_FunctionInsideStringKey_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.STRING_KEY);
    assertFalse(checkGlobalThis.shouldTraverse(t, functionNode, parent));
  }

  @Test
  public void testVisit_ThisNode_ShouldReportError() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node thisNode = new Node(Token.THIS);
    assertTrue(checkGlobalThis.shouldReportThis(thisNode));
  }

  @Test
  public void testVisit_ThisNodeInAssignLhsChild_ShouldReportError() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node thisNode = new Node(Token.THIS);
    checkGlobalThis.assignLhsChild = thisNode.getParent();
    assertTrue(checkGlobalThis.shouldReportThis(thisNode));
  }

  @Test
  public void testVisit_NonThisNode_ShouldNotReportError() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node nonThisNode = new Node(Token.NAME);
    assertFalse(checkGlobalThis.shouldReportThis(nonThisNode));
  }
}