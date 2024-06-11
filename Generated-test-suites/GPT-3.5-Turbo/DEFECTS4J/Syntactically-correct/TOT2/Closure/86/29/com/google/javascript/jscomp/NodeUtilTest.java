import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    assertFalse(NodeUtil.isForIn(null));
    // Add more test cases for different scenarios
  }

  @Test
  public void testIsStatement() {
    assertFalse(NodeUtil.isStatement(null));
    // Add more test cases for different scenarios
  }

  @Test
  public void testIsFunctionDeclaration() {
    assertFalse(NodeUtil.isFunctionDeclaration(null));
    // Add more test cases for different scenarios
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(null));
    // Add more test cases for different scenarios
  }

}