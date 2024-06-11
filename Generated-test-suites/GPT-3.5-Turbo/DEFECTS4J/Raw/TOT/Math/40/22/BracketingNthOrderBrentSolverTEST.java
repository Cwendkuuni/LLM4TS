import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    private BracketingNthOrderBrentSolver solver;

    @Before
    public void setUp() throws Exception {
        solver = new BracketingNthOrderBrentSolver();
    }

    @Test
    public void testSolve_withMaxEval_and_Function() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(100, function, -10, 10);
        assertEquals(2.0, result, 0.0001);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolve_withMaxEval_and_Function_and_MinimalOrder() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        solver.solve(100, function, -10, 10, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testSolve_withMaxEval_and_Function_and_MinimalOrder() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(100, function, -10, 10, 2.5, AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 0.0001);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_withDefaultValues_and_MinimalOrder() {
        new BracketingNthOrderBrentSolver(2, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_withNegativeAbsoluteAccuracy() {
        new BracketingNthOrderBrentSolver(-1e-6, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_withNegativeRelativeAccuracy() {
        new BracketingNthOrderBrentSolver(1e-6, -5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_withNegativeFunctionValueAccuracy() {
        new BracketingNthOrderBrentSolver(1e-6, 1e-6, -5);
    }

    @Test
    public void testGetMaximalOrder() {
        int result = solver.getMaximalOrder();
        assertEquals(5, result);
    }

}