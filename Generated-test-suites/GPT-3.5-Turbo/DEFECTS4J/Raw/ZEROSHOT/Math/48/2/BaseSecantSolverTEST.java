import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
        
        // Test case 1: function value is 0
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return 0;
            }
        };
        double x1 = solver.solve(100, f1, 0, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0.0, x1, absoluteAccuracy);
        
        // Test case 2: maximum iterations exceeded
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        };
        boolean exceptionThrown = false;
        try {
            double x2 = solver.solve(0, f2, 0, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        } catch(ConvergenceException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        
        // Test case 3: final approximation is within given accuracy
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        };
        double x3 = solver.solve(100, f3, 0, 2, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(1.0, x3, absoluteAccuracy);
    }
}