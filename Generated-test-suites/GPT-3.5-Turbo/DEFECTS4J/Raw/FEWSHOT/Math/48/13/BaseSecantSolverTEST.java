import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WhenExactRoot_ThenReturnRoot() {
        BaseSecantSolver solver = new BaseSecantSolverTestImpl();
        double result = solver.solve(100, new UnivariateRealFunctionTestImpl(), 1.0, 2.0, 1.5, AllowedSolution.ANY_SIDE);
        assertEquals(1.5, result, 0.0001);
    }

    @Test
    public void testSolve_WhenFunctionValueTooSmall_ThenReturnRoot() {
        BaseSecantSolver solver = new BaseSecantSolverTestImpl();
        double result = solver.solve(100, new UnivariateRealFunctionTestImpl(), 1.0, 2.0, 1.5, AllowedSolution.ANY_SIDE);
        assertEquals(1.5, result, 0.0001);
    }

    @Test
    public void testSolve_WhenCurrentIntervalWithinAccuracies_ThenReturnRoot() {
        BaseSecantSolver solver = new BaseSecantSolverTestImpl();
        double result = solver.solve(100, new UnivariateRealFunctionTestImpl(), 1.0, 2.0, 1.5, AllowedSolution.ANY_SIDE);
        assertEquals(1.5, result, 0.0001);
    }

    @Test
    public void testSolve_WhenFunctionValuePositive_ThenReturnRoot() {
        BaseSecantSolver solver = new BaseSecantSolverTestImpl();
        double result = solver.solve(100, new UnivariateRealFunctionTestImpl(), 1.0, 2.0, 1.5, AllowedSolution.ABOVE_SIDE);
        assertEquals(1.5, result, 0.0001);
    }

    private class BaseSecantSolverTestImpl extends BaseSecantSolver {
        public BaseSecantSolverTestImpl() {
            super(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        }

        @Override
        protected double computeObjectiveValue(double x) {
            if (x == 1.5) {
                return 0.0;
            }
            return x - 1.5;
        }

        @Override
        protected void verifyBracketing(double lower, double upper) throws ConvergenceException {
            if (lower >= upper) {
                throw new ConvergenceException();
            }
        }
    }

    private class UnivariateRealFunctionTestImpl implements UnivariateRealFunction {
        public double value(double x) {
            return x - 1.5;
        }
    }

}