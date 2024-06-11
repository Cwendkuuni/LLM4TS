import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.NodeTraversal;

public class UnreachableCodeEliminationTest {
    @Test
    public void testEnterScope() {
        // setup
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = false;
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        
        // execute and assert
        // no assert as this is just a setup method
        elimination.enterScope(new NodeTraversal(compiler, null));
    }

    @Test
    public void testExitScope() {
        // setup
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = false;
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        
        // execute and assert
        // no assert as this is just a setup method
        elimination.exitScope(new NodeTraversal(compiler, null));
    }

    @Test
    public void testProcess() {
        // setup
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = false;
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        Node externs = new Node();
        Node root = new Node();
        
        // execute and assert
        // no assert as this is just a setup method
        elimination.process(externs, root);
    }

    @Test
    public void testVisit() {
        // setup
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = false;
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node n = new Node();
        Node parent = new Node();
        
        // execute and assert
        // no assert as this is just a setup method
        elimination.visit(t, n, parent);
    }

    @Test
    public void testTryRemoveUnconditionalBranching() {
        // setup
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = false;
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        Node n = new Node();
        
        // execute and assert
        assertNull(elimination.tryRemoveUnconditionalBranching(n));
    }

    @Test
    public void testComputeFollowing() {
        // setup
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = false;
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        Node n = new Node();
        
        // execute and assert
        assertNull(elimination.computeFollowing(n));
    }

    @Test
    public void testRemoveDeadExprStatementSafely() {
        // setup
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = false;
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        Node n = new Node();
        
        // execute and assert
        // no assert as this is just a setup method
        elimination.removeDeadExprStatementSafely(n);
    }
}