import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructorWithConvergenceChecker() {
        // Create a BrentOptimizer with rel=0.1, abs=0.01, and a convergence checker
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01, null);
        
        assertNotNull(optimizer);
    }

    @Test
    public void testBrentOptimizerConstructorWithoutConvergenceChecker() {
        // Create a BrentOptimizer with rel=0.01, abs=0.001, and no convergence checker
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimizeWithCorrectInitialization() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.setMaxEvaluations(1000);
        
        // Set the function to be optimized 
        optimizer.setFunction(x -> x*x - 4);

        // Set the interval where the minimum is expected
        optimizer.setMin(0);
        optimizer.setMax(3);

        // Set the initial guess for the minimum
        optimizer.setStartValue(1.0);

        // Perform optimization
        UnivariatePointValuePair result = optimizer.optimize();
        
        assertEquals(2.0, result.getPoint(), 1e-6);
    }

    @Test
    public void testDoOptimizeWithInvalidInterval() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        
        // Set the interval where the minimum is expected
        optimizer.setMin(3);
        optimizer.setMax(0);

        // Set the initial guess for the minimum
        optimizer.setStartValue(1.0);

        // Perform optimization
        try {
            optimizer.optimize();
            fail("Expected IllegalArgumentException due to invalid interval");
        } catch (IllegalArgumentException e) {
            // Expected, test passed
        }
    }

    @Test
    public void testDoOptimizeWithInvalidFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.setMaxEvaluations(1000);
        
        // Set the interval where the minimum is expected
        optimizer.setMin(0);
        optimizer.setMax(3);

        // Set the initial guess for the minimum
        optimizer.setStartValue(1.0);

        // Perform optimization with null function
        try {
            optimizer.optimize();
            fail("Expected IllegalArgumentException due to null function");
        } catch (IllegalArgumentException e) {
            // Expected, test passed
        }
    }

    // Add more test cases as needed

}