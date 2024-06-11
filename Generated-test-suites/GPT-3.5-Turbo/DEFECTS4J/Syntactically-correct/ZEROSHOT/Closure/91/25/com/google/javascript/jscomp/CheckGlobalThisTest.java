import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {
  
  @Test
  public void testShouldTraverse_shouldTraverseFunctionsWithNoAnnotations() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_shouldNotTraverseConstructorFunctionsWithAnnotations() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setConstructor(true);
    n.setJSDocInfo(jsDocInfo);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_shouldNotTraverseFunctionsWithThisAnnotations() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.recordThisType();
    n.setJSDocInfo(jsDocInfo);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_shouldNotTraverseMethodsWithNoLogicalPlaceForThisAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    Node grandparent = new Node(Token.STRING);
    grandparent.addChildToFront(n);
    parent.addChildToFront(grandparent);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_shouldTraverseFunctionsAssignedToVariablesOrObjects() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_shouldNotTraverseMethodsWithPrototypeAssignment() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    Node grandparent = new Node(Token.GETPROP);
    grandparent.addChildToFront(new Node(Token.STRING));
    grandparent.addChildToFront(n);
    parent.addChildToFront(grandparent);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testVisit_shouldReportThisWhenOnLeftSideOfAssignment() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    
    checkGlobalThis.visit(t, n, parent);
  }
  
  @Test
  public void testVisit_shouldReportThisWhenInsidePropertyAccess() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(new Node(Token.STRING));
    parent.addChildToBack(n);
    
    checkGlobalThis.visit(t, n, parent);
  }
  
  @Test
  public void testVisit_shouldNotReportThisWhenNotOnLeftSideOfAssignment() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(new Node(Token.STRING));
    parent.addChildToBack(new Node(Token.NAME));
    
    checkGlobalThis.visit(t, n, parent);
  }
  
  @Test
  public void testVisit_shouldNotReportThisWhenNotInsidePropertyAccess() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    
    checkGlobalThis.visit(t, n, parent);
  }
}