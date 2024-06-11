import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateSolver;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.BracketedRootFinder;
import org.apache.commons.math3.analysis.solvers.LaguerreSolver;
import org.apache.commons.math3.analysis.solvers.SecantSolver;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve_simpleFunction() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        double result = UnivariateRealSolverUtils.solve(function, -3.0, 3.0);
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testSolve_functionWithRoots() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        double result = UnivariateRealSolverUtils.solve(function, -10.0, 10.0);
        assertEquals(-1.0, result, 1e-6);
        result = UnivariateRealSolverUtils.solve(function, 2.0, 4.0);
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testSolve_functionWithInflectionPoint() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x * Math.pow(x, 2) + x;
            }
        };
        double result = UnivariateRealSolverUtils.solve(function, -5.0, 5.0);
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testSolve_functionWithMultipleRoots() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
        BracketedRootFinder rootFinder = new BracketedRootFinder();
        rootFinder.setMaxIterations(100);
        UnivariateRealSolver solver = new BrentSolver();
        solver.setMaxIterations(100);
        double[] roots = UnivariateRealSolverUtils.bracket(function, -5.0, 5.0);
        double result = UnivariateRealSolverUtils.solve(function, roots[0], roots[1], 1e-6);
        assertEquals(-2.0, result, 1e-6);
        result = UnivariateRealSolverUtils.solve(function, roots[0], roots[1], 1e-6);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolve_functionWithNegativeRoot() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x + 1.0;
            }
        };
        double result = UnivariateRealSolverUtils.solve(function, -5.0, 0.0);
        assertEquals(-1.0, result, 1e-6);
    }

    @Test
    public void testSolve_functionWithNoRoots() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + 1.0;
            }
        };
        assertThrows(NoBracketException.class,
                () -> UnivariateRealSolverUtils.solve(function, -5.0, 5.0));
    }

    @Test
    public void testSolve_functionWithExceptionOnEvaluation() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws Exception {
                if (x > 0.0) {
                    throw new Exception("Function evaluation failed");
                }
                return Math.sin(x);
            }
        };
        assertThrows(FunctionEvaluationException.class,
                () -> UnivariateRealSolverUtils.solve(function, -3.0, 3.0));
    }

    @Test
    public void testBracket_simpleFunction() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        double[] roots = UnivariateRealSolverUtils.bracket(function, -3.0, 3.0);
        assertEquals(0.0, roots[0], 1e-6);
        assertEquals(0.0, roots[1], 1e-6);
    }

    @Test
    public void testBracket_functionWithRoots() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        double[] roots = UnivariateRealSolverUtils.bracket(function, -10.0, 10.0);
        assertEquals(-1.0, roots[0], 1e-6);
        assertEquals(1.0, roots[1], 1e-6);
    }

    @Test
    public void testBracket_functionWithInflectionPoint() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x * Math.pow(x, 2) + x;
            }
        };
        double[] roots = UnivariateRealSolverUtils.bracket(function, -5.0, 5.0);
        assertEquals(1.0, roots[0], 1e-6);
        assertEquals(3.0, roots[1], 1e-6);
    }

    @Test
    public void testBracket_functionWithMultipleRoots() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
        double[] roots = UnivariateRealSolverUtils.bracket(function, -5.0, 5.0, 100);
        double[] expectedRoots = {-2.0, 2.0};
        for (int i = 0; i < 2; i++) {
            assertEquals(expectedRoots[i], roots[i], 1e-6);
        }
    }

    @Test
    public void testBracket_functionWithNegativeRoot() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x + 1.0;
            }
        };
        double[] roots = UnivariateRealSolverUtils.bracket(function, -5.0, 0.0);
        assertEquals(-1.0, roots[0], 1e-6);
        assertEquals(-1.0, roots[1], 1e-6);
    }

    @Test
    public void testBracket_functionWithNoRoots() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + 1.0;
            }
        };
        assertThrows(NoBracketException.class,
                () -> UnivariateRealSolverUtils.bracket(function, -5.0, 5.0));
    }

    @Test
    public void testBracket_functionWithExceptionOnEvaluation() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws Exception {
                if (x > 0.0) {
                    throw new Exception("Function evaluation failed");
                }
                return Math.sin(x);
            }
        };
        assertThrows(NullArgumentException.class,
                () -> UnivariateRealSolverUtils.bracket(function, -3.0, 3.0));
    }

    @Test
    public void testMidpoint() throws Exception {
        assertEquals(2.5, UnivariateRealSolverUtils.midpoint(1.0, 4.0), 1e-6);
    }

    @Test
    public void testSetup() throws Exception {
        UnivariateRealFunction function = null;
        assertThrows(NullArgumentException.class,
                () -> UnivariateRealSolverUtils.setup(function));
    }
}