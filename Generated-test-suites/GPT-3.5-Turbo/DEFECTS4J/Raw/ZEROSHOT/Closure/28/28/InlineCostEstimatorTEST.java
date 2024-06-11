import org.junit.Assert;
import org.junit.Test;

import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost_NoThreshold() {
    // Arrange
    Node root = new Node();

    // Act
    int cost = InlineCostEstimator.getCost(root);

    // Assert
    Assert.assertEquals(0, cost);
  }

  @Test
  public void testGetCost_WithThreshold() {
    // Arrange
    Node root = new Node();

    // Act
    int cost = InlineCostEstimator.getCost(root, 100);

    // Assert
    Assert.assertEquals(0, cost);
  }

}