import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_NormalFunction_ReturnTrue() {
    // Test normal function that doesn't have any annotations
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    assertTrue(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION, 0), new Node(Token.BLOCK)));
  }

  @Test
  public void testShouldReportThis_ThisOnLeftSideOfAssign_ReturnTrue() {
    // Test when this is on the left side of an assignment
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    assertTrue(checkGlobalThis.shouldReportThis(new Node(Token.THIS, 0), new Node(Token.ASSIGN)));
  }

  @Test
  public void testShouldReportThis_ThisWithPropertyAccess_ReturnTrue() {
    // Test when using this with a property access
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    assertTrue(checkGlobalThis.shouldReportThis(new Node(Token.THIS, 0), new Node(Token.GETPROP)));
  }

  // Add more test cases for all public methods in the CheckGlobalThis class
  
}