import org.junit.Test;
import static org.junit.Assert.*;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost() {
        // Typical use case
        Node root = new Node(NodeType.ROOT);
        root.addChildToFront(new Node(NodeType.EXPRESSION));
        root.getLastChild().addChildToFront(new Node(NodeType.VAR));
        root.getLastChild().getLastChild().addChildToFront(new Node(NodeType.NAME));

        int result = InlineCostEstimator.getCost(root);
        assertEquals(1, result);

        // Edge case
        Node emptyRoot = new Node(NodeType.ROOT);
        int emptyResult = InlineCostEstimator.getCost(emptyRoot);
        assertEquals(0, emptyResult);

        // Error scenario
        Node nullNode = null;
        int nullResult = InlineCostEstimator.getCost(nullNode);
        assertEquals(0, nullResult);
    }

    @Test
    public void testGetCostWithThreshold() {
        // Typical use case
        Node root = new Node(NodeType.ROOT);
        root.addChildToFront(new Node(NodeType.EXPRESSION));
        root.getLastChild().addChildToFront(new Node(NodeType.VAR));
        root.getLastChild().getLastChild().addChildToFront(new Node(NodeType.NAME));

        int result = InlineCostEstimator.getCost(root, 5);
        assertEquals(1, result);

        // Edge case
        Node emptyRoot = new Node(NodeType.ROOT);
        int emptyResult = InlineCostEstimator.getCost(emptyRoot, 10);
        assertEquals(0, emptyResult);

        // Error scenario
        Node nullNode = null;
        int nullResult = InlineCostEstimator.getCost(nullNode, 8);
        assertEquals(0, nullResult);
    }

    @Test
    public void testCompiledSizeEstimatorAdd() {
        // Typical use case
        CompiledSizeEstimator estimator = new CompiledSizeEstimator(5);
        Node root = new Node(NodeType.ROOT);
        root.addChildToFront(new Node(NodeType.EXPRESSION));
        estimator.add(root);

        int result = estimator.getCost();
        assertEquals(1, result);

        // Edge case
        CompiledSizeEstimator emptyEstimator = new CompiledSizeEstimator(10);
        Node emptyRoot = new Node(NodeType.ROOT);
        emptyEstimator.add(emptyRoot);

        int emptyResult = emptyEstimator.getCost();
        assertEquals(0, emptyResult);

        // Error scenario
        CompiledSizeEstimator nullEstimator = null;
        Node nullRoot = new Node(NodeType.ROOT);
        nullRoot.addChildToFront(new Node(NodeType.EXPRESSION));
        nullRoot.getLastChild().addChildToFront(new Node(NodeType.VAR));

        assertThrows(NullPointerException.class, () -> {
            nullEstimator.add(nullRoot);
        });
    }
}