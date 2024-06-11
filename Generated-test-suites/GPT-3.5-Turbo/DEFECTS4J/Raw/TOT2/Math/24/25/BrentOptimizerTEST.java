import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test the optimization algorithm
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-8);
        final double result = optimizer.doOptimize().getValue();
        final double expected = 0.0; // Change this value based on expected result
        assertEquals(expected, result, 1e-8);
    }

    @Test
    public void testBest() {
        // Test the best method for selecting points
        UnivariatePointValuePair pointA = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair pointB = new UnivariatePointValuePair(2.0, 5.0);
        boolean isMinim = true;

        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-8);
        final UnivariatePointValuePair result = optimizer.best(pointA, pointB, isMinim);
        
        // In this case, result should be pointB as it has lower value
        assertEquals(pointB, result);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRel() {
        // Test constructor with invalid relative threshold
        // This should throw NumberIsTooSmallException
        new BrentOptimizer(1e-15, 1e-8);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbs() {
        // Test constructor with invalid absolute threshold
        // This should throw NotStrictlyPositiveException
        new BrentOptimizer(1e-10, -1e-8);
    }

    // Add more test cases as needed

}