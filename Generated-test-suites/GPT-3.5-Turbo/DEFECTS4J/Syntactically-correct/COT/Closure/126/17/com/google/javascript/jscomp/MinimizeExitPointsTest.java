
import org.junit.Test;
import static org.junit.Assert.*;

public class MinimizeExitPointsTest {

    @Test
    public void testTryMinimizeExits_matchingExitNode() {
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(null);
        
        Node node = new Node(Token.RETURN);
        assertTrue(minimizeExitPoints.matchingExitNode(node, Token.RETURN, null));
        
        node.addChildToFront(IR.string("label"));
        assertTrue(minimizeExitPoints.matchingExitNode(node, Token.RETURN, "label"));
    }
    
    @Test
    public void testMoveAllFollowing() {
        Node start = new Node(Token.BLOCK);
        start.addChildToFront(IR.returnNode());
        
        Node srcParent = new Node(Token.BLOCK);
        srcParent.addChildToBack(IR.number(1));
        srcParent.addChildToBack(start);
        
        Node destParent = new Node(Token.BLOCK);
        minimizeExitPoints.moveAllFollowing(start, srcParent, destParent);
        
        assertEquals(srcParent.getFirstChild(), destParent.getFirstChild());
        assertEquals(start, destParent.getLastChild());
        assertEquals(IR.returnNode(), destParent.getLastChild().getFirstChild());
    }
    
    // Add more test cases for the other methods in the MinimizeExitPoints class.
    // ...
}

