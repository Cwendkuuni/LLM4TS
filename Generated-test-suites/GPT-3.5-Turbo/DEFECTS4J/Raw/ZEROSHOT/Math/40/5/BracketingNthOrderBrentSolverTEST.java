import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.exception.NoBracketingException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.util.Precision;

import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    private BracketingNthOrderBrentSolver solver;

    @Before
    public void setUp() {
        solver = new BracketingNthOrderBrentSolver();
    }

    @After
    public void tearDown() {
        solver = null;
    }

    @Test
    public void testDoSolve() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * (x - 2);
            }
        };

        double result = solver.solve(100, function, 0, 3, BracketingNthOrderBrentSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 0.0001);
    }

    @Test(expected = NoBracketingException.class)
    public void testDoSolveNoBracketingInterval() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * (x - 2);
            }
        };

        solver.solve(100, function, 0, 1, BracketingNthOrderBrentSolver.AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MathInternalError.class)
    public void testDoSolveInternalError() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x;
            }
        };

        solver.solve(100, function, 0, 1);
    }

    @Test
    public void testSolveWithMaxEvaluations() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x - 1;
            }
        };

        double result = solver.solve(1, function, 0, 3, BracketingNthOrderBrentSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, result, 0.0001);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorThrowsExceptionWhenMaximalOrderIsLowerThan2() {
        new BracketingNthOrderBrentSolver(1e-6, 1);
    }
}