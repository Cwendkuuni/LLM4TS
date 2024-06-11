import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    checker = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverse_functionNotConstructorOrThisAnnotation_false() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    boolean result = checker.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_functionConstructorOrThisAnnotation_true() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    boolean result = checker.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_parentNotValidContext_false() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.PLUS);
    boolean result = checker.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_parentValidContext_true() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    boolean result = checker.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_parentGettingLentToPrototype_false() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    Node rhs = new Node();
    parent.addChildToBack(lhs);
    parent.addChildToBack(rhs);
    lhs.addChildToFront(new Node(Token.GETPROP));
    lhs.getLastChild().addChildToBack(new Node(Token.STRING, "prototype"));
    boolean result = checker.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testVisit_thisWithParentPropertyAccess_report() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    boolean result = false;
    t.setErrorHandler(new ErrorHandler() {
      public void report(NodeTraversal traversal, Node n, CheckLevel level, String msg) {
        result = true;
      }
    });
    checker.visit(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void testVisit_thisWithParentAssignment_report() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    boolean result = false;
    t.setErrorHandler(new ErrorHandler() {
      public void report(NodeTraversal traversal, Node n, CheckLevel level, String msg) {
        result = true;
      }
    });
    checker.visit(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void testVisit_notAssignLhsChild_noChange() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    checker.assignLhsChild = null;
    checker.visit(t, n, parent);
    assertNull(checker.assignLhsChild);
  }

  @Test
  public void testVisit_assignLhsChild_resetToNull() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    checker.assignLhsChild = n;
    checker.visit(t, n, parent);
    assertNull(checker.assignLhsChild);
  }

  @Test
  public void testShouldReportThis_assignLhsChild_true() {
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    checker.assignLhsChild = n;
    boolean result = checker.shouldReportThis(n, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_notAssignLhsChild_true() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    boolean result = checker.shouldReportThis(n, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_notAssignLhsChild_false() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.LET);
    boolean result = checker.shouldReportThis(n, parent);
    assertFalse(result);
  }

  @Test
  public void testGetFunctionJsDocInfo_withJSDoc_null() {
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = checker.getFunctionJsDocInfo(n);
    assertNull(jsDoc);
  }

  @Test
  public void testGetFunctionJsDocInfo_withParentJSDoc_null() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    JSDocInfo resultJsDoc = checker.getFunctionJsDocInfo(n);
    assertNull(resultJsDoc);
  }

  @Test
  public void testGetFunctionJsDocInfo_withGrandParentVarJSDoc_returnJSDoc() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    Node gramps = new Node(Token.VAR);
    JSDocInfo jsDoc = new JSDocInfo();
    gramps.setJSDocInfo(jsDoc);
    parent.addChildToFront(gramps);
    JSDocInfo resultJsDoc = checker.getFunctionJsDocInfo(n);
    assertEquals(jsDoc, resultJsDoc);
  }

}