import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {
    
    private PowellOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new PowellOptimizer(1e-6, 1e-6);
    }
    
    @Test
    public void testMinimumValue() {
        // Typical use case: finding minimum value of a quadratic function
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return Math.pow(x - 4, 2);
            }
        };
        
        double[] guess = {2};
        
        PointValuePair result = optimizer.optimize(function, null, guess, GoalType.MINIMIZE);
        
        assertEquals(4.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }
    
    @Test
    public void testMaximumValue() {
        // Typical use case: finding maximum value of a quadratic function
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return -Math.pow(x - 4, 2);
            }
        };
        
        double[] guess = {2};
        
        PointValuePair result = optimizer.optimize(function, null, guess, GoalType.MAXIMIZE);
        
        assertEquals(4.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }
    
    @Test
    public void testLocalMinimum() {
        // Testing for local minimum
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return Math.abs(x);
            }
        };
        
        double[] guess = {2};
        
        PointValuePair result = optimizer.optimize(function, null, guess, GoalType.MINIMIZE);
        
        assertNotEquals(0.0, result.getPoint(), 1e-6);
        assertTrue(result.getValue() > 0);
    }
    
    @Test
    public void testInvalidGoalType() {
        // Error scenario: Invalid goal type
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x;
            }
        };
        
        double[] guess = {2};
        
        try {
            optimizer.optimize(function, null, guess, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid goal type: null", e.getMessage());
        }
    }
    
    @Test
    public void testInvalidStartPoint() {
        // Error scenario: Invalid start point
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x;
            }
        };
        
        double[] guess = {};
        
        try {
            optimizer.optimize(function, null, guess, GoalType.MINIMIZE);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid start point: empty array", e.getMessage());
        }
    }
    
    @Test
    public void testConvergenceChecker() {
        // Error scenario: Custom convergence checker
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x;
            }
        };
        
        double[] guess = {2};
        
        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return iteration > 10;
            }
        };
        
        optimizer = new PowellOptimizer(1e-6, 1e-6, customChecker);
        
        PointValuePair result = optimizer.optimize(function, null, guess, GoalType.MINIMIZE);
        
        assertTrue(result.getValue() > 0);
    }
}