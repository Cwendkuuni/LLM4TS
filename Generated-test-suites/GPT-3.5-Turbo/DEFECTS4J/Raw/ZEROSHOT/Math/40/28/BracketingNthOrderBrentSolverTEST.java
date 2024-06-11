import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.exception.NoBracketingException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.util.Precision;

import static org.junit.Assert.assertEquals;

public class BracketingNthOrderBrentSolverTest {

    private BracketingNthOrderBrentSolver solver;

    @Before
    public void setUp() {
        solver = new BracketingNthOrderBrentSolver();
    }

    @Test
    public void testSolve_ExactRoot() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x - 5;
            }
        };

        double solution = solver.solve(100, f, 4, 6);
        assertEquals(5.0, solution, 1e-6);
    }

    @Test
    public void testSolve_ApproximateRoot() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        double solution = solver.solve(100, f, 0, 5);
        assertEquals(2.0, solution, 1e-6);
    }

    @Test
    public void testSolve_NoRoot() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        double solution = solver.solve(100, f, 0, 2);
        assertEquals(Double.NaN, solution, 1e-6);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolve_NoBracketing() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x - 5;
            }
        };

        double solution = solver.solve(100, f, 0, 2);
    }
}