
import org.junit.Test;
import static org.junit.Assert.*;

public class MinimizeExitPointsTest {

    @Test
    public void testVisit_LabelNodeMatchingExitTypeAndLabelName_RemovesExitNode() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
        Node n = new Node(Token.LABEL);
        Node lastChild = new Node(Token.BREAK);
        lastChild.addChildToFront(new Node(Token.NAME, "labelName"));
        n.addChildToBack(lastChild);

        // Act
        minimizeExitPoints.tryMinimizeExits(n, Token.BREAK, "labelName");

        // Assert
        assertTrue(n.isEmpty());
    }

    @Test
    public void testVisit_LabelNodeNotMatchingExitType_DoesNotRemoveExitNode() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
        Node n = new Node(Token.LABEL);
        Node lastChild = new Node(Token.RETURN);
        n.addChildToBack(lastChild);

        // Act
        minimizeExitPoints.tryMinimizeExits(n, Token.BREAK, null);

        // Assert
        assertFalse(n.isEmpty());
    }

    @Test
    public void testVisit_ForNodeMatchingExitType_RemovesExitNode() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
        Node n = new Node(Token.FOR);
        Node loopCodeBlock = new Node(Token.BLOCK);
        Node lastChild = new Node(Token.CONTINUE);
        loopCodeBlock.addChildToBack(lastChild);
        n.addChildToBack(loopCodeBlock);

        // Act
        minimizeExitPoints.tryMinimizeExits(n, Token.CONTINUE, null);

        // Assert
        assertTrue(loopCodeBlock.isEmpty());
    }

    @Test
    public void testVisit_WhileNodeMatchingExitType_RemovesExitNode() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
        Node n = new Node(Token.WHILE);
        Node loopCodeBlock = new Node(Token.BLOCK);
        Node lastChild = new Node(Token.CONTINUE);
        loopCodeBlock.addChildToBack(lastChild);
        n.addChildToBack(loopCodeBlock);

        // Act
        minimizeExitPoints.tryMinimizeExits(n, Token.CONTINUE, null);

        // Assert
        assertTrue(loopCodeBlock.isEmpty());
    }

    @Test
    public void testVisit_DoNodeMatchingExitType_RemovesExitNode() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
        Node n = new Node(Token.DO);
        Node loopCodeBlock = new Node(Token.BLOCK);
        Node lastChild = new Node(Token.BREAK);
        loopCodeBlock.addChildToBack(lastChild);
        n.addChildToBack(loopCodeBlock);

        // Act
        minimizeExitPoints.tryMinimizeExits(n, Token.BREAK, null);

        // Assert
        assertTrue(loopCodeBlock.isEmpty());
    }

    @Test
    public void testVisit_FunctionNodeMatchingExitType_RemovesExitNode() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
        Node n = new Node(Token.FUNCTION);
        Node lastChild = new Node(Token.RETURN);
        n.addChildToBack(lastChild);

        // Act
        minimizeExitPoints.tryMinimizeExits(n, Token.RETURN, null);

        // Assert
        assertTrue(n.isEmpty());
    }

    @Test
    public void testMatchingExitNode_ReturnNodeWithoutChild_MatchesExitType() {
        // Arrange
        Node n = new Node(Token.RETURN);

        // Act
        boolean result = minimizeExitPoints.matchingExitNode(n, Token.RETURN, null);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testMatchingExitNode_ReturnNodeWithChild_DoesNotMatchExitType() {
        // Arrange
        Node n = new Node(Token.RETURN);
        Node childNode = new Node(Token.NAME);
        n.addChildToFront(childNode);

        // Act
        boolean result = minimizeExitPoints.matchingExitNode(n, Token.RETURN, null);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testMatchingExitNode_BreakNodeWithoutLabel_DoesNotMatchExitType() {
        // Arrange
        Node n = new Node(Token.BREAK);

        // Act
        boolean result = minimizeExitPoints.matchingExitNode(n, Token.BREAK, "labelName");

        // Assert
        assertFalse(result);
    }

    @Test
    public void testMatchingExitNode_BreakNodeWithLabel_DoesNotMatchExitType() {
        // Arrange
        Node n = new Node(Token.BREAK);
        Node labelNode = new Node(Token.NAME, "labelName");
        n.addChildToFront(labelNode);

        // Act
        boolean result = minimizeExitPoints.matchingExitNode(n, Token.BREAK, null);

        // Assert
        assertFalse(result);
    }
}
