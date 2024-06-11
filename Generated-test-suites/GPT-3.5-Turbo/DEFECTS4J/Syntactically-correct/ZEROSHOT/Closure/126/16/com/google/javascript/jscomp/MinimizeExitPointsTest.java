import static org.junit.Assert.*;
import org.junit.Test;

public class MinimizeExitPointsTest {

    @Test
    public void testProcess() {
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(new AbstractCompiler());
        assertNull(minimizeExitPoints.process(null, null));
    }

    @Test
    public void testVisit() {
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(new AbstractCompiler());

        // Test case 1
        NodeTraversal t1 = new NodeTraversal(null, null);
        Node n1 = new Node(Token.LABEL);
        n1.addChildToFront(new Node(Token.BREAK));
        n1.getFirstChild().addChildToFront(new Node("label1"));
        minimizeExitPoints.visit(t1, n1, null);

        // Test case 2
        NodeTraversal t2 = new NodeTraversal(null, null);
        Node n2 = new Node(Token.FOR);
        Node loopCodeBlock = new Node(Token.BLOCK);
        n2.addChildToFront(loopCodeBlock);
        minimizeExitPoints.visit(t2, n2, null);

        // Test case 3
        NodeTraversal t3 = new NodeTraversal(null, null);
        Node n3 = new Node(Token.WHILE);
        Node loopCodeBlock2 = new Node(Token.BLOCK);
        n3.addChildToFront(loopCodeBlock2);
        minimizeExitPoints.visit(t3, n3, null);

        // Test case 4
        NodeTraversal t4 = new NodeTraversal(null, null);
        Node n4 = new Node(Token.DO);
        Node loopCodeBlock3 = new Node(Token.BLOCK);
        n4.addChildToFront(loopCodeBlock3);
        minimizeExitPoints.visit(t4, n4, null);

        // Test case 5
        NodeTraversal t5 = new NodeTraversal(null, null);
        Node n5 = new Node(Token.FUNCTION);
        n5.addChildToFront(new Node(Token.RETURN));
        minimizeExitPoints.visit(t5, n5, null);
    }

    @Test
    public void testTryMinimizeExits() {
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(new AbstractCompiler());

        // Test case 1
        Node n1 = new Node(Token.BLOCK);
        minimizeExitPoints.tryMinimizeExits(n1, Token.RETURN, null);

        // Test case 2
        Node n2 = new Node(Token.IF);
        n2.addChildToFront(new Node(Token.RETURN));
        n2.getFirstChild().addChildToFront(new Node(Token.NAME));

        Node trueBlock = new Node(Token.BLOCK);
        trueBlock.addChildToFront(new Node("blah"));
        trueBlock.addChildToBack(new Node("foo"));

        Node falseBlock = new Node(Token.BLOCK);
        falseBlock.addChildToFront(new Node("blah2"));

        n2.addChildToBack(trueBlock);
        n2.addChildToBack(falseBlock);

        minimizeExitPoints.tryMinimizeExits(n2, Token.IF, null);

        // Test case 3
        Node labelParent = new Node(Token.BLOCK);
        Node n3 = new Node(Token.LABEL);
        n3.addChildToFront(new Node("label1"));
        Node labelBlock = new Node(Token.BLOCK);
        labelBlock.addChildToFront(new Node(Token.RETURN));

        n3.addChildToBack(labelBlock);
        labelParent.addChildToBack(n3);

        minimizeExitPoints.tryMinimizeExits(n3, Token.BREAK, "label1");
    }

    @Test
    public void testMatchingExitNode() {
        Node n1 = new Node(Token.RETURN);
        assertTrue(minimizeExitPoints.matchingExitNode(n1, Token.RETURN, null));

        Node n2 = new Node(Token.BREAK);
        n2.addChildToFront(new Node("label1"));
        assertTrue(minimizeExitPoints.matchingExitNode(n2, Token.BREAK, "label1"));

        Node n3 = new Node(Token.BREAK);
        assertFalse(minimizeExitPoints.matchingExitNode(n3, Token.BREAK, null));

        Node n4 = new Node(Token.CONTINUE);
        assertFalse(minimizeExitPoints.matchingExitNode(n4, Token.CONTINUE, null));
    }

    @Test
    public void testMoveAllFollowing() {
        Node start = new Node("start");
        Node srcParent = new Node("srcParent");
        Node destParent = new Node("destParent");

        Node n1 = new Node("n1");
        Node n2 = new Node("n2");
        Node n3 = new Node("n3");

        start.addChildToBack(n1);
        srcParent.addChildToBack(start);
        srcParent.addChildToBack(n2);
        srcParent.addChildToBack(n3);

        minimizeExitPoints.moveAllFollowing(start, srcParent, destParent);

        assertEquals(1, srcParent.getChildCount());
        assertEquals(n1, srcParent.getFirstChild());
        assertEquals(3, destParent.getChildCount());
        assertEquals(start, destParent.getFirstChild());
        assertEquals(n2, destParent.getChildAtIndex(1));
        assertEquals(n3, destParent.getLastChild());
    }

}