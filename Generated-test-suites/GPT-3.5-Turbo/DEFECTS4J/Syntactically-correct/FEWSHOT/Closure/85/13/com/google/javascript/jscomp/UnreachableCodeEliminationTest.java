import static org.junit.Assert.*;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
    
    @Test
    public void testEnterScope() {
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, true);
        uce.enterScope(t);
        
        // test code here
    }
    
    @Test
    public void testExitScope() {
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, true);
        uce.exitScope(t);
        
        // test code here
    }
    
    @Test
    public void testProcess() {
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, true);
        uce.process(externs, root);
        
        // test code here
    }
    
    @Test
    public void testVisit() {
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, true);
        uce.visit(t, n, parent);
        
        // test code here
    }
    
    @Test
    public void testTryRemoveUnconditionalBranching() {
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, true);
        Node result = uce.tryRemoveUnconditionalBranching(n);
        
        // test code here
    }
    
    @Test
    public void testComputeFollowing() {
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, true);
        Node result = uce.computeFollowing(n);
        
        // test code here
    }
    
    @Test
    public void testRemoveDeadExprStatementSafely() {
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, true);
        uce.removeDeadExprStatementSafely(n);
        
        // test code here
    }
}