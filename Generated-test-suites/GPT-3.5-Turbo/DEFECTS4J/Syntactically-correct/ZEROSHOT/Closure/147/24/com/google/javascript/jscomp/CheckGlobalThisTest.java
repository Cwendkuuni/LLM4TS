import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  @Test
  public void testShouldReportThis() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    assertTrue(CheckGlobalThis.shouldReportThis(n, parent));
  }
  
  @Test
  public void testShouldNotReportThis() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ADD);
    assertFalse(CheckGlobalThis.shouldReportThis(n, parent));
  }
  
  @Test
  public void testShouldTraverseFunction() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    assertTrue(CheckGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldNotTraverseFunction() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    assertFalse(CheckGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverseAssign() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    assertTrue(CheckGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldNotTraverseAssign() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.GETPROP);
    assertFalse(CheckGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testVisitThisShouldReport() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    checkGlobalThis.visit(t, n, parent);
    assertEquals(1, t.getErrors().size());
  }
  
  @Test
  public void testVisitThisShouldNotReport() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ADD);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    checkGlobalThis.visit(t, n, parent);
    assertEquals(0, t.getErrors().size());
  }

  @Test
  public void testGetFunctionJsDocInfoWithJSDoc() {
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.NAME);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    assertEquals(jsDoc, checkGlobalThis.getFunctionJsDocInfo(n));
  }
  
  @Test
  public void testGetFunctionJsDocInfoWithoutJSDoc() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    assertNull(checkGlobalThis.getFunctionJsDocInfo(n));
  }
}