import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_shouldReturnTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);

    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_shouldReturnFalseWhenFunctionIsConstructor() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);

    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_shouldReturnFalseWhenFunctionHasThisType() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);

    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testVisit_shouldReportErrorWhenThisIsUsedOnTheLeftSideOfAnAssignment() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    checkGlobalThis.visit(t, n, parent);

    // TODO: Assert error reporting
  }

  @Test
  public void testVisit_shouldNotReportErrorWhenThisIsUsedOnTheRightSideOfAnAssignment() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME, "x");
    parent.addChildToBack(lhs);
    parent.addChildToBack(n);
    checkGlobalThis.visit(t, n, parent);

    // TODO: Assert no error reporting
  }

  @Test
  public void testVisit_shouldReportErrorWhenThisHasPropertyAccess() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToBack(n);
    parent.addChildToBack(new Node(Token.STRING, "property"));
    checkGlobalThis.visit(t, n, parent);

    // TODO: Assert error reporting
  }

  @Test
  public void testVisit_assignLhsChildResetToNull() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    checkGlobalThis.visit(t, n, parent);

    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testGetFunctionJsDocInfo_returnFunctionJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);

    assertEquals(jsDoc, checkGlobalThis.getFunctionJsDocInfo(n));
  }

  @Test
  public void testGetFunctionJsDocInfo_returnParentJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    n.setParent(parent);

    assertEquals(jsDoc, checkGlobalThis.getFunctionJsDocInfo(n));
  }

  @Test
  public void testGetFunctionJsDocInfo_returnGrampsJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    Node gramps = new Node(Token.VAR);
    JSDocInfo jsDoc = new JSDocInfo();
    gramps.setJSDocInfo(jsDoc);
    parent.setParent(gramps);
    n.setParent(parent);

    assertEquals(jsDoc, checkGlobalThis.getFunctionJsDocInfo(n));
  }

}