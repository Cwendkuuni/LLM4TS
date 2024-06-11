import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        double x0 = 1.0;
        double x1 = 3.0;
        double result = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(2.0, result, 0.001);
    }

    @Test
    public void testSolveWithAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        double x0 = 1.0;
        double x1 = 3.0;
        double absoluteAccuracy = 0.001;
        double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        assertEquals(2.0, result, 0.001);
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        double initial = 2.0;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        assertEquals(1.0, result[0], 0.001);
        assertEquals(3.0, result[1], 0.001);
    }

    @Test
    public void testBracketWithIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        double initial = 2.0;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        int maximumIterations = 100;
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
        assertEquals(1.0, result[0], 0.001);
        assertEquals(3.0, result[1], 0.001);
    }

    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 3.0;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(2.0, result, 0.001);
    }

}