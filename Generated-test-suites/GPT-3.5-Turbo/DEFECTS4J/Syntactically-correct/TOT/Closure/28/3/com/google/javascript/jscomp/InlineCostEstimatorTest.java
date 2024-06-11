import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class InlineCostEstimatorTest {

    @Test
    public void getCost_WithRootNode_ReturnsCost() {
        // Arrange
        InlineCostEstimator estimator = new InlineCostEstimator();
        Node root = new Node();

        // Act
        int cost = estimator.getCost(root);

        // Assert
        assertEquals(0, cost);
    }

    @Test
    public void getCost_WithRootNodeAndCostThreshold_ReturnsCost() {
        // Arrange
        InlineCostEstimator estimator = new InlineCostEstimator();
        Node root = new Node();
        int costThreshold = 100;

        // Act
        int cost = estimator.getCost(root, costThreshold);

        // Assert
        assertEquals(0, cost);
    }

    @Test
    public void CompiledSizeEstimator_Add_WithRootNode_CalculatesCost() {
        // Arrange
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        Node root = new Node();

        // Act
        estimator.add(root);
        int cost = estimator.getCost();

        // Assert
        assertEquals(0, cost);
    }

    @Test
    public void CompiledSizeEstimator_AddIdentifier_WithIdentifier_CalculatesCost() {
        // Arrange
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        String identifier = "abc";

        // Act
        estimator.addIdentifier(identifier);
        int cost = estimator.getCost();

        // Assert
        assertEquals(2, cost);
    }

    @Test
    public void CompiledSizeEstimator_AddIdentifier_WithEmptyIdentifier_CalculatesCost() {
        // Arrange
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        String identifier = "";

        // Act
        estimator.addIdentifier(identifier);
        int cost = estimator.getCost();

        // Assert
        assertEquals(0, cost);
    }

    @Test
    public void CompiledSizeEstimator_Add_WithEmptyString_CalculatesCost() {
        // Arrange
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        String str = "";

        // Act
        estimator.append(str);
        int cost = estimator.getCost();

        // Assert
        assertEquals(0, cost);
    }

    @Test
    public void CompiledSizeEstimator_Add_WithNonEmptyString_CalculatesCost() {
        // Arrange
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        String str = "Hello World";

        // Act
        estimator.append(str);
        int cost = estimator.getCost();

        // Assert
        assertEquals(11, cost);
    }

    @Test
    public void CompiledSizeEstimator_Add_WithMaxCostThreshold_ExitsProcessing() {
        // Arrange
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        String str = "Hello World";

        // Act
        estimator.append(str);
        boolean continueProcessing = estimator.continueProcessing();

        // Assert
        assertEquals(false, continueProcessing);
    }

    @Test
    public void CompiledSizeEstimator_Add_WithNotMaxCostThreshold_ContinuesProcessing() {
        // Arrange
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        String str = "Hello World";

        // Act
        estimator.append(str);
        boolean continueProcessing = estimator.continueProcessing();

        // Assert
        assertEquals(true, continueProcessing);
    }

}