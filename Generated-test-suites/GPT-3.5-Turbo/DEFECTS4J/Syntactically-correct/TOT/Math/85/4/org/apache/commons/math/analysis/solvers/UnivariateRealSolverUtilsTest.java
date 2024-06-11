import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;

public class UnivariateRealSolverUtilsTest {
    // Test for solve(UnivariateRealFunction f, double x0, double x1)
    
    @Test
    public void testSolve_WithValidInputs() throws FunctionEvaluationException, ConvergenceException {
        // Assign
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x - 1;
            }
        };
        double x0 = 0;
        double x1 = 2;
        
        // Action
        double result = UnivariateRealSolverUtils.solve(f, x0, x1);
        
        // Assert
        assertEquals(1, result, 0.0001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSolve_WithNullFunction() throws FunctionEvaluationException, ConvergenceException {
        // Assign
        UnivariateRealFunction f = null;
        double x0 = 0;
        double x1 = 2;
        
        // Action
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSolve_WithInvalidInterval() throws FunctionEvaluationException, ConvergenceException {
        // Assign
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x - 1;
            }
        };
        double x0 = 2;
        double x1 = 0;
        
        // Action
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }
    
    // Test for solve(UnivariateRealFunction f, double x0, double x1, double absoluteAccuracy)
    
    @Test
    public void testSolve_WithValidInputsAndAbsoluteAccuracy() throws FunctionEvaluationException, ConvergenceException {
        // Assign
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x - 1;
            }
        };
        double x0 = 0;
        double x1 = 2;
        double absoluteAccuracy = 0.0001;
        
        // Action
        double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        
        // Assert
        assertEquals(1, result, absoluteAccuracy);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSolve_WithInvalidAbsoluteAccuracy() throws FunctionEvaluationException, ConvergenceException {
        // Assign
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x - 1;
            }
        };
        double x0 = 0;
        double x1 = 2;
        double absoluteAccuracy = -0.0001;
        
        // Action
        UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
    }
    
    // Test for bracket(UnivariateRealFunction function, double initial, double lowerBound, double upperBound)
    
    @Test
    public void testBracket_WithValidInputs() throws FunctionEvaluationException, ConvergenceException {
        // Assign
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x - 1;
            }
        };
        double initial = 1.5;
        double lowerBound = 0;
        double upperBound = 2;
        
        // Action
        double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        
        // Assert
        assertEquals(lowerBound, result[0], 0.0001);
        assertEquals(upperBound, result[1], 0.0001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBracket_WithNullFunction() throws FunctionEvaluationException, ConvergenceException {
        // Assign
        UnivariateRealFunction function = null;
        double initial = 1.5;
        double lowerBound = 0;
        double upperBound = 2;
        
        // Action
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBracket_WithInvalidInitial() throws FunctionEvaluationException, ConvergenceException {
        // Assign
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x - 1;
            }
        };
        double initial = -1;
        double lowerBound = 0;
        double upperBound = 2;
        
        // Action
        UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
    }
    
    // Test for bracket(UnivariateRealFunction function, double initial, double lowerBound, double upperBound, int maximumIterations)
    
    @Test
    public void testBracket_WithValidInputsAndMaximumIterations() throws FunctionEvaluationException, ConvergenceException {
        // Assign
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x - 1;
            }
        };
        double initial = 1.5;
        double lowerBound = 0;
        double upperBound = 2;
        int maximumIterations = 10;
        
        // Action
        double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);
        
        // Assert
        assertEquals(lowerBound, result[0], 0.0001);
        assertEquals(upperBound, result[1], 0.0001);
    }
    
    // Additional test for midpoint(double a, double b)
    
    @Test
    public void testMidpoint_WithValidInputs() {
        // Assign
        double a = 0;
        double b = 2;
        
        // Action
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        
        // Assert
        assertEquals(1, result, 0.0001);
    }
}