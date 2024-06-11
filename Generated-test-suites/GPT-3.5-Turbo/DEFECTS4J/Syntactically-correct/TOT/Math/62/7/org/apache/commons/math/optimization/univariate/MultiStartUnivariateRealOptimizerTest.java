import static org.junit.Assert.*;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    // Test optimize() method with typical use cases
    // Typical use case 1: Single-start optimizer returns a valid point
    @Test
    public void testOptimizeTypicalUseCase1() {
        // Create a single-start optimizer with a mock optimizer
        BaseUnivariateRealOptimizer mockOptimizer = new MockUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(mockOptimizer, 3, new RandomGenerator());

        // Mock the optimize() method of the single-start optimizer to return a valid point
        UnivariateRealPointValuePair expected = new UnivariateRealPointValuePair(2.5, 10.0);
        ((MockUnivariateRealOptimizer) mockOptimizer).setOptimizeResult(expected);

        // Call the optimize() method
        UnivariateRealPointValuePair result = optimizer.optimize(null, null, 0.0, 5.0);

        // Verify that the result is the expected point
        assertEquals(expected, result);
    }

    // Test optimize() method with typical use cases
    // Typical use case 2: Single-start optimizer does not converge
    @Test(expected = ConvergenceException.class)
    public void testOptimizeTypicalUseCase2() {
        // Create a single-start optimizer with a mock optimizer
        BaseUnivariateRealOptimizer mockOptimizer = new MockUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(mockOptimizer, 3, new RandomGenerator());

        // Mock the optimize() method of the single-start optimizer to throw a ConvergenceException
        ((MockUnivariateRealOptimizer) mockOptimizer).setThrowConvergenceException(true);

        // Call the optimize() method
        optimizer.optimize(null, null, 0.0, 5.0);
    }

    // Test optimize() method with edge cases
    // Edge case 1: Single-start optimizer returns null
    @Test
    public void testOptimizeEdgeCase1() {
        // Create a single-start optimizer with a mock optimizer
        BaseUnivariateRealOptimizer mockOptimizer = new MockUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(mockOptimizer, 3, new RandomGenerator());

        // Mock the optimize() method of the single-start optimizer to return null
        ((MockUnivariateRealOptimizer) mockOptimizer).setOptimizeResult(null);

        // Call the optimize() method
        UnivariateRealPointValuePair result = optimizer.optimize(null, null, 0.0, 5.0);

        // Verify that the result is null
        assertNull(result);
    }

    // Test optimize() method with error scenarios
    // Error scenario 1: Max evaluations reached before convergence
    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeErrorScenario1() {
        // Create a single-start optimizer with a mock optimizer
        BaseUnivariateRealOptimizer mockOptimizer = new MockUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(mockOptimizer, 3, new RandomGenerator());

        // Mock the getMaxEvaluations() method of the optimizer to return a small value
        ((MockUnivariateRealOptimizer) mockOptimizer).setMockMaxEvaluations(5);

        // Call the optimize() method
        optimizer.optimize(null, null, 0.0, 5.0);
    }

    // Test getOptima() method
    @Test
    public void testGetOptima() {
        // Create a single-start optimizer with a mock optimizer
        BaseUnivariateRealOptimizer mockOptimizer = new MockUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(mockOptimizer, 3, new RandomGenerator());

        // Mock the getOptima() method of the optimizer
        UnivariateRealPointValuePair[] expected = new UnivariateRealPointValuePair[3];
        for (int i = 0; i < 3; i++) {
            expected[i] = new UnivariateRealPointValuePair(i, i * 10.0);
        }
        ((MockUnivariateRealOptimizer) mockOptimizer).setOptima(expected);

        // Call the getOptima() method
        UnivariateRealPointValuePair[] result = optimizer.getOptima();

        // Verify that the result is the expected optima
        assertArrayEquals(expected, result);
    }

}