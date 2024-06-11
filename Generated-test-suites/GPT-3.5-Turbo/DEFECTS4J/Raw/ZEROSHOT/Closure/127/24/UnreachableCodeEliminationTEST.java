import org.junit.Test;
import static org.junit.Assert.*;

public class UnreachableCodeEliminationTest {

    @Test
    public void testProcess() {
        UnreachableCodeElimination uce = new UnreachableCodeElimination(new AbstractCompiler(), true);
        Node externs = new Node();
        Node toplevel = new Node();

        uce.process(externs, toplevel);
    }

    @Test
    public void testEliminationPass() {
        NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(), null);
        Node n = new Node();

        UnreachableCodeElimination.EliminationPass ep = new UnreachableCodeElimination(traversal.getCfg());

        ep.visit(traversal, n, null);
    }

    @Test
    public void testTryRemoveUnconditionalBranching() {
        Node n = new Node();

        UnreachableCodeElimination.EliminationPass ep = new UnreachableCodeElimination(traversal.getCfg());

        ep.tryRemoveUnconditionalBranching(n);
    }

    @Test
    public void testComputeFollowing() {
        Node n = new Node();

        UnreachableCodeElimination.EliminationPass ep = new UnreachableCodeElimination(traversal.getCfg());

        Node following = ep.computeFollowing(n);
    }

    @Test
    public void testRemoveDeadExprStatementSafely() {
        Node n = new Node();

        UnreachableCodeElimination.EliminationPass ep = new UnreachableCodeElimination(traversal.getCfg());

        ep.removeDeadExprStatementSafely(n);
    }

    @Test
    public void testRemoveNode() {
        Node n = new Node();

        UnreachableCodeElimination.EliminationPass ep = new UnreachableCodeElimination(traversal.getCfg());

        ep.removeNode(n);
    }
}