import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.util.FastMath;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private class DummyFunction implements UnivariateRealFunction {

        public double value(double x) {
            return x;
        }
    }

    private BaseSecantSolver.DummySecantSolver solver;

    private static class DummySecantSolver extends BaseSecantSolver {

        protected DummySecantSolver(double absoluteAccuracy, Method method) {
            super(absoluteAccuracy, method);
        }

        protected double doSolve() {
            return 0;
        }

        public double solve(int maxEval, UnivariateRealFunction f, double min,
                            double max, double startValue) {
            return 0;
        }
    }

    @Before
    public void setUp() {
        solver = new BaseSecantSolverTest.DummySecantSolver(1e-10,
                BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve() {
        int maxEval = 100;
        UnivariateRealFunction f = new BaseSecantSolverTest.DummyFunction();
        double min = -10;
        double max = 10;
        double startValue = 5;
        BracketedUnivariateRealSolver.AllowedSolution allowedSolution =
                BracketedUnivariateRealSolver.AllowedSolution.ANY_SIDE;

        double result = solver.solve(maxEval, f, min, max, startValue,
                allowedSolution);

        assertEquals(0, result, 0);
    }

    @Test
    public void testSolveWithDefaultAllowedSolution() {
        int maxEval = 100;
        UnivariateRealFunction f = new BaseSecantSolverTest.DummyFunction();
        double min = -10;
        double max = 10;
        double startValue = 5;

        double result = solver.solve(maxEval, f, min, max, startValue);

        assertEquals(0, result, 0);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithInvalidInterval() {
        int maxEval = 100;
        UnivariateRealFunction f = new BaseSecantSolverTest.DummyFunction();
        double min = 10;
        double max = -10;
        double startValue = 5;
        BracketedUnivariateRealSolver.AllowedSolution allowedSolution =
                BracketedUnivariateRealSolver.AllowedSolution.ANY_SIDE;

        solver.solve(maxEval, f, min, max, startValue, allowedSolution);
    }

    @Test(expected = MathInternalError.class)
    public void testSolveWithInvalidMethod() {
        int maxEval = 100;
        UnivariateRealFunction f = new BaseSecantSolverTest.DummyFunction();
        double min = -10;
        double max = 10;
        double startValue = 5;
        BaseSecantSolver.Method method = null;
        BracketedUnivariateRealSolver.AllowedSolution allowedSolution =
                BracketedUnivariateRealSolver.AllowedSolution.ANY_SIDE;

        BaseSecantSolver.DummySecantSolver invalidSolver =
                new BaseSecantSolverTest.DummySecantSolver(1e-10, method);

        invalidSolver.solve(maxEval, f, min, max, startValue, allowedSolution);
    }
}