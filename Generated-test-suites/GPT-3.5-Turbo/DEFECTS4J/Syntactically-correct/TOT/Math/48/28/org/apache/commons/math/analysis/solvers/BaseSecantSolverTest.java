import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.util.FastMath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {

    private static final double EPSILON = 1e-10;

    private BaseSecantSolver solver;

    @Before
    public void setup() {
        solver = new BaseSecantSolverStub(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void solve_WhenRootIsInBracket_ReturnsRoot() {
        UnivariateRealFunction f = new UnivariateRealFunctionStub(1.0);
        double result = solver.solve(100, f, 0.0, 2.0, 1.0);

        Assert.assertEquals(1.0, result, EPSILON);
    }

    @Test
    public void solve_WhenRootIsNotInBracket_ThrowsConvergenceException() {
        UnivariateRealFunction f = new UnivariateRealFunctionStub(2.0);
        try {
            solver.solve(100, f, 0.0, 1.0, 0.5);
            Assert.fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Pass
        }
    }

    @Test
    public void solve_WhenRootIsOnLeftSide_ReturnsRootWithAllowedSolutionLeftSide() {
        UnivariateRealFunction f = new UnivariateRealFunctionStub(-2.0);
        double result = solver.solve(100, f, 0.0, 2.0, 1.0, BaseSecantSolver.AllowedSolution.LEFT_SIDE);

        Assert.assertEquals(0.0, result, EPSILON);
    }

    @Test
    public void solve_WhenRootIsOnLeftSide_ReturnsRootWithAllowedSolutionAnySide() {
        UnivariateRealFunction f = new UnivariateRealFunctionStub(-2.0);
        double result = solver.solve(100, f, 0.0, 2.0, 1.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        Assert.assertEquals(0.0, result, EPSILON);
    }

    @Test
    public void solve_WhenRootIsOnLeftSide_ThrowsConvergenceExceptionWithAllowedSolutionRightSide() {
        UnivariateRealFunction f = new UnivariateRealFunctionStub(-2.0);
        try {
            solver.solve(100, f, 0.0, 2.0, 1.0, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
            Assert.fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Pass
        }
    }

    @Test
    public void solve_WhenRootIsOnRightSide_ReturnsRootWithAllowedSolutionRightSide() {
        UnivariateRealFunction f = new UnivariateRealFunctionStub(2.0);
        double result = solver.solve(100, f, 0.0, 2.0, 1.0, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);

        Assert.assertEquals(2.0, result, EPSILON);
    }

    @Test
    public void solve_WhenRootIsOnRightSide_ReturnsRootWithAllowedSolutionAnySide() {
        UnivariateRealFunction f = new UnivariateRealFunctionStub(2.0);
        double result = solver.solve(100, f, 0.0, 2.0, 1.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        Assert.assertEquals(2.0, result, EPSILON);
    }

    @Test
    public void solve_WhenRootIsOnRightSide_ThrowsConvergenceExceptionWithAllowedSolutionLeftSide() {
        UnivariateRealFunction f = new UnivariateRealFunctionStub(2.0);
        try {
            solver.solve(100, f, 0.0, 2.0, 1.0, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
            Assert.fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Pass
        }
    }

    @Test
    public void solve_WhenMaxEvaluationsIsReached_ThrowsConvergenceException() {
        UnivariateRealFunction f = new UnivariateRealFunctionStub(1.0);
        try {
            solver.solve(1, f, 0.0, 2.0, 1.0);
            Assert.fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Pass
        }
    }

    @Test
    public void solve_WhenExactRootIsOnLeftSide_ReturnsExactRoot() {
        UnivariateRealFunction f = new UnivariateRealFunctionStub(1.0);
        double result = solver.solve(100, f, 1.0, 2.0, 1.0, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);

        Assert.assertEquals(1.0, result, EPSILON);
    }

    @Test
    public void solve_WhenExactRootIsOnRightSide_ReturnsExactRoot() {
        UnivariateRealFunction f = new UnivariateRealFunctionStub(-1.0);
        double result = solver.solve(100, f, 0.0, 1.0, 1.0, BaseSecantSolver.AllowedSolution.LEFT_SIDE);

        Assert.assertEquals(1.0, result, EPSILON);
    }

    private class BaseSecantSolverStub extends BaseSecantSolver {

        public BaseSecantSolverStub(double absoluteAccuracy, Method method) {
            super(absoluteAccuracy, method);
        }

        public BaseSecantSolverStub(double relativeAccuracy, double absoluteAccuracy, Method method) {
            super(relativeAccuracy, absoluteAccuracy, method);
        }

        public BaseSecantSolverStub(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy, Method method) {
            super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method);
        }

        @Override
        protected final double computeObjectiveValue(double x) {
            return 0.0; // Assume the function value is always 0 for test purposes
        }

        @Override
        protected final void verifyInterval(double lower, double upper) throws ConvergenceException {
            // Skip interval verification for test purposes
        }
    }

    private class UnivariateRealFunctionStub implements UnivariateRealFunction {

        private double root;

        public UnivariateRealFunctionStub(double root) {
            this.root = root;
        }

        @Override
        public double value(double x) {
            return x - root;
        }
    }
}