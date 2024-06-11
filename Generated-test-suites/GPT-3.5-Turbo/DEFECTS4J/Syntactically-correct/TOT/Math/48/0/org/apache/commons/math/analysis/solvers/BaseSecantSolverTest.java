import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    private static final double EPSILON = 1e-6;

    @Test
    public void testSolve_WhenRootIsBetweenBounds_ReturnsRoot() {
        BaseSecantSolver solver = new BaseSecantSolverTestImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Test function: f(x) = x^2 - 1, which has roots at -1 and 1
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 1;
            }
        };

        double root = solver.solve(100, f, -2, 2);
        Assert.assertTrue(FastMath.abs(root - 1) < EPSILON);
    }

    @Test
    public void testSolve_WhenRootIsBetweenBounds_ReturnsRoot2() {
        BaseSecantSolver solver = new BaseSecantSolverTestImpl(1e-6, BaseSecantSolver.Method.ILLINOIS);

        // Test function: f(x) = x^2 - 1, which has roots at -1 and 1
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 1;
            }
        };

        double root = solver.solve(100, f, -2, 2);
        Assert.assertTrue(FastMath.abs(root - 1) < EPSILON);
    }

    @Test
    public void testSolve_WhenRootIsBetweenBounds_ReturnsRoot3() {
        BaseSecantSolver solver = new BaseSecantSolverTestImpl(1e-6, BaseSecantSolver.Method.PEGASUS);

        // Test function: f(x) = x^2 - 1, which has roots at -1 and 1
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 1;
            }
        };

        double root = solver.solve(100, f, -2, 2);
        Assert.assertTrue(FastMath.abs(root - 1) < EPSILON);
    }

    @Test
    public void testSolve_WhenRootIsLowerBound_ReturnsLowerBound() {
        BaseSecantSolver solver = new BaseSecantSolverTestImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Test function: f(x) = x^2 - 1, which has roots at -1 and 1
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 1;
            }
        };

        double root = solver.solve(100, f, -2, -1);
        Assert.assertTrue(FastMath.abs(root - (-1)) < EPSILON);
    }

    @Test
    public void testSolve_WhenRootIsUpperBound_ReturnsUpperBound() {
        BaseSecantSolver solver = new BaseSecantSolverTestImpl(1e-6, BaseSecantSolver.Method.ILLINOIS);

        // Test function: f(x) = x^2 - 1, which has roots at -1 and 1
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 1;
            }
        };

        double root = solver.solve(100, f, 1, 2);
        Assert.assertTrue(FastMath.abs(root - 1) < EPSILON);
    }

    @Test
    public void testSolve_WhenRootIsNotBetweenBounds_ThrowsConvergenceException() {
        BaseSecantSolver solver = new BaseSecantSolverTestImpl(1e-6, BaseSecantSolver.Method.PEGASUS);

        // Test function: f(x) = x^2 - 1, which has roots at -1 and 1
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 1;
            }
        };

        try {
            double root = solver.solve(100, f, -2, -1);
            Assert.fail("Expected ConvergenceException");
        } catch (ConvergenceException ex) {
            // Expected exception
        }
    }

    private class BaseSecantSolverTestImpl extends BaseSecantSolver {
        public BaseSecantSolverTestImpl(double absoluteAccuracy, Method method) {
            super(absoluteAccuracy, method);
        }
        protected double computeObjectiveValue(double x) {
            // Implementation of the computeObjectiveValue method is not needed for this test
            return 0;
        }
    }

}