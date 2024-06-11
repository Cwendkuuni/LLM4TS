import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_NonFunctionNode_ReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node();
    Node parent = new Node();
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }

@Test
  public void testShouldTraverse_FunctionNode_NotConstructor_NotThisAnnotation_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_FunctionNode_Constructor_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_FunctionNode_ThisAnnotation_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_FunctionNode_HasNoLogicalPlaceForThisAnnotation_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setType(Token.ASSIGN);
    n.setJSDocInfo(jsDoc);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_FunctionNode_ValidFunction_ReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(false);
    n.setJSDocInfo(jsDoc);
    parent.setType(Token.NAME);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_FunctionNode_ValidFunction_ReturnsTrue2() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(false);
    n.setJSDocInfo(jsDoc);
    parent.setType(Token.SCRIPT);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_FunctionNode_ValidFunction_ReturnsTrue3() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(false);
    n.setJSDocInfo(jsDoc);
    parent.setType(Token.BLOCK);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_FunctionNode_ValidFunction_ReturnsTrue4() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(false);
    n.setJSDocInfo(jsDoc);
    parent.setType(Token.ASSIGN);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testVisit_NodeNotThis_DoesNotReportError() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node();
    Node parent = new Node();
    checkGlobalThis.visit(t, n, parent);
  }

  @Test
  public void testVisit_NodeThis_ShouldReportError_ReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    assertTrue(checkGlobalThis.shouldReportThis(n, parent));
  }

  @Test
  public void testVisit_NodeThis_ShouldNotReportError_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    assertFalse(checkGlobalThis.shouldReportThis(n, parent));
  }

  @Test
  public void testVisit_NodeAssignmentOnLeftSide_ReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node();
    Node parent = new Node();
    checkGlobalThis.assignLhsChild = new Node();
    assertTrue(checkGlobalThis.shouldReportThis(n, parent));
  }

  @Test
  public void testVisit_NodeAssignmentOnLeftSide_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node();
    Node parent = new Node();
    checkGlobalThis.assignLhsChild = null;
    assertFalse(checkGlobalThis.shouldReportThis(n, parent));
  }
}