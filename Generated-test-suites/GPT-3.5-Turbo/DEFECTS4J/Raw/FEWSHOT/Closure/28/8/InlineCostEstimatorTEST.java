import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class InlineCostEstimatorTest {
    
    @Test
    public void testGetCost() {
        Node root = new Node(NodeType.SCRIPT);
        assertEquals(0, InlineCostEstimator.getCost(root));
        
        Node node1 = new Node(NodeType.VAR);
        root.addChildToFront(node1);
        assertEquals(4, InlineCostEstimator.getCost(root));
        
        Node node2 = new Node(NodeType.NUMBER);
        node1.addChildToFront(node2);
        assertEquals(12, InlineCostEstimator.getCost(root));
        
        Node node3 = new Node(NodeType.TYPEOFNAME);
        node2.addChildToFront(node3);
        assertEquals(22, InlineCostEstimator.getCost(root));
    }
    
    @Test
    public void testGetCostWithThreshold() {
        Node root = new Node(NodeType.SCRIPT);
        assertEquals(0, InlineCostEstimator.getCost(root, 10));
        
        Node node1 = new Node(NodeType.VAR);
        root.addChildToFront(node1);
        assertEquals(4, InlineCostEstimator.getCost(root, 10));
        
        Node node2 = new Node(NodeType.NUMBER);
        node1.addChildToFront(node2);
        assertEquals(8, InlineCostEstimator.getCost(root, 10));
        
        Node node3 = new Node(NodeType.TYPEOFNAME);
        node2.addChildToFront(node3);
        assertEquals(14, InlineCostEstimator.getCost(root, 10));
    }
    
    @Test
    public void testCompiledSizeEstimator() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        assertEquals(true, estimator.continueProcessing());
        assertEquals(0, estimator.getCost());
        assertEquals('\0', estimator.getLastChar());
        
        estimator.append("abc");
        assertEquals(true, estimator.continueProcessing());
        assertEquals(3, estimator.getCost());
        assertEquals('c', estimator.getLastChar());
        
        estimator.append("def");
        assertEquals(false, estimator.continueProcessing());
        assertEquals(6, estimator.getCost());
        assertEquals('f', estimator.getLastChar());
        
        estimator.addIdentifier("identifier");
        assertEquals(8, estimator.getCost());
    }
}