import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.*;

public class UnivariateRealSolverUtilsTest {

    // Test solve method with default solver
    @Test
    public void testSolveDefaultSolver() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        double x0 = 1.0;
        double x1 = 3.0;
        double result = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(2.0, result, 0.0001);
    }

    // Test solve method with custom absolute accuracy
    @Test
    public void testSolveCustomAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };
        double x0 = 3.0;
        double x1 = 4.0;
        double absoluteAccuracy = 0.0001;
        double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        assertEquals(3.1416, result, 0.0001);
    }

    // Test bracket method
    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return Math.cos(x);
            }
        };
        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 1.0;
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        assertEquals(-1.0, result[0], 0.0001);
        assertEquals(1.0, result[1], 0.0001);
    }

    // Test midpoint method
    @Test
    public void testMidpoint() {
        double a = 0.0;
        double b = 2.0;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(1.0, result, 0.0001);
    }
}