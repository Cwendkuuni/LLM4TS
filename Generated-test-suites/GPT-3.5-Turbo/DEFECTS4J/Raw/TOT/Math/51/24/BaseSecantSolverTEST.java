import org.apache.commons.math.analysis.QuinticFunction;
import org.apache.commons.math.exception.NoBracketingException;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    // Test cases for solve() method

    // Typical use case - solution is found within the given accuracies
    @Test
    public void testSolve_typicalUseCase() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        QuinticFunction function = new QuinticFunction();

        double result = solver.solve(100, function, -1, 2, -0.5);

        Assert.assertEquals(0.0, function.value(result), 1e-6);
    }

    // Edge case - solution is exactly the minimum bound
    @Test
    public void testSolve_solutionIsMinimumBound() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        QuinticFunction function = new QuinticFunction();

        double result = solver.solve(100, function, -1, 2, -1);

        Assert.assertEquals(0.0, function.value(result), 1e-6);
    }

    // Edge case - solution is exactly the maximum bound
    @Test
    public void testSolve_solutionIsMaximumBound() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        QuinticFunction function = new QuinticFunction();

        double result = solver.solve(100, function, -1, 2, 2);

        Assert.assertEquals(0.0, function.value(result), 1e-6);
    }

    // Error scenario - no bracketing interval found
    @Test(expected = NoBracketingException.class)
    public void testSolve_noBracketingIntervalFound() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        QuinticFunction function = new QuinticFunction();

        solver.solve(100, function, -1, -0.5, 1);
    }

    // Test cases for verifyBracketing() method

    // Typical use case - bracketing interval is valid
    @Test
    public void testVerifyBracketing_bracketingIntervalIsValid() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        solver.verifyBracketing(-1, 1);
    }

    // Error scenario - left bound is equal to right bound
    @Test(expected = NoBracketingException.class)
    public void testVerifyBracketing_leftBoundIsEqualToRightBound() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        solver.verifyBracketing(1, 1);
    }

    // Error scenario - left bound is greater than right bound
    @Test(expected = NoBracketingException.class)
    public void testVerifyBracketing_leftBoundIsGreaterThanRightBound() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        solver.verifyBracketing(2, 1);
    }

}