import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void testSolve() {
        // Test with a simple linear function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 2 * x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);
        double root = solver.solve(100, f, 0, 10);
        Assert.assertEquals(2.0, root, EPSILON);

        root = solver.solve(100, f, -10, 0);
        Assert.assertEquals(2.0, root, EPSILON);
    }

    @Test
    public void testSolveWithMultipleRoots() {
        // Test with a quadratic function with multiple roots
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return (x - 2) * (x + 2);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);
        double root = solver.solve(100, f, -10, 10);
        Assert.assertEquals(-2.0, root, EPSILON);

        root = solve.solve(100, f, -10, -1);
        Assert.assertEquals(-2.0, root, EPSILON);

        root = solve.solve(100, f, 1, 10);
        Assert.assertEquals(2.0, root, EPSILON);
    }

    @Test(expected=ConvergenceException.class)
    public void testSolveWithNoRoot() {
        // Test with a function that has no roots
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x + 1;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);
        solver.solve(100, f, -10, 10);
    }

    @Test
    public void testSolveWithTolerances() {
        // Test with a function that has a root within the given tolerances
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return (x - 2.5) * (x - 2.5);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-2, 1e-4, 1e-6, Method.REGULA_FALSI);
        double root = solver.solve(100, f, 1, 4);
        Assert.assertEquals(2.5, root, EPSILON);
    }

    @Test(expected=MathInternalError.class)
    public void testSolveWithInvalidMethod() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, null);
        solver.solve(100, f, -10, 10);
    }
}