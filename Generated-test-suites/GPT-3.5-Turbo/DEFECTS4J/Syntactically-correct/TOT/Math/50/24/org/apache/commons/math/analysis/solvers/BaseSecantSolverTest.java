import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    }

    // Test computeObjectiveValue()
    @Test
    public void testComputeObjectiveValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x*x - 4;
            }
        };

        double result = solver.computeObjectiveValue(2.0);
        assertEquals(0.0, result, 1e-6);

        double result2 = solver.computeObjectiveValue(-2.0);
        assertEquals(0.0, result2, 1e-6);
    }

    // Test verifyBracketing() with valid bracketing
    @Test
    public void testVerifyBracketing_validBracketing() {
        try {
            solver.verifyBracketing(1.0, 2.0);
        } catch (IllegalArgumentException e) {
            assertFalse(true);
        }
    }

    // Test verifyBracketing() with invalid bracketing
    @Test(expected = IllegalArgumentException.class)
    public void testVerifyBracketing_invalidBracketing() {
        solver.verifyBracketing(2.0, 1.0);
    }

    // Test solve() with valid solution
    @Test
    public void testSolve_validSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x + 4;
            }
        };

        double result = solver.solve(100, f, -10, 10);
        assertEquals(-4.0, result, 1e-6);
    }

    // Test solve() with start value exactly equal to root
    @Test
    public void testSolve_startValueEqualsRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 4;
            }
        };

        double result = solver.solve(100, f, -10, 10, 4.0);
        assertEquals(4.0, result, 1e-6);
    }

    // Test solve() with function value smaller than function value accuracy
    @Test
    public void testSolve_smallFunctionValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 0.000001;
            }
        };

        double result = solver.solve(100, f, -10, 10);
        assertTrue(Math.abs(result) < 1e-6);
    }

    // Test solve() with interval smaller than accuracies
    @Test
    public void testSolve_smallInterval() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };

        double result = solver.solve(100, f, 1.0, 1.000001);
        assertEquals(1.0, result, 1e-6);
    }

    // Test solve() with unknown allowed solution
    @Test(expected = MathInternalError.class)
    public void testSolve_unknownAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 0.0;
            }
        };

        double result = solver.solve(100, f, -10, 10, BaseSecantSolver.AllowedSolution.UNKNOWN);
    }

    // Test solve() with valid allowed solution
    @Test
    public void testSolve_validAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 0.0;
            }
        };

        double result = solver.solve(100, f, -10, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(-10.0, result, 1e-6);
    }

}