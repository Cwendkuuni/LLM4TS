import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost() {
        Node root = new Node(Node.SCRIPT);
        int cost = InlineCostEstimator.getCost(root);
        assertEquals(0, cost);
    }

    @Test
    public void testGetCostWithThreshold() {
        Node root = new Node(Node.SCRIPT);
        int cost = InlineCostEstimator.getCost(root, 100);
        assertEquals(0, cost);
    }
    
    @Test
    public void testCompiledSizeEstimatorAdd() {
        CompiledSizeEstimator cse = new CompiledSizeEstimator(100);
        Node root = new Node(Node.SCRIPT);
        cse.add(root);
        int cost = cse.getCost();
        assertEquals(0, cost);
    }

    @Test
    public void testAppend() {
        CompiledSizeEstimator cse = new CompiledSizeEstimator(100);
        cse.append("abc");
        int cost = cse.getCost();
        assertEquals(3, cost);
    }

    @Test
    public void testAddIdentifier() {
        CompiledSizeEstimator cse = new CompiledSizeEstimator(100);
        cse.addIdentifier("abc");
        int cost = cse.getCost();
        assertEquals(2, cost);
    }
    
}