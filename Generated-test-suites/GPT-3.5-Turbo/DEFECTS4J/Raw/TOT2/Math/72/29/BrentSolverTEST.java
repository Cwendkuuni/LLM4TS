import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

    @Test
    public void testFindRootTypicalCase() {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction();
        ConvergenceChecker checker = new ConvergenceChecker();
        double x0 = 1.0;
        double f0 = function.valueAt(x0);
        double x1 = 2.0;
        double f1 = function.valueAt(x1);
        int maxIter = 100;
        
        assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
        assertEquals(expectedRoot, solver.getRoot(), 0.0001);
    }

    @Test
    public void testFindRootEdgeCases() {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction();
        ConvergenceChecker checker = new ConvergenceChecker();
        double x0 = 0.0;
        double f0 = function.valueAt(x0);
        double x1 = 5.0;
        double f1 = function.valueAt(x1);
        int maxIter = 100;
        
        assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
        assertEquals(expectedRoot, solver.getRoot(), 0.0001);
    }

    @Test
    public void testFindRootErrorScenarios() {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction();
        ConvergenceChecker checker = new ConvergenceChecker();
        double x0 = 1.0;
        double f0 = function.valueAt(x0);
        double x1 = 2.0;
        double f1 = function.valueAt(x1);
        int maxIter = 5; // Setting a low maxIter for error scenario
        
        assertFalse(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    }

    // Add more test cases for other methods if needed
    
}