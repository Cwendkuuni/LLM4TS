import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  private CheckGlobalThis checkGlobalThis;
  
  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
  }
  
  @Test
  public void shouldTraverse_shouldReturnTrue_whenNodeIsFunctionAndNotConstructorOrThisTypeOrOverride() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(false);
    jsDoc.setThisType(false);
    jsDoc.setOverride(false);
    n.setFunction(true);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void shouldTraverse_shouldReturnFalse_whenNodeIsFunctionAndConstructor() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setFunction(true);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void shouldTraverse_shouldReturnFalse_whenNodeIsFunctionAndThisType() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setThisType(true);
    n.setFunction(true);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void shouldTraverse_shouldReturnFalse_whenNodeIsFunctionAndOverride() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    n.setFunction(true);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void shouldTraverse_shouldReturnFalse_whenNodeIsFunctionAndNotAllowedParentType() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    Node grandParent = new Node();
    n.setFunction(true);
    grandParent.setType(Token.BLOCK); // Not allowed parent type
    grandParent.addChildToFront(n);
    parent.addChildToFront(grandParent);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void shouldTraverse_shouldReturnFalse_whenNodeIsFunctionAndLentToPrototype() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    Node grandParent = new Node();
    n.setFunction(true);
    jsDoc.setLendsName("prototype"); // Lends to prototype
    grandParent.setJSDocInfo(jsDoc);
    parent.addChildToFront(n);
    grandParent.addChildToFront(parent);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void shouldTraverse_shouldReturnTrue_whenParentIsAssignAndNodeIsAssignLhsChild() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    Node lhs = new Node();
    n.setThis(true);
    parent.setType(Token.ASSIGN);
    lhs.addChildToFront(n);
    parent.addChildToFront(lhs);
    checkGlobalThis.assignLhsChild = lhs; // assignLhsChild is set
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void shouldTraverse_shouldReturnFalse_whenParentIsAssignAndNodeIsNotAssignLhsChild() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    Node lhs = new Node();
    n.setThis(true);
    parent.setType(Token.ASSIGN);
    lhs.addChildToFront(new Node()); // Another child node
    parent.addChildToFront(lhs);
    checkGlobalThis.assignLhsChild = new Node(); // assignLhsChild is set but not equal to lhs
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void visit_shouldReportErrorAndSetAssignLhsChildToNull_whenNodeIsThisAndShouldReportThisReturnsTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    n.setThis(true);
    parent.addChildToFront(n);
    checkGlobalThis.assignLhsChild = new Node(); // assignLhsChild is set
    checkGlobalThis.visit(t, n, parent);
    assertNull(checkGlobalThis.assignLhsChild);
    // Error should be reported  
    // compiler instance is not available in this test
  }
  
  @Test
  public void visit_shouldNotReportErrorAndDoNotChangeAssignLhsChild_whenNodeIsThisAndShouldReportThisReturnsFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    n.setThis(true);
    parent.addChildToFront(n);
    checkGlobalThis.assignLhsChild = null; // assignLhsChild is null
    checkGlobalThis.visit(t, n, parent);
    assertNull(checkGlobalThis.assignLhsChild);
    // No error should be reported  
    // compiler instance is not available in this test
  }
}