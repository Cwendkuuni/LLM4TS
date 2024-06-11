import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_WithFunctionConstructor_ReturnsFalse() {
    CheckGlobalThis checker = new CheckGlobalThis(null, null);
    boolean result = checker.shouldTraverse(null, new Node(Token.FUNCTION), null);
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_WithFunctionThis_ReturnsFalse() {
    CheckGlobalThis checker = new CheckGlobalThis(null, null);
    boolean result = checker.shouldTraverse(null, new Node(Token.FUNCTION), null);
    assertFalse(result);
  }

  @Test
  public void testVisit_WithThisOnLeftSideOfAssignReportTrue() {
    CheckGlobalThis checker = new CheckGlobalThis(null, null);
    boolean result = checker.shouldReportThis(new Node(Token.THIS), new Node(Token.ASSIGN));
    assertTrue(result);
  }
  
  @Test
  public void testVisit_WithThisOnRightSideOfAssignAndParentIsGetReportTrue() {
    CheckGlobalThis checker = new CheckGlobalThis(null, null);
    boolean result = checker.shouldReportThis(new Node(Token.THIS), new Node(Token.GETPROP));
    assertTrue(result);
  }
  
  @Test
  public void testVisit_WithThisOnRightSideOfAssignAndParentNotGetReportFalse() {
    CheckGlobalThis checker = new CheckGlobalThis(null, null);
    boolean result = checker.shouldReportThis(new Node(Token.THIS), new Node(Token.LET));
    assertFalse(result);
  }

}