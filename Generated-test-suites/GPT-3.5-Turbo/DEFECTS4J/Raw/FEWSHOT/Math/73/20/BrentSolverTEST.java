import org.junit.Test;
import static org.junit.Assert.*;

import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {
    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        
        // Define a simple linear function f(x) = x + 1
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x + 1;
            }
        };
        
        // Test finding root in an interval [0, 5] where f(2) = 3
        assertTrue(solver.findRoot(function, new ConvergenceChecker(), 100, 0, 1, 5, 6));
        assertEquals(2.0, solver.getRoot(), 1e-10);
        
        // Test finding root in an interval [0, 5] where f(0.5) = 1.5
        assertTrue(solver.findRoot(function, new ConvergenceChecker(), 100, 0, 1, 5, 6));
        assertEquals(0.5, solver.getRoot(), 1e-10);
        
        // Test finding root in an interval [-2, 2] where f(-1) = 0
        assertTrue(solver.findRoot(function, new ConvergenceChecker(), 100, -2, -1, 2, 3));
        assertEquals(-1.0, solver.getRoot(), 1e-10);
        
        // Test finding root in an interval [-5, 0] where f(-3) = -2
        assertTrue(solver.findRoot(function, new ConvergenceChecker(), 100, -5, -4, 0, 1));
        assertEquals(-3.0, solver.getRoot(), 1e-10);
        
        // Test finding root in an interval [-5, 0] where f(-5) = -4
        assertTrue(solver.findRoot(function, new ConvergenceChecker(), 100, -5, -4, 0, 1));
        assertEquals(-5.0, solver.getRoot(), 1e-10);
        
        // Test finding root in an interval [-5, 0] where f(0) = 1
        assertFalse(solver.findRoot(function, new ConvergenceChecker(), 100, -5, -4, 0, 1));
    }
}