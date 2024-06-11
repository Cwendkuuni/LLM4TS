import org.junit.Assert;
import org.junit.Test;

import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

  @Test
  public void getCost_withSingleNode_shouldReturnCost() {
    // Arrange
    Node root = new Node(Node.Type.EMPTY);
    int expectedCost = 1;

    // Act
    int actualCost = InlineCostEstimator.getCost(root);

    // Assert
    Assert.assertEquals(expectedCost, actualCost);
  }

  @Test
  public void getCost_withMultipleNodes_shouldReturnCost() {
    // Arrange
    Node root = new Node(Node.Type.SCRIPT);
    root.addChildToFront(new Node(Node.Type.VAR));
    root.addChildToFront(new Node(Node.Type.EXPR_RESULT));
    int expectedCost = 3;

    // Act
    int actualCost = InlineCostEstimator.getCost(root);

    // Assert
    Assert.assertEquals(expectedCost, actualCost);
  }

  @Test
  public void getCost_withSingleNodeAndCostThreshold_shouldReturnCost() {
    // Arrange
    Node root = new Node(Node.Type.EXPR_RESULT);
    int costThreshold = 10;
    int expectedCost = 1;

    // Act
    int actualCost = InlineCostEstimator.getCost(root, costThreshold);

    // Assert
    Assert.assertEquals(expectedCost, actualCost);
  }

  @Test
  public void getCost_withMultipleNodesAndCostThreshold_shouldReturnCost() {
    // Arrange
    Node root = new Node(Node.Type.SCRIPT);
    root.addChildToFront(new Node(Node.Type.VAR));
    root.addChildToFront(new Node(Node.Type.EXPR_RESULT));
    int costThreshold = 3;
    int expectedCost = 3;

    // Act
    int actualCost = InlineCostEstimator.getCost(root, costThreshold);

    // Assert
    Assert.assertEquals(expectedCost, actualCost);
  }

  @Test
  public void add_withIdentifier_shouldIncreaseCost() {
    // Arrange
    String identifier = "test";
    InlineCostEstimator.CostEstimator estimator = new InlineCostEstimator.CostEstimator(10);
    int initialCost = estimator.getCost();
    int expectedCost = initialCost + identifier.length();

    // Act
    estimator.addIdentifier(identifier);
    int actualCost = estimator.getCost();

    // Assert
    Assert.assertEquals(expectedCost, actualCost);
  }

  @Test
  public void add_withIdentifierAndCostThresholdExceeded_shouldStopProcessing() {
    // Arrange
    String identifier = "test";
    int costThreshold = 5;
    InlineCostEstimator.CostEstimator estimator = new InlineCostEstimator.CostEstimator(costThreshold);
    int initialCost = estimator.getCost();

    // Act
    estimator.addIdentifier(identifier);
    boolean continueProcessing = estimator.continueProcessing();

    // Assert
    Assert.assertFalse(continueProcessing);
  }

  @Test
  public void add_withIdentifierAndCostThresholdNotExceeded_shouldContinueProcessing() {
    // Arrange
    String identifier = "test";
    int costThreshold = 10;
    InlineCostEstimator.CostEstimator estimator = new InlineCostEstimator.CostEstimator(costThreshold);
    int initialCost = estimator.getCost();

    // Act
    estimator.addIdentifier(identifier);
    boolean continueProcessing = estimator.continueProcessing();

    // Assert
    Assert.assertTrue(continueProcessing);
  }

  @Test
  public void append_withString_shouldIncreaseCost() {
    // Arrange
    String str = "testing";
    InlineCostEstimator.CostEstimator estimator = new InlineCostEstimator.CostEstimator(10);
    int initialCost = estimator.getCost();
    int expectedCost = initialCost + str.length();

    // Act
    estimator.append(str);
    int actualCost = estimator.getCost();

    // Assert
    Assert.assertEquals(expectedCost, actualCost);
  }

  @Test
  public void append_withStringAndCostThresholdExceeded_shouldStopProcessing() {
    // Arrange
    String str = "testing";
    int costThreshold = 5;
    InlineCostEstimator.CostEstimator estimator = new InlineCostEstimator.CostEstimator(costThreshold);
    int initialCost = estimator.getCost();

    // Act
    estimator.append(str);
    boolean continueProcessing = estimator.continueProcessing();

    // Assert
    Assert.assertFalse(continueProcessing);
  }

  @Test
  public void append_withStringAndCostThresholdNotExceeded_shouldContinueProcessing() {
    // Arrange
    String str = "testing";
    int costThreshold = 10;
    InlineCostEstimator.CostEstimator estimator = new InlineCostEstimator.CostEstimator(costThreshold);
    int initialCost = estimator.getCost();

    // Act
    estimator.append(str);
    boolean continueProcessing = estimator.continueProcessing();

    // Assert
    Assert.assertTrue(continueProcessing);
  }

  @Test
  public void getLastChar_withNoAppendedString_shouldReturnNullChar() {
    // Arrange
    InlineCostEstimator.CostEstimator estimator = new InlineCostEstimator.CostEstimator(10);
    char expectedChar = '\0';

    // Act
    char actualChar = estimator.getLastChar();

    // Assert
    Assert.assertEquals(expectedChar, actualChar);
  }

  @Test
  public void getLastChar_withAppendedString_shouldReturnLastChar() {
    // Arrange
    String str = "testing";
    InlineCostEstimator.CostEstimator estimator = new InlineCostEstimator.CostEstimator(10);
    estimator.append(str);
    char expectedChar = str.charAt(str.length() - 1);

    // Act
    char actualChar = estimator.getLastChar();

    // Assert
    Assert.assertEquals(expectedChar, actualChar);
  }

}