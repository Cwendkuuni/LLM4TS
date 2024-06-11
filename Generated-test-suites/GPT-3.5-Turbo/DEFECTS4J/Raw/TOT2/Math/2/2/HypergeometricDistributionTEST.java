import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    // Test cases for constructor
    @Test
    public void testConstructor_PositiveInputs() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertNotNull(dist);
        } catch (Exception e) {
            fail("Exception thrown for valid inputs");
        }
    }

    @Test
    public void testConstructor_NegativeNumberOfSuccesses() {
        try {
            new HypergeometricDistribution(10, -5, 3);
            fail("Expected NotPositiveException to be thrown for negative number of successes");
        } catch (NotPositiveException e) {
            // Expected NotPositiveException
        }
    }

    @Test
    public void testConstructor_ZeroPopulationSize() {
        try {
            new HypergeometricDistribution(0, 5, 3);
            fail("Expected NotStrictlyPositiveException to be thrown for zero population size");
        } catch (NotStrictlyPositiveException e) {
            // Expected NotStrictlyPositiveException
        }
    }

    @Test
    public void testConstructor_SampleSizeGreaterThanPopulationSize() {
        try {
            new HypergeometricDistribution(10, 5, 15);
            fail("Expected NumberIsTooLargeException to be thrown for sample size greater than population size");
        } catch (NumberIsTooLargeException e) {
            // Expected NumberIsTooLargeException
        }
    }
    
    // Add more test cases for other public methods here

}