import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.AllowedSolution;
import org.apache.commons.math.util.FastMath;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {
    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1e-6;

    @Test
    public void testSolve() {
        BaseSecantSolver.Method method = BaseSecantSolver.Method.REGULA_FALSI;
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, method) {
            @Override
            protected double computeObjectiveValue(double x) {
                return Math.exp(x) - 2;
            }
        };
        int maxEval = 100;
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };
        double min = 0;
        double max = 2;
        double expectedResult = Math.log(2);
        double result = solver.solve(maxEval, f, min, max, AllowedSolution.ANY_SIDE);
        assertEquals(expectedResult, result, DEFAULT_ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        BaseSecantSolver.Method method = BaseSecantSolver.Method.PEGASUS;
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, method) {
            @Override
            protected double computeObjectiveValue(double x) {
                return Math.exp(x) - 2;
            }
        };
        int maxEval = 100;
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };
        double min = 0;
        double max = 2;
        double startValue = 1;
        double expectedResult = Math.log(2);
        double result = solver.solve(maxEval, f, min, max, startValue, AllowedSolution.ABOVE_SIDE);
        assertEquals(expectedResult, result, DEFAULT_ABSOLUTE_ACCURACY);
    }
}