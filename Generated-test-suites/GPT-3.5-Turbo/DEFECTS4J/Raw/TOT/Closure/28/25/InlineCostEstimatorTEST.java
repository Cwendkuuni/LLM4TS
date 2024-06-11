import org.junit.Assert;
import org.junit.Test;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost_returnsCorrectCost() {
        // Arrange
        Node root = new Node();
        InlineCostEstimator estimator = new InlineCostEstimator();

        // Act
        int cost = estimator.getCost(root);

        // Assert
        Assert.assertEquals(0, cost);
    }

    @Test
    public void testGetCost_withCostThreshhold_returnsCorrectCost() {
        // Arrange
        Node root = new Node();
        int costThreshhold = 100;
        InlineCostEstimator estimator = new InlineCostEstimator();

        // Act
        int cost = estimator.getCost(root, costThreshhold);

        // Assert
        Assert.assertEquals(0, cost);
    }

    @Test
    public void testCompiledSizeEstimator_addIdentifier_increasesCost() {
        // Arrange
        String identifier = "test";
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);

        // Act
        estimator.addIdentifier(identifier);
        int cost = estimator.getCost();

        // Assert
        Assert.assertEquals(identifier.length(), cost);
    }

    @Test
    public void testCompiledSizeEstimator_append_increasesCost() {
        // Arrange
        String str = "test";
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);

        // Act
        estimator.append(str);
        int cost = estimator.getCost();

        // Assert
        Assert.assertEquals(str.length(), cost);
    }

    @Test
    public void testCompiledSizeEstimator_append_withMaxCost_reachesCostThreshhold() {
        // Arrange
        String str = "test";
        int costThreshhold = str.length() - 1;
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(costThreshhold);

        // Act
        estimator.append(str);
        int cost = estimator.getCost();

        // Assert
        Assert.assertEquals(costThreshhold, cost);
        Assert.assertFalse(estimator.continueProcessing());
    }
}