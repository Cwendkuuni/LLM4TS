import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    // Arrange
    Node root = new Node(Token.EMPTY);
    InlineCostEstimator estimator = new InlineCostEstimator();

    // Act
    int cost = estimator.getCost(root);

    // Assert
    assertEquals(0, cost);
  }

  @Test
  public void testGetCost_WithCostThreshold() {
    // Arrange
    Node root = new Node(Token.EMPTY);
    int costThreshold = 100;
    InlineCostEstimator estimator = new InlineCostEstimator();

    // Act
    int cost = estimator.getCost(root, costThreshold);

    // Assert
    assertEquals(0, cost);
  }

  @Test
  public void testCompiledSizeEstimatorAdd() {
    // Arrange
    Node root = new Node(Token.EMPTY);
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);

    // Act
    estimator.add(root);

    // Assert
    assertEquals(0, estimator.getCost());
  }

  @Test
  public void testCompiledSizeEstimatorGetCost() {
    // Arrange
    Node root = new Node(Token.EMPTY);
    int costThreshold = 100;
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(costThreshold);

    // Act
    int cost = estimator.getCost();

    // Assert
    assertEquals(0, cost);
  }
  
  @Test
  public void testCompiledSizeEstimatorAppend() {
    // Arrange
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);

    // Act
    estimator.append("test");

    // Assert
    assertEquals(4, estimator.getCost());
  }

  @Test
  public void testCompiledSizeEstimatorAddIdentifier() {
    // Arrange
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);

    // Act
    estimator.addIdentifier("identifier");

    // Assert
    assertEquals(2, estimator.getCost());
  }

}