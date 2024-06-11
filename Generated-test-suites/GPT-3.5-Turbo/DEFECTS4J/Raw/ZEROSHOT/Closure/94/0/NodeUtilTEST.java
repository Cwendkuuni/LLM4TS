import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    Assert.assertFalse(NodeUtil.isForIn(null));
    // Add more test cases here
  }
  
  @Test
  public void testIsStatement() {
    Assert.assertFalse(NodeUtil.isStatement(null));
    // Add more test cases here
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Assert.assertFalse(NodeUtil.isFunctionDeclaration(null));
    // Add more test cases here
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(null));
    // Add more test cases here
  }
  
}