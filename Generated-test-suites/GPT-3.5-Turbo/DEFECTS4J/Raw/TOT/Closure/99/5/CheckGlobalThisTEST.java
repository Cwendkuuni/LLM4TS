import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_InsidePrototypeMethod_ShouldReturnFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionAnnotatedWithConstructor_ShouldReturnFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionAnnotatedWithThis_ShouldReturnFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithoutLogicalPlaceForThisAnnotation_ShouldReturnFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_AssignLhsChildNotNull_ShouldReturnTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    checkGlobalThis.assignLhsChild = new Node(Token.OBJECTLIT);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_ParentIsNull_ShouldReturnFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_NotAssignableToPrototypeProperty_ShouldReturnTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = null;
    Node parent = new Node(Token.GETPROP);
    parent.addChildToBack(new Node(Token.NAME, "prototype"));
    parent.addChildToBack(new Node(Token.STRING, "property"));
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_AssignLhsChildIsNull_ShouldReturnFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    checkGlobalThis.assignLhsChild = null;
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldReportThis_ThisInAssignment_ShouldReturnTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldReportThis_ThisWithPropertyAccess_ShouldReturnTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldReportThis_ThisNotInAssignmentOrPropertyAccess_ShouldReturnFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NOT);
    
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testVisit_ThisWithGlobalThisError_ShouldReportGlobalThisError() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null, null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NOT);
    int count = 0;
    
    checkGlobalThis.visit(t, n, parent);
    
    List<JSError> errors = t.getErrors();
    for (JSError error : errors) {
      if (error.getType().equals(DiagnosticType.warning("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object"))) {
        count++;
      }
    }
    
    assertEquals(1, count);
  }
  
  @Test
  public void testVisit_ThisNotWithGlobalThisError_ShouldNotReportGlobalThisError() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null, null, null);
    Node n = new Node(Token.NOT);
    Node parent = new Node(Token.THIS);
    int count = 0;
    
    checkGlobalThis.visit(t, n, parent);
    
    List<JSError> errors = t.getErrors();
    for (JSError error : errors) {
      if (error.getType().equals(DiagnosticType.warning("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object"))) {
        count++;
      }
    }
    
    assertEquals(0, count);
  }
  
  @Test
  public void testVisit_AssignLhsChildNotNull_ShouldSetAssignLhsChildToNull() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    checkGlobalThis.assignLhsChild = new Node(Token.OBJECTLIT);
    
    checkGlobalThis.visit(t, n, parent);
    
    assertNull(checkGlobalThis.assignLhsChild);
  }
  
}