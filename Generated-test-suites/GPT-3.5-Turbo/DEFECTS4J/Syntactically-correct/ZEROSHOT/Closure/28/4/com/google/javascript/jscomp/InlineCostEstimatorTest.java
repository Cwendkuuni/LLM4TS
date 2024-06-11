import org.junit.Test;

import static org.junit.Assert.*;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost_root() {
        InlineCostEstimator estimator = new InlineCostEstimator();
        
        int cost = estimator.getCost(null);
        
        assertEquals(0, cost);
    }

    @Test
    public void testGetCost_rootAndCostThreshold() {
        InlineCostEstimator estimator = new InlineCostEstimator();
        
        int cost = estimator.getCost(null, 10);
        
        assertEquals(0, cost);
    }

    @Test
    public void testCompiledSizeEstimator_add() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        // Add test node
        Node testNode = new Node();
        
        estimator.add(testNode);
        
        // TODO: Add assertions
    }

    @Test
    public void testCompiledSizeEstimator_getCost() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        
        int cost = estimator.getCost();
        
        assertEquals(0, cost);
    }

    @Test
    public void testCompiledSizeEstimator_continueProcessing() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        
        boolean continueProcessing = estimator.continueProcessing();
        
        assertTrue(continueProcessing);
    }

    @Test
    public void testCompiledSizeEstimator_getLastChar() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        
        char lastChar = estimator.getLastChar();
        
        assertEquals('\0', lastChar);
    }

    @Test
    public void testCompiledSizeEstimator_append() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        String str = "test";
        
        estimator.append(str);
        
        assertEquals(str.charAt(str.length() - 1), estimator.getLastChar());
        assertEquals(str.length(), estimator.getCost());
    }

    @Test
    public void testCompiledSizeEstimator_addIdentifier() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        String identifier = "test";
        
        estimator.addIdentifier(identifier);
        
        assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, estimator.getCost());
    }
}