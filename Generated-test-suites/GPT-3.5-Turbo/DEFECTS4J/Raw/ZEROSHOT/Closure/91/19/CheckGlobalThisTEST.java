import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis();
  }
  
  @Test
  public void testShouldTraverse_WithFunctionConstructor_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    
    jsDoc.setConstructor(true);
    
    n.setType(Token.FUNCTION);
    n.putProp("jsdocinfo", jsDoc);
  
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_WithFunctionWithThisAnnotation_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    
    jsDoc.recordThisType();
    
    n.setType(Token.FUNCTION);
    n.putProp("jsdocinfo", jsDoc);
  
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_WithFunctionWithOverrideAnnotation_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    
    jsDoc.setOverride(true);
    
    n.setType(Token.FUNCTION);
    n.putProp("jsdocinfo", jsDoc);
  
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_WithFunctionNotInBlock_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    
    Node p = new Node();
    p.setType(Token.SCRIPT);
    
    parent.setType(Token.ASSIGN);
    parent.addChildToFront(p);
    
    n.setType(Token.FUNCTION);
  
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_WithFunctionInBlock_ReturnsTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    
    Node p = new Node();
    p.setType(Token.BLOCK);
    
    parent.setType(Token.ASSIGN);
    parent.addChildToFront(p);
    
    n.setType(Token.FUNCTION);
  
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_WithFunctionWithOwnPropertyAccess_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    
    Node lhs = new Node();
    lhs.setType(Token.GETPROP);
    
    parent.setType(Token.ASSIGN);
    parent.addChildToFront(lhs);
    
    n.setType(Token.FUNCTION);
  
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_WithFunctionWithPrototypePropertyAccess_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    
    Node lhs = new Node();
    lhs.setType(Token.GETPROP);
    
    Node llhs = new Node();
    llhs.setType(Token.GETPROP);
    llhs.addChildToBack(new Node(Token.STRING));
    llhs.getLastChild().setString("prototype");
    
    lhs.addChildToFront(llhs);
    
    parent.setType(Token.ASSIGN);
    parent.addChildToFront(lhs);
    
    n.setType(Token.FUNCTION);
  
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldReportThis_WithThisOnLeftSideOfAssign_ReturnsTrue() {
    Node n = new Node();
    Node parent = new Node();
    
    checkGlobalThis.setAssignLhsChild(new Node());
    
    n.setType(Token.THIS);
  
    assertTrue(checkGlobalThis.shouldReportThis(n, parent));
  }
  
  @Test
  public void testShouldReportThis_WithThisWithPropertyAccess_ReturnsTrue() {
    Node n = new Node();
    Node parent = new Node();
    
    parent.setType(Token.GETPROP);
    
    n.setType(Token.THIS);
  
    assertTrue(checkGlobalThis.shouldReportThis(n, parent));
  }
  
  @Test
  public void testShouldReportThis_WithThisWithNoPropertyAccess_ReturnsFalse() {
    Node n = new Node();
    Node parent = new Node();
    
    n.setType(Token.THIS);
  
    assertFalse(checkGlobalThis.shouldReportThis(n, parent));
  }
  
  @Test
  public void testVisit_WithThisAndShouldReportThis_CallsReportMethod() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    
    checkGlobalThis.setCompiler(new AbstractCompiler());
    
    n.setType(Token.THIS);
  
    checkGlobalThis.visit(t, n, parent);
    
    assertEquals(1, checkGlobalThis.getCompiler().getReportCalls());
  }
  
  @Test
  public void testVisit_WithThisAndNotShouldReportThis_DoesNotCallReportMethod() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    
    checkGlobalThis.setCompiler(new AbstractCompiler());
    
    n.setType(Token.THIS);
  
    checkGlobalThis.setAssignLhsChild(new Node());
    
    checkGlobalThis.visit(t, n, parent);
    
    assertEquals(0, checkGlobalThis.getCompiler().getReportCalls());
  }
}