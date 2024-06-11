import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class UnivariateRealSolverUtilsTest {
    
    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 1;
            }
        };
        
        double result = UnivariateRealSolverUtils.solve(f, 0, 2);
        assertEquals(1.0, result, 0.0001);
    }
    
    @Test
    public void testSolveWithAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 1;
            }
        };
        
        double result = UnivariateRealSolverUtils.solve(f, 0, 2, 0.001);
        assertEquals(1.0, result, 0.001);
    }
    
    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 1;
            }
        };
        
        double[] result = UnivariateRealSolverUtils.bracket(f, 0, -2, 2);
        assertEquals(-2.0, result[0], 0.0001);
        assertEquals(2.0, result[1], 0.0001);
    }
    
    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(0, 2);
        assertEquals(1.0, result, 0.0001);
    }
}