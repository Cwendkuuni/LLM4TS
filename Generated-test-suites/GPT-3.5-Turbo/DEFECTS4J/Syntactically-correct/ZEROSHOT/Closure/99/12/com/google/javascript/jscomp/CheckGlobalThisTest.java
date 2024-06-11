import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_GlobalThis_ReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, checkGlobalThis, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithConstructorAnnotation_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, checkGlobalThis, null);
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo());
    n.getFirstChild().putProp(Node.JSDOC_INFO_PROP, new JSDocInfo());
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, checkGlobalThis, null);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.putProp(Node.JSDOC_INFO_PROP, jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithOverrideAnnotation_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, checkGlobalThis, null);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    n.putProp(Node.JSDOC_INFO_PROP, jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInBlock_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, checkGlobalThis, null);
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo());
    Node parent = new Node(Token.BLOCK);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInScript_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, checkGlobalThis, null);
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo());
    Node parent = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithName_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, checkGlobalThis, null);
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo());
    Node parent = new Node(Token.NAME);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInAssign_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, checkGlobalThis, null);
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo());
    Node parent = new Node(Token.ASSIGN);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionInVar_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, checkGlobalThis, null);
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo());
    Node parent = new Node(Token.VAR);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testVisit_GlobalThisAndShouldReportThis_ReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, checkGlobalThis, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    boolean result = false;
    try {
      checkGlobalThis.visit(t, n, parent);
      result = true;
    } catch (Exception e) {
      result = false;
    }
    assertTrue(result);
  }

  @Test
  public void testVisit_NotGlobalThis_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, checkGlobalThis, null);
    Node n = new Node(Token.NULL);
    Node parent = new Node(Token.GETPROP);
    boolean result = false;
    try {
      checkGlobalThis.visit(t, n, parent);
      result = true;
    } catch (Exception e) {
      result = false;
    }
    assertFalse(result);
  }

  @Test
  public void testVisit_AssignLhsChildNotNull_ClearsAssignLhsChild() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    checkGlobalThis.assignLhsChild = new Node(Token.THIS);
    NodeTraversal t = new NodeTraversal(null, checkGlobalThis, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    checkGlobalThis.visit(t, n, parent);
    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionHasJSDoc_ReturnsJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo expected = new JSDocInfo();
    n.putProp(Node.JSDOC_INFO_PROP, expected);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(expected, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionIsAssignedWithJSDoc_ReturnsJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    JSDocInfo expected = new JSDocInfo();
    parent.putProp(Node.JSDOC_INFO_PROP, expected);
    parent.addChildToFront(n);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(expected, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionIsNamedWithJSDoc_ReturnsJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo());
    Node parent = new Node(Token.NAME);
    JSDocInfo expected = new JSDocInfo();
    parent.putProp(Node.JSDOC_INFO_PROP, expected);
    parent.addChildToFront(n);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(expected, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionIsNamedInVarWithJSDoc_ReturnsJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo());
    Node parent = new Node(Token.NAME);
    parent.addChildToFront(n);
    Node gramps = new Node(Token.VAR);
    JSDocInfo expected = new JSDocInfo();
    gramps.putProp(Node.JSDOC_INFO_PROP, expected);
    gramps.addChildToFront(parent);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(expected, result);
  }
}