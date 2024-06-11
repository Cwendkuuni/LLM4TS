import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    checker = new CheckGlobalThis(compiler, level);
  }

  @Test
  public void testShouldTraverse_GlobalFunctionWithConstructorAnnotation_ShouldReturnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    assertFalse(checker.shouldTraverse(t, n, null));
  }

  @Test
  public void testShouldTraverse_GlobalFunctionWithThisAnnotation_ShouldReturnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);
    assertFalse(checker.shouldTraverse(t, n, null));
  }

  @Test
  public void testShouldTraverse_LocalFunctionWithThisAnnotation_ShouldReturnTrue() {
    NodeTraversal t = new NodeTraversal();
    Node parent = new Node(Token.VAR);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    parent.setJSDocInfo(jsDoc);
    assertTrue(checker.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_LocalFunctionWithoutAnnotations_ShouldReturnTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    assertTrue(checker.shouldTraverse(t, n, null));
  }

  @Test
  public void testVisit_NodeIsThisAndShouldReport_ShouldReportWarning() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(n);
    assertTrue(checker.shouldReportThis(n, parent));
    // Based on the pseudocode, MOCK the compiler.report method and verify it is called with the correct parameters.
  }

  @Test
  public void testVisit_NodeIsThisAndShouldNotReport_ShouldNotReportWarning() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildAfter(n, new Node(Token.OTHER));
    assertFalse(checker.shouldReportThis(n, parent));
    // Based on the pseudocode, MOCK the compiler.report method and verify it is not called.
  }

  @Test
  public void testVisit_NodeIsAssignLhsChild_ShouldResetVariable() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.VAR);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(n);
    checker.visit(t, n, parent);
    assertNull(checker.assignLhsChild);
  }

  @Test
  public void testVisit_NodeIsNotAssignLhsChild_ShouldNotResetVariable() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.VAR);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildAfter(new Node(Token.THIS), n);
    checker.assignLhsChild = n;
    checker.visit(t, n, parent);
    assertNotNull(checker.assignLhsChild);
  }

  @Test
  public void testGetFunctionJsDocInfo_NodeHasJSDocInfo_ShouldReturnJSDocInfo() {
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    JSDocInfo result = checker.getFunctionJsDocInfo(n);
    assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_ParentIsAssignAndHasJSDocInfo_ShouldReturnJSDocInfo() {
    Node assignNode = new Node(Token.ASSIGN);
    Node n = new Node(Token.VAR);
    n.setJSDocInfo(new JSDocInfo());
    assignNode.addChildToFront(n);
    JSDocInfo result = checker.getFunctionJsDocInfo(n);
    assertEquals(n.getJSDocInfo(), result);
  }

  @Test
  public void testGetFunctionJsDocInfo_ParentIsNameAndHasJSDocInfo_ShouldReturnJSDocInfo() {
    Node nameNode = new Node(Token.NAME);
    Node assignNode = new Node(Token.ASSIGN);
    Node n = new Node(Token.FUNCTION);
    n.setJSDocInfo(new JSDocInfo());
    nameNode.addChildToBack(assignNode);
    assignNode.addChildToBack(n);
    JSDocInfo result = checker.getFunctionJsDocInfo(n);
    assertEquals(n.getJSDocInfo(), result);
  }

  @Test
  public void testGetFunctionJsDocInfo_ParentIsVarAndHasJSDocInfo_ShouldReturnJSDocInfo() {
    Node varNode = new Node(Token.VAR);
    Node nameNode = new Node(Token.NAME);
    Node assignNode = new Node(Token.ASSIGN);
    Node n = new Node(Token.FUNCTION);
    n.setJSDocInfo(new JSDocInfo());
    varNode.addChildToBack(nameNode);
    nameNode.addChildToBack(assignNode);
    assignNode.addChildToBack(n);
    JSDocInfo result = checker.getFunctionJsDocInfo(n);
    assertEquals(n.getJSDocInfo(), result);
  }

  @Test
  public void testGetFunctionJsDocInfo_NoJSDocInfo_ShouldReturnNull() {
    Node n = new Node(Token.FUNCTION);
    JSDocInfo result = checker.getFunctionJsDocInfo(n);
    assertNull(result);
  }

}