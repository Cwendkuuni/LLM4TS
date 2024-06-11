import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.PrecisionModifiedException;
import org.apache.commons.math3.exception.ZeroException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    @Test
    public void testSolveSimpleFunction() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, -2.0, 2.0);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testSolveFunctionWithNegativeRoot() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1.0;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, -1.0, 1.0);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    @Test
    public void testSolveFunctionWithTwoRoots() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, -3.0, 3.0);
            fail("Expected MaxIterationsExceededException or PrecisionModifiedException");
        } catch (MaxIterationsExceededException | PrecisionModifiedException e) {
            // Expected
        }
    }

    @Test
    public void testSolveFunctionWithRootAtEnd() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1.0;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, 1.0, 2.0);
        assertEquals(-1.0, root, 1e-6);
    }

    @Test
    public void testSolveFunctionWithTwoCloseRoots() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 0.0001;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, -1.5, 1.5);
        assertTrue(Math.abs(root) < 0.001);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveFunctionWithZeroArgument() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, 0.0, 0.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionWithException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws Exception {
                if (x < 0) {
                    throw new FunctionEvaluationException("Function is not defined for x < 0");
                }
                return x;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, -1.0, 1.0);
    }
}