import org.junit.Test;
import static org.junit.Assert.*;

public class UnreachableCodeEliminationTest {

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);

        Node externs = new Node();
        Node toplevel = new Node();
        uce.process(externs, toplevel);
    }

    @Test
    public void testVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);

        NodeTraversal t = new NodeTraversal(compiler, null);
        Node n = new Node();
        Node parent = new Node();
        uce.new EliminationPass(null).visit(t, n, parent);
    }

    @Test
    public void testTryRemoveUnconditionalBranching() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);

        Node n = new Node();

        uce.new EliminationPass(null).tryRemoveUnconditionalBranching(n);
    }

    @Test
    public void testComputeFollowing() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);

        Node n = new Node();

        uce.new EliminationPass(null).computeFollowing(n);
    }

    @Test
    public void testRemoveDeadExprStatementSafely() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);

        Node n = new Node();

        uce.new EliminationPass(null).removeDeadExprStatementSafely(n);
    }

    @Test
    public void testRemoveNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);

        Node n = new Node();

        uce.new EliminationPass(null).removeNode(n);
    }
}