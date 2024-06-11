import static org.junit.Assert.*;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_NonGlobalContext_ReturnsTrue() {
    CheckGlobalThis checker = new CheckGlobalThis();
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.CALL);

    boolean result = checker.shouldTraverse(t, n, parent);

    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithConstructorAnnotation_ReturnsFalse() {
    CheckGlobalThis checker = new CheckGlobalThis();
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.CALL);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);

    boolean result = checker.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation_ReturnsFalse() {
    CheckGlobalThis checker = new CheckGlobalThis();
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.CALL);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);

    boolean result = checker.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithoutConstructorOrThisAnnotation_ReturnsTrue() {
    CheckGlobalThis checker = new CheckGlobalThis();
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.CALL);

    boolean result = checker.shouldTraverse(t, n, parent);

    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideAssignment_AssignLhsChildNotNull_ReturnsFalse() {
    CheckGlobalThis checker = new CheckGlobalThis();
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME);
    parent.addChildToBack(lhs);
    parent.addChildToBack(new Node(Token.EMPTY));
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    lhs.setJSDocInfo(jsDoc);
    checker.setAssignLhsChild(lhs);

    boolean result = checker.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInsideAssignment_AssignLhsChildNull_ReturnsTrue() {
    CheckGlobalThis checker = new CheckGlobalThis();
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME);
    parent.addChildToBack(lhs);
    parent.addChildToBack(new Node(Token.EMPTY));
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    lhs.setJSDocInfo(jsDoc);

    boolean result = checker.shouldTraverse(t, n, parent);

    assertTrue(result);
  }

  @Test
  public void testVisit_ThisNodeAndShouldReportThis_ReturnsCorrectResult() {
    CheckGlobalThis checker = new CheckGlobalThis();
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    parent.addChildToBack(n);
    DiagnosticType expectedDiagnosticType = DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object");
    t.setErrorExpected(n);

    checker.visit(t, n, parent);

    assertTrue(t.getErrorReported().getType().equals(expectedDiagnosticType));
  }

  @Test
  public void testVisit_ThisNodeAndShouldReportThis_AssignLhsChildNotNull_ResetsAssignLhsChildToNull() {
    CheckGlobalThis checker = new CheckGlobalThis();
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    parent.addChildToBack(n);
    t.setErrorExpected(n);
    Node assignLhsChild = new Node(Token.NAME);
    checker.setAssignLhsChild(assignLhsChild);

    checker.visit(t, n, parent);

    assertNull(checker.getAssignLhsChild());
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionWithJsDoc_ReturnsJsDocInfo() {
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);

    JSDocInfo result = getFunctionJsDocInfo(n);

    assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJsDoc_ParentIsName_ReturnsJsDocInfo() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    parent.addChildToBack(n);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);

    JSDocInfo result = getFunctionJsDocInfo(n);

    assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJsDoc_ParentIsAssign_ReturnsJsDocInfo() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(new Node(Token.NAME));
    parent.addChildToBack(n);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);

    JSDocInfo result = getFunctionJsDocInfo(n);

    assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJsDoc_ParentIsNameInsideVar_ReturnsJsDocInfo() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    Node gramps = new Node(Token.VAR);
    gramps.addChildToBack(parent);
    parent.addChildToBack(n);
    JSDocInfo jsDoc = new JSDocInfo();
    gramps.setJSDocInfo(jsDoc);

    JSDocInfo result = getFunctionJsDocInfo(n);

    assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJsDoc_ReturnsNull() {
    Node n = new Node(Token.FUNCTION);

    JSDocInfo result = getFunctionJsDocInfo(n);

    assertNull(result);
  }
}