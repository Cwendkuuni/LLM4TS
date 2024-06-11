import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.AllowedSolution;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    private static final double EPSILON = 1e-6;

    @Test
    public void testSolve_WhenRootExists_ReturnsRoot() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x - 5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double result = solver.solve(100, f, 0, 10, AllowedSolution.ANY_SIDE);

        assertEquals(5.0, result, EPSILON);
    }

    @Test
    public void testSolve_WhenNoRootExists_ThrowsException() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x + 5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLLINOIS);
        
        try {
            double result = solver.solve(100, f, 0, 10, AllowedSolution.ANY_SIDE);
        }
        catch (RuntimeException ex) {
            assertEquals("No bracketing range found.", ex.getMessage());
        }
    }

    @Test
    public void testSolve_WhenRootExists_ReturnsRightBound() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x - 5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        double result = solver.solve(100, f, 0, 10, AllowedSolution.RIGHT_SIDE);

        assertEquals(5.0, result, EPSILON);
    }

    @Test
    public void testSolve_WhenFunctionValueTooSmall_ReturnsApproximation() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x - 1e-10;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, 0, 10, AllowedSolution.ANY_SIDE);

        assertEquals(0.0, result, EPSILON);
    }

    @Test
    public void testSolve_WhenIntervalWithinGivenAccuracies_ReturnsApproximation() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x - 1e-8;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        double result = solver.solve(100, f, 0, 10, AllowedSolution.ANY_SIDE);

        assertEquals(0.0, result, EPSILON);
    }

}