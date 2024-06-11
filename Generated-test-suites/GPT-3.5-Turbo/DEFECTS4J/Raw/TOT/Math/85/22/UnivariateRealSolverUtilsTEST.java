import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;

public class UnivariateRealSolverUtilsTest {
    
    private UnivariateRealFunction function;
    
    @Before
    public void setup() {
        function = new UnivariateRealFunction() {
            public double value(double x) {
                // Define the function here
                return x * x - 4;
            }
        };
    }
    
    @Test
    public void testSolve() {
        try {
            double result = UnivariateRealSolverUtils.solve(function, 1, 3);
            assertEquals(2.0, result, 0.000001);
        } catch (ConvergenceException e) {
            e.printStackTrace();
            fail("ConvergenceException thrown");
        } catch (FunctionEvaluationException e) {
            e.printStackTrace();
            fail("FunctionEvaluationException thrown");
        }
    }
    
    @Test
    public void testSolveWithAccuracy() {
        try {
            double result = UnivariateRealSolverUtils.solve(function, 1, 3, 0.000001);
            assertEquals(2.0, result, 0.000001);
        } catch (ConvergenceException e) {
            e.printStackTrace();
            fail("ConvergenceException thrown");
        } catch (FunctionEvaluationException e) {
            e.printStackTrace();
            fail("FunctionEvaluationException thrown");
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.solve(null, 1, 3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.solve(function, 3, 1);
    }
    
    @Test
    public void testBracket() {
        try {
            double[] result = UnivariateRealSolverUtils.bracket(function, 2, 1, 3);
            assertEquals(1.0, result[0], 0.000001);
            assertEquals(3.0, result[1], 0.000001);
        } catch (ConvergenceException e) {
            e.printStackTrace();
            fail("ConvergenceException thrown");
        } catch (FunctionEvaluationException e) {
            e.printStackTrace();
            fail("FunctionEvaluationException thrown");
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithNullFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(null, 2, 1, 3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(function, 3, 1, 2);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testBracketWithNoRoot() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction noRootFunction = new UnivariateRealFunction() {
            public double value(double x) {
                return x + 1;
            }
        };
        UnivariateRealSolverUtils.bracket(noRootFunction, 0, -1, 1);
    }
    
    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(1, 3);
        assertEquals(2.0, result, 0.000001);
    }
}