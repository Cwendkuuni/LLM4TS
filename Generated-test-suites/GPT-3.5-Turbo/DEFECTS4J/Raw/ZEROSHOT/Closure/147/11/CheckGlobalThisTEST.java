import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_GlobalContext_ReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.SCRIPT);
    Node parent = null;
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionConstructor_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EMPTY);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.createConstructorType();
    parent.setJSDocInfo(jsDoc);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionThis_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EMPTY);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.createThisType();
    parent.setJSDocInfo(jsDoc);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_BlockFunction_ReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_AssignFunction_ReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }

  @Test
  public void testVisit_ThisShouldReport_ReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(new Node(Token.NAME, "example"));
    parent.addChildToBack(new Node(Token.STRING, "property"));
    
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    assertTrue(result);
  }

  @Test
  public void testVisit_ThisShouldNotReport_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    assertFalse(result);
  }

}