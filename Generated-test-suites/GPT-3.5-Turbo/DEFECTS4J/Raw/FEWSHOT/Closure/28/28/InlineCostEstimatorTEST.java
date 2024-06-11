import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {
    
    @Test
    public void testGetCost() {
        Node root = new Node(1);
        int expected = 5;
        int actual = InlineCostEstimator.getCost(root);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetCostWithCostThreshold() {
        Node root = new Node(1);
        int costThreshold = 10;
        int expected = 5;
        int actual = InlineCostEstimator.getCost(root, costThreshold);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCompiledSizeEstimatorAdd() {
        Node root = new Node(1);
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        estimator.add(root);
        int expected = 5;
        int actual = estimator.getCost();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCompiledSizeEstimatorGetCost() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        int expected = 0;
        int actual = estimator.getCost();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testContinueProcessing() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        boolean expected = true;
        boolean actual = estimator.continueProcessing();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetLastChar() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        char expected = '\0';
        char actual = estimator.getLastChar();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppend() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        String str = "test";
        estimator.append(str);
        int expected = 4;
        int actual = estimator.getCost();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAddIdentifier() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        String identifier = "test";
        estimator.addIdentifier(identifier);
        int expected = 2;
        int actual = estimator.getCost();
        assertEquals(expected, actual);
    }
    
}