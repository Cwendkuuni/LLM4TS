import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class UnivariateRealSolverUtilsTest {

    private class TestFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x * x - 4;
        }
    }
    
    private UnivariateRealFunction testFunction;
    
    @Before
    public void setUp() {
        testFunction = new TestFunction();
    }
    
    @Test
    public void testSolve() {
        try {
            double result = UnivariateRealSolverUtils.solve(testFunction, 0, 2);
            assertEquals("Test solve", 2.0, result, 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testSolveWithAccuracy() {
        try {
            double result = UnivariateRealSolverUtils.solve(testFunction, 0, 2, 0.0001);
            assertEquals("Test solve with accuracy", 2.0, result, 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testBracket() {
        try {
            double[] result = UnivariateRealSolverUtils.bracket(testFunction, 1, 0, 2);
            assertArrayEquals("Test bracket", new double[]{0, 2}, result, 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testBracketWithIterations() {
        try {
            double[] result = UnivariateRealSolverUtils.bracket(testFunction, 1, 0, 2, 10);
            assertArrayEquals("Test bracket with iterations", new double[]{0, 2}, result, 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(1, 3);
        assertEquals("Test midpoint", 2.0, result, 0.0001);
    }
    
}