import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class MinimizeExitPointsTest {

    private static MinimizeExitPoints minimizeExitPoints;

    @BeforeClass
    public static void setUp() {
        minimizeExitPoints = new MinimizeExitPoints();
    }

    @Test
    public void testProcess() {
        minimizeExitPoints.process(null, null);
    }

    @Test
    public void testVisit() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.EMPTY);
        Node parent = new Node(Token.EMPTY);

        minimizeExitPoints.visit(t, n, parent);
    }

    @Test
    public void testTryMinimizeExits() {
        Node n = new Node(Token.EMPTY);
        int exitType = Token.RETURN;
        String labelName = "label";

        minimizeExitPoints.tryMinimizeExits(n, exitType, labelName);
    }

    @Test
    public void testTryMinimizeIfBlockExits() {
        Node srcBlock = new Node(Token.EMPTY);
        Node destBlock = new Node(Token.EMPTY);
        Node ifNode = new Node(Token.IF);
        int exitType = Token.RETURN;
        String labelName = "label";

        minimizeExitPoints.tryMinimizeIfBlockExits(srcBlock, destBlock, ifNode, exitType, labelName);
    }

    @Test
    public void testMatchingExitNode() {
        Node n = new Node(Token.RETURN);
        int type = Token.RETURN;
        String labelName = null;

        assertTrue(minimizeExitPoints.matchingExitNode(n, type, labelName));
    }

    @Test
    public void testMoveAllFollowing() {
        Node start = new Node(Token.START);
        Node srcParent = new Node(Token.PARENT);
        Node destParent = new Node(Token.PARENT);

        minimizeExitPoints.moveAllFollowing(start, srcParent, destParent);
    }
}