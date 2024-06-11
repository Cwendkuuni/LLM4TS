import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class UnreachableCodeEliminationTest {

    @Test
    public void testRemoveDeadExprStatementSafely() {
        UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination();
        Node n1 = new Node(Token.EMPTY);
        Node parent1 = new Node(Token.SCRIPT);
        n1.setParent(parent1);
        unreachableCodeElimination.removeDeadExprStatementSafely(n1);
        Assert.assertNull(n1.getParent());

        Node n2 = new Node(Token.BLOCK);
        Node parent2 = new Node(Token.SCRIPT);
        n2.setParent(parent2);
        unreachableCodeElimination.removeDeadExprStatementSafely(n2);
        Assert.assertNull(n2.getParent());

        Node n3 = new Node(Token.BLOCK);
        n3.addChildToFront(new Node(Token.EMPTY));
        Node parent3 = new Node(Token.TRY);
        parent3.addChildToFront(new Node(Token.CATCH));
        n3.setParent(parent3);
        unreachableCodeElimination.removeDeadExprStatementSafely(n3);
        Assert.assertNotNull(n3.getParent());

        Node n4 = new Node(Token.DO);
        Node parent4 = new Node(Token.SCRIPT);
        n4.setParent(parent4);
        unreachableCodeElimination.removeDeadExprStatementSafely(n4);
        Assert.assertNotNull(n4.getParent());
    }

    @Test
    public void testTryRemoveUnconditionalBranching() {
        UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination();
        Node n1 = new Node(Token.BREAK);
        unreachableCodeElimination.tryRemoveUnconditionalBranching(n1);
        Assert.assertEquals(n1, unreachableCodeElimination.tryRemoveUnconditionalBranching(n1));

        Node n2 = new Node(Token.RETURN);
        unreachableCodeElimination.tryRemoveUnconditionalBranching(n2);
        Assert.assertEquals(n2, unreachableCodeElimination.tryRemoveUnconditionalBranching(n2));

        Node n3 = new Node(Token.CONTINUE);
        unreachableCodeElimination.tryRemoveUnconditionalBranching(n3);
        Assert.assertEquals(n3, unreachableCodeElimination.tryRemoveUnconditionalBranching(n3));
    }
}