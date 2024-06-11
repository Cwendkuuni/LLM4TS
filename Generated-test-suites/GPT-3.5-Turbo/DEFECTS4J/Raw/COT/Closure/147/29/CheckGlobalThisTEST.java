import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  // Test shouldTraverse() method
  
  @Test
  public void testShouldTraverse_FunctionNotConstructorOrThisOverride_NoTraverse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.BLOCK)));
  }
  
  @Test
  public void testShouldTraverse_FunctionConstructor_NoTraverse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.BLOCK)));
  }
  
  @Test
  public void testShouldTraverse_FunctionThisAnnotation_NoTraverse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.BLOCK)));
  }
  
  // Other test cases for shouldTraverse() method
  
  // Test visit() method
  
  @Test
  public void testVisit_ThisOnLeftSideOfAssignment_ReportError() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    checkGlobalThis.visit(null, n, parent);
    
    // assert reporting of error
  }
  
  @Test
  public void testVisit_ThisWithPropertyAccess_ReportError() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    checkGlobalThis.visit(null, n, parent);
    
    // assert reporting of error
  }
  
  // Other test cases for visit() method
  
}