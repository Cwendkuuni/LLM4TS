import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactory;
import org.junit.Test;

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
        double expected = 2.0;
        
        double result = UnivariateRealSolverUtils.solve(f, x0, x1);
        
        assertEquals(expected, result, 0.0001);
    }
    
    @Test
    public void testSolve_WithAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        double x0 = 1.0;
        double x1 = 3.0;
        double absoluteAccuracy = 0.0001;
        double expected = 2.0;
        
        double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        
        assertEquals(expected, result, absoluteAccuracy);
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
        double[] expected = {1.0, 3.0};
        
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        
        assertArrayEquals(expected, result, 0.0001);
    }
    
    @Test
    public void testBracket_WithMaximumIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        double initial = 2.0;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        int maximumIterations = 10;
        double[] expected = {1.0, 3.0};
        
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
        
        assertArrayEquals(expected, result, 0.0001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBracket_WithNullFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = null;
        
        double initial = 2.0;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBracket_WithInvalidParameters() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        double initial = 2.0;
        double lowerBound = 5.0;
        double upperBound = 3.0;
        
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }
    
    @Test
    public void testMidpoint() {
        double a = -1.0;
        double b = 1.0;
        double expected = 0.0;
        
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        
        assertEquals(expected, result, 0.0001);
    }
    
    @Test
    public void testSetup_WithValidFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        UnivariateRealSolverUtils.setup(f);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetup_WithNullFunction() {
        UnivariateRealFunction f = null;
        
        UnivariateRealSolverUtils.setup(f);
    }
    
    public void assertArrayEquals(double[] expected, double[] actual, double delta) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i], delta);
        }
    }

}