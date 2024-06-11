import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // Edge case: Node n is null
    boolean result = NodeUtil.isForIn(null);
    Assert.assertFalse(result);
  }

  @Test
  public void testIsStatement() {
    // Edge case: Node n is null
    boolean result = NodeUtil.isStatement(null);
    Assert.assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Edge case: Node n is null
    boolean result = NodeUtil.isFunctionDeclaration(null);
    Assert.assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Edge case: Node n is null
    boolean result = NodeUtil.isHoistedFunctionDeclaration(null);
    Assert.assertFalse(result);
  }

  // Other basic test cases for each method can be added here

}