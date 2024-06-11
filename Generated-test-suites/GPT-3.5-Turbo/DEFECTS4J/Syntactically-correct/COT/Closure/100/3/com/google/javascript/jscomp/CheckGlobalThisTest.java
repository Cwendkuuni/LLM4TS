
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {
  
  @Test
  public void testShouldTraverse() {
    // Test if shouldTraverse returns true for a function without constructor or @this annotation
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createFunctionNode();
    Node parent = createParentNode();
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertEquals(true, result);
  }
  
  @Test
  public void testShouldTraverseConstructor() {
    // Test if shouldTraverse returns false for a function with constructor annotation
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createFunctionNode();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertEquals(false, result);
  }
  
  @Test
  public void testShouldTraverseThisType() {
    // Test if shouldTraverse returns false for a function with @this annotation
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createFunctionNode();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertEquals(false, result);
  }
  
  @Test
  public void testVisit() {
    // Test if visit reports an error for a THIS node with shouldReportThis returning true
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createThisNode();
    Node parent = createParentNode();
    checkGlobalThis.visit(t, n, parent);
    // check if reporter reports an error
    // assertEquals();
  }
  
  private Node createFunctionNode() {
    Node n = new Node(Token.FUNCTION);
    return n;
  }
  
  private Node createParentNode() {
    Node parent = new Node(Token.VAR);
    return parent;
  }
  
  private Node createThisNode() {
    Node n = new Node(Token.THIS);
    return n;
  }
  
}