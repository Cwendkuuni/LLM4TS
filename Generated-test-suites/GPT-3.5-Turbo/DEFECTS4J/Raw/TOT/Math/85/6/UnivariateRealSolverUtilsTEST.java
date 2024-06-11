import static org.junit.Assert.*;

import org.apache.commons.math.analysis.QuinticFunction;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.ConvergenceException;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve_default() {
        UnivariateRealFunction f = new QuinticFunction(); // sample function
        double x0 = 0.0; // lower bound
        double x1 = 1.0; // upper bound
        
        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1);
            
            // Check if the result is accurate within certain threshold
            assertEquals(0.0, f.value(result), 0.0001);
            
        } catch (ConvergenceException ce) {
            fail("Should not throw ConvergenceException");
        } catch (Exception e) {
            fail("Should not throw any other exceptions");
        }
    }
    
    @Test
    public void testSolve_withAbsoluteAccuracy() {
        UnivariateRealFunction f = new QuinticFunction(); // sample function
        double x0 = 0.0; // lower bound
        double x1 = 1.0; // upper bound
        double absoluteAccuracy = 0.0001; // accuracy
        
        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
            
            // Check if the result is accurate within the specified absolute accuracy
            assertEquals(0.0, f.value(result), absoluteAccuracy);
            
        } catch (ConvergenceException ce) {
            fail("Should not throw ConvergenceException");
        } catch (Exception e) {
            fail("Should not throw any other exceptions");
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSolve_nullFunction() {
        UnivariateRealFunction f = null; // null function
        double x0 = 0.0; // lower bound
        double x1 = 1.0; // upper bound
        
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSolve_invalidInterval() {
        UnivariateRealFunction f = new QuinticFunction(); // sample function
        double x0 = 1.0; // lower bound > upper bound
        double x1 = 0.0; // upper bound < lower bound
        
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testSolve_convergenceException() {
        // Test case where the solver fails to converge
        
        UnivariateRealFunction f = new QuinticFunction(); // sample function
        double x0 = -10.0; // lower bound
        double x1 = 10.0; // upper bound
        
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new QuinticFunction(); // sample function
        double initial = 0.5; // initial midpoint
        double lowerBound = 0.0; // lower bound
        double upperBound = 1.0; // upper bound
        int maximumIterations = 100; // maximum number of iterations
        
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
            
            double a = result[0];
            double b = result[1];
            
            // Check if a and b bracket a root of the function
            assertTrue(f.value(a) * f.value(b) < 0.0);
            
        } catch (ConvergenceException ce) {
            fail("Should not throw ConvergenceException");
        } catch (Exception e) {
            fail("Should not throw any other exceptions");
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBracket_nullFunction() {
        UnivariateRealFunction f = null; // null function
        double initial = 0.5; // initial midpoint
        double lowerBound = 0.0; // lower bound
        double upperBound = 1.0; // upper bound
        int maximumIterations = 100; // maximum number of iterations
        
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBracket_invalidInterval() {
        UnivariateRealFunction f = new QuinticFunction(); // sample function
        double initial = 0.5; // initial midpoint
        double lowerBound = 1.0; // lower bound > upper bound
        double upperBound = 0.0; // upper bound < lower bound
        int maximumIterations = 100; // maximum number of iterations
        
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBracket_invalidMaxIterations() {
        UnivariateRealFunction f = new QuinticFunction(); // sample function
        double initial = 0.5; // initial midpoint
        double lowerBound = 0.0; // lower bound
        double upperBound = 1.0; // upper bound
        int maximumIterations = 0; // maximum number of iterations <= 0
        
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testBracket_convergenceException() {
        // Test case where the algorithm fails to find a and b satisfying the desired conditions
        
        UnivariateRealFunction f = new QuinticFunction(); // sample function
        double initial = 0.5; // initial midpoint
        double lowerBound = -0.1; // lower bound
        double upperBound = 0.1; // upper bound
        int maximumIterations = 100; // maximum number of iterations
        
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }
    
    @Test
    public void testMidpoint() {
        double a = 0.0;
        double b = 2.0;
        
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        
        // Check if the result is the midpoint between a and b
        assertEquals(1.0, result, 0.0001);
    }
    
    @Test
    public void testSetup() {
        UnivariateRealFunction f = new QuinticFunction(); // sample function
        
        try {
            UnivariateRealSolverUtils.setup(f);
        } catch (Exception e) {
            fail("Should not throw any exceptions");
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetup_nullFunction() {
        UnivariateRealFunction f = null; // null function
        
        UnivariateRealSolverUtils.setup(f);
    }
}