import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.QuinticFunction;
import org.apache.commons.math.analysis.Expm1Function;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;

import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    // Test case for solve() method with default solver
    @Test
    public void testSolveWithDefaultSolver() {
        UnivariateRealFunction f = new QuinticFunction();
        double lowerBound = -1.0;
        double upperBound = 1.0;
        
        try {
            double result = UnivariateRealSolverUtils.solve(f, lowerBound, upperBound);
            // assert statements for the result
            assertEquals(f.value(result), 0.0, 0.0001);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    // Test case for solve() method with specified solver and absolute accuracy
    @Test
    public void testSolveWithAbsoluteAccuracy() {
        UnivariateRealFunction f = new Expm1Function();
        double lowerBound = 0.0;
        double upperBound = 1.0;
        double absoluteAccuracy = 0.0001;
        
        try {
            double result = UnivariateRealSolverUtils.solve(f, lowerBound, upperBound, absoluteAccuracy);
            // assert statements for the result
            assertEquals(f.value(result), 0.0, absoluteAccuracy);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    // Test case for bracket() method
    @Test
    public void testBracket() {
        UnivariateRealFunction f = new QuinticFunction();
        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 1.0;
        int maximumIterations = 100;
        
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
            // assert statements for the result
            assertTrue(result[0] < initial && initial < result[1]);
            assertTrue(f.value(result[0]) * f.value(result[1]) <= 0.0);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    // Test case for midpoint() method
    @Test
    public void testMidpoint() {
        double a = -10.0;
        double b = 10.0;
        double expectedResult = 0.0;
        
        // call the midpoint() method
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        
        // assert statements for the result
        assertEquals(expectedResult, result, 0.0001);
    }
    
    // Test case for solve() method with null function
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() {
        UnivariateRealFunction f = null;
        double lowerBound = 0.0;
        double upperBound = 1.0;
        
        UnivariateRealSolverUtils.solve(f, lowerBound, upperBound);
    }
    
    // Test case for solve() method with invalid bounds
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidBounds() {
        UnivariateRealFunction f = new QuinticFunction();
        double lowerBound = 1.0;
        double upperBound = 0.0;
        
        UnivariateRealSolverUtils.solve(f, lowerBound, upperBound);
    }
    
    // Test case for solve() method with negative absolute accuracy
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNegativeAbsoluteAccuracy() {
        UnivariateRealFunction f = new Expm1Function();
        double lowerBound = 0.0;
        double upperBound = 1.0;
        double absoluteAccuracy = -0.0001;
        
        UnivariateRealSolverUtils.solve(f, lowerBound, upperBound, absoluteAccuracy);
    }
    
    // Test case for bracket() method with null function
    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithNullFunction() {
        UnivariateRealFunction f = null;
        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 1.0;
        int maximumIterations = 100;
        
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }
    
    // Test case for bracket() method with negative maximumIterations
    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithNegativeMaximumIterations() {
        UnivariateRealFunction f = new QuinticFunction();
        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 1.0;
        int maximumIterations = -100;
        
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }

}