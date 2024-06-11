import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    /**
     * Test solver with a simple quadratic function.
     * @throws Exception if the solver fails.
     */
    @Test
    public void testQuadraticFunction() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2 * x + 1;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1.0e-6, 5);
        double root = solver.solve(100, f, -1.5, 1.5);
        assertEquals("Root not found", 0.0, root, 1.0e-6);
    }

    /**
     * Test solver with a simple cubic function.
     * @throws Exception if the solver fails.
     */
    @Test
    public void testCubicFunction() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x * x - 2 * x * x + x - 1;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1.0e-6, 5);
        double root = solver.solve(100, f, -1.5, 1.5);
        assertEquals("Root not found", 1.0 / 3.0, root, 1.0e-6);
    }

    /**
     * Test solver with a function having two roots.
     * @throws Exception if the solver fails.
     */
    @Test
    public void testFunctionWithTwoRoots() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - Math.cos(x);
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1.0e-6, 5);
        double root1 = solver.solve(100, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        double root2 = solver.solve(100, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        // The order of the roots is not guaranteed
        assertTrue("Roots not found or not ordered",
                Precision.abs(Math.PI, Math.abs(root1 - Math.PI)) < 1.0e-6 &&
                Precision.abs(Math.PI / 2.0, Math.abs(root2 - Math.PI / 2.0)) < 1.0e-6);
    }

    /**
     * Test solver with a function having no bracketing interval.
     * @throws Exception if the solver fails.
     */
    @Test(expected = NoBracketingException.class)
    public void testFunctionWithNoBracketing() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1.0e-6, 5);
        solver.solve(100, f, 0.0, 0.0);
    }

    /**
     * Test solver with a function having a negative bracketing interval.
     * @throws Exception if the solver fails.
     */
    @Test
    public void testFunctionWithNegativeBracketing() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1.0e-6, 5);
        try {
            solver.solve(100, f, -1.0, -2.0);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // expected
        }
    }

    /**
     * Test solver with a function having a very small bracketing interval.
     * @throws Exception if the solver fails.
     */
    @Test
    public void testFunctionWithVerySmallBracketing() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-10000.0 * x * x) - Math.exp(-10000.0);
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1.0e-15, 5);
        double root = solver.solve(100, f, -0.001, 0.001);
        assertTrue("Root not found or invalid", Precision.abs(Math.E, Math.abs(root - Math.E)) < 1.0e-12);
    }

    /**
     * Test solver with a very ill-conditioned function.
     * @throws Exception if the solver fails.
     */
    @Test
    public void testIllConditionedFunction() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 1.0 / (1.0 + Math.pow(10.0, 10.0 * x));
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1.0e-12, 5);
        double root = solver.solve(100, f, -0.1, 0.1);
        assertTrue("Root not found or invalid", Precision.abs(0.5, Math.abs(root - 0.5)) < 1.0e-9);
    }

    /**
     * Test solver with a function having a singularity.
     * @throws Exception if the solver fails.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testFunctionWithSingularity() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x == 0) {
                    throw new MathIllegalArgumentException("Function has a singularity at x = 0");
                }
                return 1.0 / x;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1.0e-6, 5);
        solver.solve(100, f, -1.0, 1.0);
    }

}