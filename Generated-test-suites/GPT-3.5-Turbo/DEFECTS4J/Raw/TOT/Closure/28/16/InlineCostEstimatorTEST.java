import com.google.javascript.rhino.Node;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost_withRootNode_shouldReturnCost() {
        Node root = new Node(Node.TOKEN);
        int expectedCost = 1;
        
        int actualCost = InlineCostEstimator.getCost(root);
        
        assertEquals(expectedCost, actualCost);
    }
    
    @Test
    public void testGetCost_withRootNodeAndCostThreshold_shouldReturnCost() {
        Node root = new Node(Node.TOKEN);
        int costThreshold = 100;
        int expectedCost = 1;
        
        int actualCost = InlineCostEstimator.getCost(root, costThreshold);
        
        assertEquals(expectedCost, actualCost);
    }
    
    @Test
    public void testGetCost_withRootNodeAndMaxCostThreshold_shouldReturnCost() {
        Node root = new Node(Node.TOKEN);
        int maxCostThreshold = Integer.MAX_VALUE;
        int expectedCost = 1;
        
        int actualCost = InlineCostEstimator.getCost(root, maxCostThreshold);
        
        assertEquals(expectedCost, actualCost);
    }
    
    @Test
    public void testCompiledSizeEstimator_add_withRootNode_shouldIncrementCost() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        Node root = new Node(Node.TOKEN);
        int expectedCost = 1;
        
        estimator.add(root);
        int actualCost = estimator.getCost();
        
        assertEquals(expectedCost, actualCost);
    }
    
    @Test
    public void testCompiledSizeEstimator_getCost_shouldReturnCost() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        estimator.add(new Node(Node.TOKEN));
        estimator.add(new Node(Node.TOKEN));
        int expectedCost = 2;
        
        int actualCost = estimator.getCost();
        
        assertEquals(expectedCost, actualCost);
    }
    
    @Test
    public void testCompiledSizeEstimator_append_withString_shouldIncrementCost() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        String str = "abc";
        int expectedCost = 3;
        
        estimator.append(str);
        int actualCost = estimator.getCost();
        
        assertEquals(expectedCost, actualCost);
    }
    
    @Test
    public void testCompiledSizeEstimator_append_withEmptyString_shouldNotIncrementCost() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        String str = "";
        int expectedCost = 0;
        
        estimator.append(str);
        int actualCost = estimator.getCost();
        
        assertEquals(expectedCost, actualCost);
    }
    
    @Test
    public void testCompiledSizeEstimator_addIdentifier_withIdentifier_shouldIncrementCost() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        String identifier = "abcd";
        int expectedCost = InlineCostEstimator.ESTIMATED_IDENTIFIER_COST;
        
        estimator.addIdentifier(identifier);
        int actualCost = estimator.getCost();
        
        assertEquals(expectedCost, actualCost);
    }
    
    @Test
    public void testCompiledSizeEstimator_addIdentifier_withEmptyIdentifier_shouldNotIncrementCost() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        String identifier = "";
        int expectedCost = 0;
        
        estimator.addIdentifier(identifier);
        int actualCost = estimator.getCost();
        
        assertEquals(expectedCost, actualCost);
    }
}