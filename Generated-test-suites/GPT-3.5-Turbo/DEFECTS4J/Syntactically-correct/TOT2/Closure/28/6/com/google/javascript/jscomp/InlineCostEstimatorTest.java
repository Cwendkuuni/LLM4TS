import static org.junit.Assert.assertEquals;

import com.google.javascript.rhino.Node;
import org.junit.Test;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost() {
        Node root = new Node(Node.Token.FUNCTION);
        root.addChildToBack(new Node(Node.Token.NAME, new Node("testFunction")));
        root.addChildToBack(new Node(Node.Token.NUMBER, new Node("5")));
        
        int cost = InlineCostEstimator.getCost(root);

        assertEquals(2, cost);
    }

    @Test
    public void testGetCostWithCostThreshhold() {
        Node root = new Node(Node.Token.FUNCTION);
        root.addChildToBack(new Node(Node.Token.NAME, new Node("testFunction")));
        root.addChildToBack(new Node(Node.Token.NUMBER, new Node("5")));
        
        int cost = InlineCostEstimator.getCost(root, 4);

        assertEquals(4, cost);
    }

    @Test
    public void testCompiledSizeEstimatorAdd() {
        CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        Node root = new Node(Node.Token.FUNCTION);
        root.addChildToBack(new Node(Node.Token.NAME, new Node("testFunction")));
        
        estimator.add(root);

        assertEquals(4, estimator.getCost());
    }

    @Test
    public void testCompiledSizeEstimatorAddWithIdentifier() {
        CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        estimator.addIdentifier("identifier");

        assertEquals(2, estimator.getCost());
    }

    @Test
    public void testCompiledSizeEstimatorCostThreshhold() {
        CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(5);
        Node root = new Node(Node.Token.STRING, new Node("testString"));
        
        estimator.add(root);

        assertEquals(10, estimator.getCost());
    }

}