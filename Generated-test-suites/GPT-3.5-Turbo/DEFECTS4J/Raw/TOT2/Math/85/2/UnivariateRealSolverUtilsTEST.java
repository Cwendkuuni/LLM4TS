import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.apache.commons.math.analysis.solvers.ConvergenceException;
import org.apache.commons.math.analysis.FunctionEvaluationException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
    
    @Test
    public void testSolveBasic() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        double result = UnivariateRealSolverUtils.solve(f, 0, 10);
        assertEquals(5.0, result, 0.0001);
    }
    
    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = UnivariateRealSolverUtils.solve(f, 0, 10, 0.0001);
        assertEquals(2.0, result, 0.0001);
    }
    
    @Test
    public void testBracketValid() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };
        double[] bracket = UnivariateRealSolverUtils.bracket(f, Math.PI / 4, 0, Math.PI / 2);
        assertTrue(bracket[0] < bracket[1]);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testBracketInvalid() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };
        // This bracketing will not converge
        UnivariateRealSolverUtils.bracket(f, Math.PI / 4, 0, Math.PI / 4);
    }
    
    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(0, 10);
        assertEquals(5.0, result, 0.0001);
    }
}