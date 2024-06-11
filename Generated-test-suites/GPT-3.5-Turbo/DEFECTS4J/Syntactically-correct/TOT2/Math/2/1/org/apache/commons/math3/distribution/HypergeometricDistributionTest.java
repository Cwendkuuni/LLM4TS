import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        // Typical use case
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 2);
        assertEquals(0.3636, dist.cumulativeProbability(1), 0.0001);

        // Edge case
        assertEquals(1.0, dist.cumulativeProbability(3), 0.0);

        // Exception handling
        try {
            new HypergeometricDistribution(-5, 2, 1);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // as expected
        }
    }

    @Test
    public void testNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 2);
        assertEquals(3, dist.getNumberOfSuccesses());
    }

    @Test
    public void testProbability() {
        // Typical use case
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 2);
        assertEquals(0.6, dist.probability(1), 0.0);

        // Edge case
        assertEquals(0.0, dist.probability(5), 0.0);
    }

    // Add more test cases for other methods as needed

}