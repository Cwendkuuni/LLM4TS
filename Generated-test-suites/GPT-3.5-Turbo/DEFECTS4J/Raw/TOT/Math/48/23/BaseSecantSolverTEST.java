import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.util.FastMath;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BaseSecantSolverTest {

    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1e-6;

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve() {

        // Typical use case
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result1 = solver.solve(100, f1, 1, 3);
        assertEquals(2, result1, DEFAULT_ABSOLUTE_ACCURACY);

        // Edge case
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        double result2 = solver.solve(100, f2, 0, 1);
        assertEquals(0, result2, DEFAULT_ABSOLUTE_ACCURACY);

        // Error scenario
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        try {
            double result3 = solver.solve(100, f3, 0, 1);
            fail("Expected ConvergenceException to be thrown");
        } catch (ConvergenceException e) {
            assertTrue(e.getMessage().contains("Solution not found after 100 iterations"));
        }

    }

}