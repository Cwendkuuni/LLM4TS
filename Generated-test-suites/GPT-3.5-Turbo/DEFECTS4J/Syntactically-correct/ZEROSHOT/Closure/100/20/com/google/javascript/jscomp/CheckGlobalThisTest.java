import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_returnFalseForConstructorFunction() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.NAME);
    assertFalse(CheckGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_returnFalseForFunctionWithThisAnnotation() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.NAME);
    assertFalse(CheckGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_returnFalseForAssignmentToPrototypeProperty() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.GETPROP);
    Node lhs = new Node(Token.NAME, "obj");
    n.addChildToBack(lhs);
    Node rhs = new Node(Token.STRING, "prototype");
    n.addChildToBack(rhs);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(lhs);
    parent.addChildToBack(new Node(Token.STRING, "value"));
    assertFalse(CheckGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_returnFalseForAssignmentToSubproperty() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.GETPROP);
    Node lhs = new Node(Token.STRING, "obj.prototype");
    n.addChildToBack(lhs);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(lhs);
    parent.addChildToBack(new Node(Token.STRING, "value"));
    assertFalse(CheckGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_returnTrueForThisOnLeftSideOfAssignment() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(n);
    parent.addChildToBack(new Node(Token.STRING, "value"));
    assertTrue(CheckGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_returnFalseForRegularThisReference() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    assertFalse(CheckGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testVisit_reportErrorForGlobalThis() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    CheckGlobalThis.checkGlobalThis(t, n, parent);
    // Assert that an error is reported with the correct level and diagnostic type
    assertTrue(t.hasError());
    assertEquals(DiagnosticType.warning("JSC_USED_GLOBAL_THIS"), t.getError().getType());
  }

  @Test
  public void testVisit_resetAssignLhsChild() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    CheckGlobalThis.checkGlobalThis(t, n, parent);
    assertNull(CheckGlobalThis.getAssignLhsChild());
  }

  @Test
  public void testShouldReportThis_returnTrueForAssignLhsChildNotNull() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    CheckGlobalThis.setAssignLhsChild(parent);
    assertTrue(CheckGlobalThis.shouldReportThis(n, parent));
  }

  @Test
  public void testShouldReportThis_returnFalseForAssignLhsChildNull() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    CheckGlobalThis.setAssignLhsChild(null);
    assertFalse(CheckGlobalThis.shouldReportThis(n, parent));
  }

  @Test
  public void testGetFunctionJsDocInfo_returnJsDocInfoFromFunctionNode() {
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    assertEquals(jsDoc, CheckGlobalThis.getFunctionJsDocInfo(n));
  }

  @Test
  public void testGetFunctionJsDocInfo_returnJsDocInfoFromParentWithFunctionNode() {
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    n.addChildToBack(parent);
    assertEquals(jsDoc, CheckGlobalThis.getFunctionJsDocInfo(n));
  }

  @Test
  public void testGetFunctionJsDocInfo_returnJsDocInfoFromParentWithAssignmentNode() {
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    n.addChildToBack(parent);
    assertEquals(jsDoc, CheckGlobalThis.getFunctionJsDocInfo(n));
  }

  @Test
  public void testGetFunctionJsDocInfo_returnJsDocInfoFromGrandparentWithAssignmentNode() {
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.NAME);
    Node grandparent = new Node(Token.VAR);
    JSDocInfo jsDoc = new JSDocInfo();
    grandparent.setJSDocInfo(jsDoc);
    parent.addChildToBack(grandparent);
    n.addChildToBack(parent);
    assertEquals(jsDoc, CheckGlobalThis.getFunctionJsDocInfo(n));
  }
}