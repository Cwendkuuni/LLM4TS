import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void should_report_global_this() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

        // Act
        NodeTraversal traversal = NodeTraversal.builder().setCallback(checkGlobalThis).build();
        Node node = new Node(Token.THIS);

        // Assert
        assertTrue(checkGlobalThis.shouldReportThis(node, null));
    }

    @Test
    public void should_not_report_this_on_left_side_of_assign_possible_this_access() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

        // Act
        NodeTraversal traversal = NodeTraversal.builder().setCallback(checkGlobalThis).build();
        Node assignNode = new Node(Token.ASSIGN);
        Node leftNode = new Node(Token.THIS);
        assignNode.addChildToBack(leftNode);

        // Assert
        assertFalse(checkGlobalThis.shouldReportThis(leftNode, assignNode));
    }

    // Add additional test cases for other methods in the CheckGlobalThis class

}