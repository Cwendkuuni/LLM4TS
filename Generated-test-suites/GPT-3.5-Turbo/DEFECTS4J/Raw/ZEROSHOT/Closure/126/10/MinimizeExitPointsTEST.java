import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MinimizeExitPointsTest {

    private MinimizeExitPoints minimizeExitPoints;

    @Before
    public void setUp() {
        minimizeExitPoints = new MinimizeExitPoints(new AbstractCompiler());
    }

    @Test
    public void testProcess() {
        Node externs = new Node();
        Node root = new Node();

        minimizeExitPoints.process(externs, root);
    }

    @Test
    public void testVisit_LABEL() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();

        minimizeExitPoints.visit(t, n, parent);
    }

    @Test
    public void testVisit_FOR() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();

        minimizeExitPoints.visit(t, n, parent);
    }

    @Test
    public void testVisit_WHILE() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();

        minimizeExitPoints.visit(t, n, parent);
    }

    @Test
    public void testVisit_DO() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();

        minimizeExitPoints.visit(t, n, parent);
    }

    @Test
    public void testVisit_FUNCTION() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();

        minimizeExitPoints.visit(t, n, parent);
    }

    @Test
    public void testTryMinimizeExits() {
        Node n = new Node();
        int exitType = 0;
        String labelName = "";

        minimizeExitPoints.tryMinimizeExits(n, exitType, labelName);
    }

    @Test
    public void testTryMinimizeIfBlockExits() {
        Node srcBlock = new Node();
        Node destBlock = new Node();
        Node ifNode = new Node();
        int exitType = 0;
        String labelName = "";

        minimizeExitPoints.tryMinimizeIfBlockExits(srcBlock, destBlock, ifNode, exitType, labelName);
    }

    @Test
    public void testMatchingExitNode() {
        Node n = new Node();
        int type = 0;
        String labelName = "";

        boolean result = minimizeExitPoints.matchingExitNode(n, type, labelName);

        assertTrue(result);
    }

    @Test
    public void testMoveAllFollowing() {
        Node start = new Node();
        Node srcParent = new Node();
        Node destParent = new Node();

        minimizeExitPoints.moveAllFollowing(start, srcParent, destParent);
    }
}