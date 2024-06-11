import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost() {
        Node root = new Node();
        int cost = InlineCostEstimator.getCost(root);
        assertEquals(0, cost);
    }

    @Test
    public void testGetCostWithCostThreshold() {
        Node root = new Node();
        int costThreshhold = 10;
        int cost = InlineCostEstimator.getCost(root, costThreshhold);
        assertEquals(0, cost);
    }
    
    @Test
    public void testCompiledSizeEstimatorAdd() {
        Node root = new Node();
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        estimator.add(root);
        int cost = estimator.getCost();
        assertEquals(0, cost);
    }
    
    @Test
    public void testCompiledSizeEstimatorGetCost() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        int cost = estimator.getCost();
        assertEquals(0, cost);
    }
    
    @Test
    public void testCompiledSizeEstimatorContinueProcessing() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        boolean continueProcessing = estimator.continueProcessing();
        assertTrue(continueProcessing);
    }
    
    @Test
    public void testCompiledSizeEstimatorGetLastChar() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        char lastChar = estimator.getLastChar();
        assertEquals('\0', lastChar);
    }
    
    @Test
    public void testCompiledSizeEstimatorAppend() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        estimator.append("test");
        char lastChar = estimator.getLastChar();
        int cost = estimator.getCost();
        assertFalse(estimator.continueProcessing());
        assertEquals('t', lastChar);
        assertEquals(4, cost);
    }
    
    @Test
    public void testCompiledSizeEstimatorAddIdentifier() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        estimator.addIdentifier("test");
        int cost = estimator.getCost();
        assertEquals(4, cost);
    }
    
}