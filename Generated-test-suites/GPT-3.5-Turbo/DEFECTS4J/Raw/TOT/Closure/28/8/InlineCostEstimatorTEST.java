import org.junit.Test;
import static org.junit.Assert.*;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost_WithRoot_ShouldReturnCorrectCost() {
        // Arrange
        Node root = new Node();
        InlineCostEstimator estimator = new InlineCostEstimator();

        // Act
        int cost = estimator.getCost(root);

        // Assert
        assertEquals(0, cost);
    }

    @Test
    public void testGetCost_WithRootAndCostThreshold_ShouldReturnCorrectCost() {
        // Arrange
        Node root = new Node();
        int costThreshold = 10;
        InlineCostEstimator estimator = new InlineCostEstimator();

        // Act
        int cost = estimator.getCost(root, costThreshold);

        // Assert
        assertEquals(0, cost);
    }

    @Test
    public void testAdd_WithRoot_ShouldAddNodeToEstimator() {
        // Arrange
        Node root = new Node();
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);

        // Act
        estimator.add(root);
        int cost = estimator.getCost();

        // Assert
        assertEquals(0, cost);
    }

    @Test
    public void testAppend_WithString_ShouldUpdateCost() {
        // Arrange
        String str = "abc";
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);

        // Act
        estimator.append(str);
        int cost = estimator.getCost();

        // Assert
        assertEquals(3, cost);
    }

    @Test
    public void testContinueProcessing_WithCostBelowThreshold_ShouldReturnTrue() {
        // Arrange
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);

        // Act
        boolean continueProcessing = estimator.continueProcessing();

        // Assert
        assertTrue(continueProcessing);
    }

    @Test
    public void testContinueProcessing_WithCostEqualToThreshold_ShouldReturnFalse() {
        // Arrange
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        estimator.add("abc");

        // Act
        boolean continueProcessing = estimator.continueProcessing();

        // Assert
        assertFalse(continueProcessing);
    }

    @Test
    public void testAddIdentifier_WithString_ShouldAddIdentifierToEstimator() {
        // Arrange
        String identifier = "foo";
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);

        // Act
        estimator.addIdentifier(identifier);
        int cost = estimator.getCost();

        // Assert
        assertEquals(2, cost);
    }

    @Test
    public void testGetCost_WithLargeRootAndCostThreshold_ShouldReturnCorrectCost() {
        // Arrange
        Node root = createLargeRoot();
        int costThreshold = 100;
        InlineCostEstimator estimator = new InlineCostEstimator();

        // Act
        int cost = estimator.getCost(root, costThreshold);

        // Assert
        assertEquals(101, cost);
    }

    private Node createLargeRoot() {
        Node root = new Node();
        for (int i = 1; i <= 100; i++) {
            Node child = new Node();
            root.addChildToBack(child);
        }
        return root;
    }
}