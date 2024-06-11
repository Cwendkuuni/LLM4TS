import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  @Test
  public void testShouldReportThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node node = new Node(Token.THIS);
    assertTrue(checkGlobalThis.shouldReportThis(node));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node node = new Node(Token.NAME);
    JSDocInfo jsDocInfo = new JSDocInfo();
    node.setJSDocInfo(jsDocInfo);
    assertEquals(jsDocInfo, checkGlobalThis.getFunctionJsDocInfo(node));
  }

  @Test
  public void testGetFunctionJsDocInfoParentName() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node node = new Node(Token.NAME);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDocInfo = new JSDocInfo();
    parent.setJSDocInfo(jsDocInfo);
    node.setParent(parent);
    assertEquals(jsDocInfo, checkGlobalThis.getFunctionJsDocInfo(node));
  }

  @Test
  public void testGetFunctionJsDocInfoParentAssign() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node node = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    JSDocInfo jsDocInfo = new JSDocInfo();
    parent.setJSDocInfo(jsDocInfo);
    node.setParent(parent);
    assertEquals(jsDocInfo, checkGlobalThis.getFunctionJsDocInfo(node));
  }

  @Test
  public void testGetFunctionJsDocInfoParentVar() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node node = new Node(Token.NAME);
    Node parent = new Node(Token.VAR);
    JSDocInfo jsDocInfo = new JSDocInfo();
    parent.setJSDocInfo(jsDocInfo);
    Node gramps = new Node(Token.BLOCK);
    parent.setParent(gramps);
    node.setParent(parent);
    assertEquals(jsDocInfo, checkGlobalThis.getFunctionJsDocInfo(node));
  }

  @Test
  public void testGetFunctionJsDocInfoParentNull() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node node = new Node(Token.NAME);
    assertNull(checkGlobalThis.getFunctionJsDocInfo(node));
  }

  @Test
  public void testShouldTraverseFunctionWithConstructorJsDoc() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setConstructor(true);
    n.setJSDocInfo(jsDocInfo);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithInterfaceJsDoc() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setInterface(true);
    n.setJSDocInfo(jsDocInfo);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithThisTypeJsDoc() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.recordThisType();
    n.setJSDocInfo(jsDocInfo);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithOverrideJsDoc() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setOverride(true);
    n.setJSDocInfo(jsDocInfo);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverseNotFunction() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.VALUE);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverseFunctionInsideBlock() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverseFunctionInsideScript() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverseFunctionInsideName() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    parent.addChildToBack(n);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverseFunctionInsideAssign() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(n);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverseFunctionInsideStringKey() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.STRING_KEY);
    parent.addChildToBack(n);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverseFunctionInsideGetPropertyInsideGetPrototype() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.GETPROP);
    Node grandParent = new Node(Token.GETPROP);
    parent.addChildToBack(grandParent);
    grandParent.addChildToBack(n);
    parent.addChildToBack(new Node(Token.STRING));
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverseFunctionInsideGetPrototypeGetProperty() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.GETPROP);
    Node grandParent = new Node(Token.GETPROP);
    parent.addChildToBack(grandParent);
    grandParent.addChildToBack(new Node(Token.STRING));
    grandParent.addChildToBack(n);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverseFunctionInsideGet() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToBack(new Node(Token.STRING));
    parent.addChildToBack(n);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverseFunctionInsideVarInsideBlock() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.VAR);
    Node grandParent = new Node(Token.BLOCK);
    parent.setParent(grandParent);
    grandParent.addChildToBack(parent);
    parent.addChildToBack(n);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverseFunctionInsideAssignInsideBlock() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    Node grandParent = new Node(Token.BLOCK);
    parent.setParent(grandParent);
    grandParent.addChildToBack(parent);
    parent.addChildToBack(n);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverseFunctionInsideGetPropertyInsideBlock() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.GETPROP);
    Node grandParent = new Node(Token.BLOCK);
    parent.setParent(grandParent);
    grandParent.addChildToBack(parent);
    parent.addChildToBack(new Node(Token.STRING));
    parent.addChildToBack(n);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverseAssignBeforeFunctionWithThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node assignLhsChild = new Node(Token.NAME);
    checkGlobalThis.assignLhsChild = assignLhsChild;
    Node n = new Node(Token.THIS);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, null));
    checkGlobalThis.assignLhsChild = null;
  }

  @Test
  public void testVisitShouldReportThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
    checkGlobalThis.visit(t, n, parent);
    assertNotNull(t.getErrorLevel(DiagnosticType.warning));
  }

  @Test
  public void testVisitAssignLhsChildIsNull() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.BLOCK);
    checkGlobalThis.visit(t, n, parent);
    assertNull(checkGlobalThis.assignLhsChild);
  }

}