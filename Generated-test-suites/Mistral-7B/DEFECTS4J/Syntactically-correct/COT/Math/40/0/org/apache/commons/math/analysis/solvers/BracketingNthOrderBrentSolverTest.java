import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testDefaultConstructor() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testConstructorWithAbsoluteAccuracyAndMaximalOrder() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 3);
        assertEquals(1e-5, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(3, solver.getMaximalOrder());
    }

    @Test
    public void testConstructorWithRelativeAccuracyAndAbsoluteAccuracyAndMaximalOrder() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-4, 1e-5, 3);
        assertEquals(1e-4, solver.getRelativeAccuracy(), 0.0);
        assertEquals(1e-5, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(3, solver.getMaximalOrder());
    }

    @Test
    public void testConstructorWithRelativeAccuracyAndAbsoluteAccuracyAndFunctionValueAccuracyAndMaximalOrder() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-4, 1e-5, 1e-6, 3);
        assertEquals(1e-4, solver.getRelativeAccuracy(), 0.0);
        assertEquals(1e-5, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(1e-6, solver.getFunctionValueAccuracy(), 0.0);
        assertEquals(3, solver.getMaximalOrder());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithMaximalOrderLessThan2() throws NumberIsTooSmallException {
        new BracketingNthOrderBrentSolver(1e-5, 1);
    }

    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testSolveWithDefaultFunctionAndInterval() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = f -> Math.pow(Math.sin(x), 3) - 0.5;
        double result = solver.solve(50, function, -1, 1);
        assertEquals(0, result, 1e-6);
    }

    @Test
    public void testSolveWithStartValueAndInterval() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = f -> Math.pow(Math.sin(x), 3) - 0.5;
        double result = solver.solve(50, function, -1, 1, 0.5);
        assertEquals(0, result, 1e-6);
    }

    @Test
    public void testSolveWithFunctionAndIntervalAndAllowedSolutionLeftSide() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.setAllowedSolution(AllowedSolution.LEFT_SIDE);
        UnivariateFunction function = f -> Math.pow(Math.sin(x), 3) - 0.5;
        double result = solver.solve(50, function, -1, 1);
        assertEquals(-1, result, 1e-6);
    }

    @Test
    public void testSolveWithFunctionAndIntervalAndAllowedSolutionRightSide() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.setAllowedSolution(AllowedSolution.RIGHT_SIDE);
        UnivariateFunction function = f -> Math.pow(Math.sin(x), 3) - 0.5;
        double result = solver.solve(50, function, -1, 1);
        assertEquals(1, result, 1e-6);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolveWithoutBracketing() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = f -> Math.sin(x) - 0.5;
        solver.solve(50, function, 0, 0);
    }

    @Test(expected = MathInternalError.class)
    public void testSolveWithInvalidAllowedSolution() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = f -> Math.pow(Math.sin(x), 3) - 0.5;
        solver.setAllowedSolution(123);
        solver.solve(50, function, -1, 1);
    }

}