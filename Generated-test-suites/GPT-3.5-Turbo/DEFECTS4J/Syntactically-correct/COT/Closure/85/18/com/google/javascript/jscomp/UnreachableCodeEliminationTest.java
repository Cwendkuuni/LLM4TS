
import org.junit.Test;
import static org.junit.Assert.*;

public class UnreachableCodeEliminationTest {
    
    @Test
    public void testEnterScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = false;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        
        // TODO: Generate test case
        
        // uce.enterScope();
        
        // TODO: Assert statements
        

    }

    @Test
    public void testExitScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = false;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        
        // TODO: Generate test case
        
        // uce.exitScope();
        
        // TODO: Assert statements
        
    }

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = false;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        
        // TODO: Generate test case
        
        // uce.process(externs, root);
        
        // TODO: Assert statements

    }

    @Test
    public void testVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = false;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        
        // TODO: Generate test case
        
        // uce.visit(t, n, parent);
        
        // TODO: Assert statements

    }

    @Test
    public void testTryRemoveUnconditionalBranching() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = false;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        
        // TODO: Generate test case
        
        // uce.tryRemoveUnconditionalBranching(n);
        
        // TODO: Assert statements

    }

    @Test
    public void testComputeFollowing() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = false;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        
        // TODO: Generate test case
        
        // uce.computeFollowing(n);
        
        // TODO: Assert statements

    }

    @Test
    public void testRemoveDeadExprStatementSafely() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = false;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        
        // TODO: Generate test case
        
        // uce.removeDeadExprStatementSafely(n);
        
        // TODO: Assert statements

    }

}
