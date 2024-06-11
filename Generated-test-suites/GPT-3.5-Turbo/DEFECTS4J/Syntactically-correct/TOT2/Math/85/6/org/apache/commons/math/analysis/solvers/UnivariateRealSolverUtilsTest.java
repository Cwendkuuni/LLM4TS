import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;

public class UnivariateRealSolverUtilsTest {
    
    @Test
    public void testSolveDefault() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double result = UnivariateRealSolverUtils.solve(f, 0, 5);
        assertEquals(2.0, result, 0.0001);
    }
    
    @Test
    public void testSolveWithAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = UnivariateRealSolverUtils.solve(f, 0, 5, 0.0001);
        assertEquals(2.0, result, 0.0001);
    }
    
    @Test
    public void testBracketSuccess() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        double[] result = UnivariateRealSolverUtils.bracket(f, 1.5, 0, 3);
        assertEquals(1.0, result[0], 0.0001);
        assertEquals(2.0, result[1], 0.0001);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testBracketFailure() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        };
        UnivariateRealSolverUtils.bracket(f, 1, 0, 3, 1); // Forces failure
    }
    
    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(0, 2);
        assertEquals(1.0, result, 0.0001);
    }
}