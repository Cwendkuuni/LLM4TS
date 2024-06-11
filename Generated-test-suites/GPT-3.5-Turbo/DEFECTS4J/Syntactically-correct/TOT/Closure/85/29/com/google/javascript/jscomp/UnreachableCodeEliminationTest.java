import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import com.google.javascript.rhino.Node;

public class UnreachableCodeEliminationTest {

    @Test
    public void testEnterScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);

        NodeTraversal t = new NodeTraversal(compiler, null);

        uce.enterScope(t);

        // Write test case here

        uce.exitScope(t);
    }

    @Test
    public void testExitScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);

        NodeTraversal t = new NodeTraversal(compiler, null);

        uce.enterScope(t);

        // Write test case here

        uce.exitScope(t);
    }

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);

        Node externs = null;
        Node root = null;

        uce.process(externs, root);

        // Write test case here

    }

    @Test
    public void testVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);

        NodeTraversal t = new NodeTraversal(compiler, null);
        Node n = null;
        Node parent = null;

        uce.enterScope(t);
        uce.visit(t, n, parent);
        uce.exitScope(t);

        // Write test case here

    }

    @Test
    public void testTryRemoveUnconditionalBranching() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);

        Node n = null;

        Node result = uce.tryRemoveUnconditionalBranching(n);

        // Write test case here

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testComputeFollowing() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);

        Node n = null;

        Node result = uce.computeFollowing(n);

        // Write test case here

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testRemoveDeadExprStatementSafely() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);

        Node n = null;

        uce.removeDeadExprStatementSafely(n);

        // Write test case here

    }
}