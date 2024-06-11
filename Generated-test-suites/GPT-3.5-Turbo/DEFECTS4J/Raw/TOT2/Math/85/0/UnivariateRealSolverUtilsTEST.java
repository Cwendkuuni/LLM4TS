import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;

public class UnivariateRealSolverUtilsTest {
    
    @Test
    public void testSolveBasic() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = UnivariateRealSolverUtils.solve(f, 0, 5);
        assertEquals(2.0, result, 0.0001);
    }
    
    @Test
    public void testSolveWithAccuracy() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = UnivariateRealSolverUtils.solve(f, 0, 5, 0.01);
        assertEquals(2.0009, result, 0.0001);
    }
    
    @Test
    public void testBracket() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double[] bracket = UnivariateRealSolverUtils.bracket(f, 1, -2, 2);
        assertEquals(-1.0, bracket[0], 0.0001);
        assertEquals(1.0, bracket[1], 0.0001);
    }
    
    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(0, 10);
        assertEquals(5.0, result, 0.0001);
    }
    
}