import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolveWithDefaultSolver() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5; // simple linear function
            }
        };
        
        double result = UnivariateRealSolverUtils.solve(f, 0, 10);
        assertEquals(5.0, result, 0.0001);
    }

    @Test
    public void testSolveWithDefaultSolverAndAbsoluteAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 16; // quadratic function
            }
        };
        
        double result = UnivariateRealSolverUtils.solve(f, -10, 10, 0.0001);
        assertEquals(4.0, result, 0.0001);
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4; // quadratic function with roots at -2 and 2
            }
        };
        
        double[] bracket = UnivariateRealSolverUtils.bracket(f, 0, -5, 5);
        assertEquals(-2.0, bracket[0], 0.0001);
        assertEquals(2.0, bracket[1], 0.0001);
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(0, 10);
        assertEquals(5.0, result, 0.0001);
    }

}