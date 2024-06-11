import static org.junit.Assert.assertEquals;

import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.exception.NoBracketingException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.junit.Before;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    private BracketingNthOrderBrentSolver solver;

    @Before
    public void setUp() {
        solver = new BracketingNthOrderBrentSolver();
    }

    @Test
    public void testSolve() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        double result = solver.solve(100, f, -10, 10);
        assertEquals(2.0, result, 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveWithLowMaximalOrder() {
        solver = new BracketingNthOrderBrentSolver(1e-6, 1);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        double result = solver.solve(100, f, -10, 10, AllowedSolution.ABOVE_SIDE);
        assertEquals(2.0, result, 0.0);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolveWithNoBracketingException() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        solver.solve(100, f, -1, 1);
    }
}