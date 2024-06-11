import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        // Creating test values
        double x0 = 0.0;
        double f0 = -1.0;
        double x1 = 1.0;
        double f1 = 1.0;
        
        // Creating instance of BrentSolver
        BrentSolver solver = new BrentSolver();
        
        // Testing the findRoot method
        assertTrue(solver.findRoot(x -> x * x - 9, 100, x0, f0, x1, f1));
        assertEquals(3.0, solver.getRoot(), 1e-10);
    }

    @Test
    public void testFindRootWithZeroIterations() {
        // Creating test values
        double x0 = 0.0;
        double f0 = -1.0;
        double x1 = 1.0;
        double f1 = 1.0;
        
        // Creating instance of BrentSolver
        BrentSolver solver = new BrentSolver();
        
        // Testing the findRoot method with zero iterations
        assertFalse(solver.findRoot(x -> x * x - 9, 0, x0, f0, x1, f1));
    }

    @Test
    public void testFindRootWithInvalidFunction() {
        // Creating test values
        double x0 = 0.0;
        double f0 = -1.0;
        double x1 = 1.0;
        double f1 = 1.0;
        
        // Creating instance of BrentSolver
        BrentSolver solver = new BrentSolver();
        
        // Testing the findRoot method with an invalid function
        assertThrows(FunctionException.class, () -> solver.findRoot(null, 100, x0, f0, x1, f1));
    }

}