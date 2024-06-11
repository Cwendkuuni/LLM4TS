import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testConstructWithParameters() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 10);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(10, solver.getMaximalOrder());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructWithSmallMaximalOrder() {
        new BracketingNthOrderBrentSolver(1e-8, 1);
    }

    @Test
    public void testSolveSimpleFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = f -> Math.pow(Math.E, f) - 2;
        double result = solver.solve(100, function, -2, 1);
        assertEquals(Math.log(2, Math.E), result, 1e-7);
    }

    @Test
    public void testSolveFunctionWithStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = f -> Math.pow(Math.E, f) - 2;
        double startValue = 0;
        double result = solver.solve(100, function, -2, 1, startValue);
        assertEquals(Math.log(2, Math.E), result, 1e-7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveFunctionWithNullFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = null;
        solver.solve(100, function, -2, 1);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolveFunctionOutOfBracketing() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = f -> Math.pow(Math.E, f) - 3;
        double result = solver.solve(100, function, -2, 1);
    }

    @Test
    public void testSolveFunctionWithAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 10, AllowedSolution.LEFT_SIDE);
        UnivariateFunction function = f -> Math.pow(Math.E, f) - 2;
        double result = solver.solve(100, function, -2, 1);
        assertEquals(Math.log(2, Math.E), result, 1e-7);
    }

    @Test
    public void testSolveFunctionWithAllowedSolutionBelow() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 10, AllowedSolution.BELOW_SIDE);
        UnivariateFunction function = f -> Math.pow(Math.E, f) - 2;
        double result = solver.solve(100, function, -2, 1);
        assertEquals(Math.log(2, Math.E), result, 1e-7);
    }

    @Test
    public void testSolveFunctionWithAllowedSolutionAbove() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 10, AllowedSolution.ABOVE_SIDE);
        UnivariateFunction function = f -> Math.pow(Math.E, f) - 2;
        double result = solver.solve(100, function, -2, 1);
        assertEquals(Math.log(2, Math.E), result, 1e-7);
    }

}