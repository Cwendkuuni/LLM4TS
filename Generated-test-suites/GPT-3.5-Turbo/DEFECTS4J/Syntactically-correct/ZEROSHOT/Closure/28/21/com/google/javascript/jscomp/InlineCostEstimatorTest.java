import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class InlineCostEstimatorTest {

    private InlineCostEstimator inlineCostEstimator;
    private Node testNode;
    private int testThreshold;

    @Before
    public void setUp() {
        inlineCostEstimator = new InlineCostEstimator();
        testNode = new Node();
        testThreshold = Integer.MAX_VALUE;
    }

    @Test
    public void testGetCost() {
        int result = inlineCostEstimator.getCost(testNode);
        int expected = 0;
        assertEquals(expected, result);
    }

    @Test
    public void testGetCostWithThreshold() {
        int result = inlineCostEstimator.getCost(testNode, testThreshold);
        int expected = 0;
        assertEquals(expected, result);
    }

    @Test
    public void testCompiledSizeEstimatorAdd() {
        InlineCostEstimator.CompiledSizeEstimator estimator = inlineCostEstimator.new CompiledSizeEstimator(testThreshold);
        estimator.add(testNode);
    }

    @Test
    public void testCompiledSizeEstimatorGetCost() {
        InlineCostEstimator.CompiledSizeEstimator estimator = inlineCostEstimator.new CompiledSizeEstimator(testThreshold);
        int result = estimator.getCost();
        int expected = 0;
        assertEquals(expected, result);
    }

}