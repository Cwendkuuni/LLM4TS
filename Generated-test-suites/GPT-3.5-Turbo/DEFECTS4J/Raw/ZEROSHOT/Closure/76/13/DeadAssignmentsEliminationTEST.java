import org.junit.Assert;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);

        // Test case 1: Empty externs and root nodes
        Node externs = new Node(Token.BLOCK);
        Node root = new Node(Token.BLOCK);

        elimination.process(externs, root);

        // Test case 2: Externs and root nodes with some contents
        Node externs2 = new Node(Token.BLOCK);
        Node root2 = new Node(Token.BLOCK);

        Node assignmentNode1 = new Node(Token.ASSIGN);
        Node nameNode1 = new Node(Token.NAME, "x");
        Node valueNode1 = new Node(Token.NUMBER, 1);
        assignmentNode1.addChildToBack(nameNode1);
        assignmentNode1.addChildToBack(valueNode1);
        root2.addChildToBack(assignmentNode1);

        Node assignmentNode2 = new Node(Token.ASSIGN);
        Node nameNode2 = new Node(Token.NAME, "y");
        Node valueNode2 = new Node(Token.NUMBER, 2);
        assignmentNode2.addChildToBack(nameNode2);
        assignmentNode2.addChildToBack(valueNode2);
        root2.addChildToBack(assignmentNode2);

        elimination.process(externs2, root2);

        // Assertion: No changes to externs and root nodes

        Assert.assertEquals(0, externs2.getChildCount());
        Assert.assertEquals(2, root2.getChildCount());
        Assert.assertEquals(assignmentNode1, root2.getFirstChild());
        Assert.assertEquals(assignmentNode2, root2.getLastChild());
    }
}