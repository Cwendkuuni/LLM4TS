import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

    @Test
    public void getCost_RootNode_ReturnsZero() {
        Node root = new Node();
        int cost = InlineCostEstimator.getCost(root);
        assertEquals(0, cost);
    }

    @Test
    public void getCostWithThreshold_RootNodeAndThreshold_ReturnsZero() {
        Node root = new Node();
        int threshold = 10;
        int cost = InlineCostEstimator.getCost(root, threshold);
        assertEquals(0, cost);
    }

    @Test
    public void getCostWithThreshold_RootNodeAndThresholdExceeds_ReturnsNonZero() {
        Node root = new Node();
        int threshold = 1;
        int cost = InlineCostEstimator.getCost(root, threshold);
        assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, cost);
    }

    @Test
    public void append_StringWithWhitespaceAndNonWhitespace_CorrectlyUpdatesCost() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        String str = "a bc";
        estimator.append(str);
        assertEquals(str.length(), estimator.getCost());
    }

    @Test
    public void addIdentifier_ValidIdentifier_CorrectlyUpdatesCost() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        String identifier = "valid";
        estimator.addIdentifier(identifier);
        assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, estimator.getCost());
    }

    @Test
    public void addIdentifier_EmptyIdentifier_CorrectlyUpdatesCost() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        String identifier = "";
        estimator.addIdentifier(identifier);
        assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, estimator.getCost());
    }

    @Test
    public void addIdentifier_NullIdentifier_CorrectlyUpdatesCost() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        String identifier = null;
        estimator.addIdentifier(identifier);
        assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, estimator.getCost());
    }

    @Test
    public void CompiledSizeEstimator_ContinueProcessingFalse_CorrectlyUpdatesContinueProcessing() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(2);
        estimator.leave();
        assertFalse(estimator.continueProcessing());
    }

    @Test
    public void CompiledSizeEstimator_GetLastChar_CorrectlyRetrievesLastChar() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        estimator.append("abc");
        assertEquals('c', estimator.getLastChar());
    }
}