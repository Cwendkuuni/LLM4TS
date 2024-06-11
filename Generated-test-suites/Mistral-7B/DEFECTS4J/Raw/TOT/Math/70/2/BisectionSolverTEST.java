import org.apache.commons.math3.analysis.BisectionSolver;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.PrecisionRequiredException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class BisectionSolverTest {

    /**
     * Test bisection solver for a simple function with known roots.
     */
    @Test
    public void testBisectionSolverSimpleFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        try {
            double root = solver.solve(f, -2.5, 2.5);
            assertEquals(0.0, root, 1e-8);
        } catch (MaxIterationsExceededException e) {
            // This should not happen for a simple function like this
            assertFalse(true);
        }
    }

    /**
     * Test bisection solver for a function with roots very close to the interval bounds.
     */
    @Test
    public void testBisectionSolverRootsCloseToBounds() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        try {
            double root = solver.solve(f, -1.57, 1.57);
            assertTrue(Math.abs(Math.PI - root) < 1e-6);
        } catch (MaxIterationsExceededException e) {
            // This function has roots very close to the interval bounds,
            // so the bisection solver might not converge in a reasonable number of iterations
            assertFalse(true);
        }
    }

    /**
     * Test bisection solver for a function with roots very far from the interval bounds.
     */
    @Test
    public void testBisectionSolverRootsFarFromBounds() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        try {
            double root = solver.solve(f, -5, 5);
            assertFalse(Math.abs(Math.exp(-5) - root) < 1e-6);
        } catch (MaxIterationsExceededException e) {
            // This function has roots very far from the interval bounds,
            // so the bisection solver might not converge in a reasonable number of iterations
            assertFalse(true);
        }
    }

    /**
     * Test bisection solver for a function that is not continuous.
     */
    @Test
    public void testBisectionSolverDiscontinuousFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return Math.sqrt(x);
                } else {
                    throw new IllegalArgumentException("x should be positive");
                }
            }
        };

        BisectionSolver solver = new BisectionSolver();
        assertThrows(IllegalArgumentException.class, () -> solver.solve(f, -1, 1));
    }

    /**
     * Test bisection solver for a function that does not have real roots.
     */
    @Test
    public void testBisectionSolverNoRealRoots() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + 1;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        assertThrows(FunctionEvaluationException.class, () -> solver.solve(f, -10, 10));
    }

    /**
     * Test bisection solver for a function that has complex roots.
     */
    @Test(expected = PrecisionRequiredException.class)
    public void testBisectionSolverComplexRoots() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + 1;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -2, 2);
    }
}