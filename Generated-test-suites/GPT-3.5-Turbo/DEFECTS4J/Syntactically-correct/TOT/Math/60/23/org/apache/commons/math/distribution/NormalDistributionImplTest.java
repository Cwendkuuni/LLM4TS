import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        // Test with positive mean
        NormalDistributionImpl distribution1 = new NormalDistributionImpl(5, 2);
        assertEquals(5, distribution1.getMean(), 0);

        // Test with negative mean
        NormalDistributionImpl distribution2 = new NormalDistributionImpl(-10, 3);
        assertEquals(-10, distribution2.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        // Test with positive standard deviation
        NormalDistributionImpl distribution1 = new NormalDistributionImpl(5, 2);
        assertEquals(2, distribution1.getStandardDeviation(), 0);

        // Test with negative standard deviation
        NormalDistributionImpl distribution2 = new NormalDistributionImpl(-10, -3);
        assertEquals(-3, distribution2.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        // Test with positive x
        NormalDistributionImpl distribution1 = new NormalDistributionImpl(5, 2);
        assertEquals(0.02699548325659403, distribution1.density(7), 1e-9);

        // Test with zero x
        NormalDistributionImpl distribution2 = new NormalDistributionImpl(5, 2);
        assertEquals(0.12098536225957168, distribution2.density(5), 1e-9);

        // Test with negative x
        NormalDistributionImpl distribution3 = new NormalDistributionImpl(5, 2);
        assertEquals(0.02699548325659403, distribution3.density(3), 1e-9);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        // Test with positive x
        NormalDistributionImpl distribution1 = new NormalDistributionImpl(5, 2);
        assertEquals(0.5987063256829237, distribution1.cumulativeProbability(7), 1e-9);

        // Test with zero x
        NormalDistributionImpl distribution2 = new NormalDistributionImpl(5, 2);
        assertEquals(0.5, distribution2.cumulativeProbability(5), 1e-9);

        // Test with negative x
        NormalDistributionImpl distribution3 = new NormalDistributionImpl(5, 2);
        assertEquals(0.4012936743170763, distribution3.cumulativeProbability(3), 1e-9);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        // Test with positive p
        NormalDistributionImpl distribution1 = new NormalDistributionImpl(5, 2);
        assertEquals(7.289707253902945, distribution1.inverseCumulativeProbability(0.8), 1e-9);

        // Test with zero p
        NormalDistributionImpl distribution2 = new NormalDistributionImpl(5, 2);
        assertEquals(Double.NEGATIVE_INFINITY, distribution2.inverseCumulativeProbability(0), 1e-9);

        // Test with negative p
        NormalDistributionImpl distribution3 = new NormalDistributionImpl(5, 2);
        try {
            distribution3.inverseCumulativeProbability(-0.2);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            assertEquals("Given probability value is not valid: -0.2", e.getMessage());
        }
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        double sample = distribution.sample();

        // Check if the sampled value is within the expected range based on mean and standard deviation
        assertTrue(sample >= 1 && sample <= 9);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), 0);
    }

    @Test
    public void testGetDomainLowerBound() {
        // Test with p < 0.5
        NormalDistributionImpl distribution1 = new NormalDistributionImpl(5, 2);
        assertEquals(-Double.MAX_VALUE, distribution1.getDomainLowerBound(0.3), 0);

        // Test with p > 0.5
        NormalDistributionImpl distribution2 = new NormalDistributionImpl(5, 2);
        assertEquals(5, distribution2.getDomainLowerBound(0.8), 0);

        // Test with p = 0.5
        NormalDistributionImpl distribution3 = new NormalDistributionImpl(5, 2);
        assertEquals(-Double.MAX_VALUE, distribution3.getDomainLowerBound(0.5), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        // Test with p < 0.5
        NormalDistributionImpl distribution1 = new NormalDistributionImpl(5, 2);
        assertEquals(5, distribution1.getDomainUpperBound(0.3), 0);

        // Test with p > 0.5
        NormalDistributionImpl distribution2 = new NormalDistributionImpl(5, 2);
        assertEquals(Double.MAX_VALUE, distribution2.getDomainUpperBound(0.8), 0);

        // Test with p = 0.5
        NormalDistributionImpl distribution3 = new NormalDistributionImpl(5, 2);
        assertEquals(Double.MAX_VALUE, distribution3.getDomainUpperBound(0.5), 0);
    }

    @Test
    public void testGetInitialDomain() {
        // Test with p < 0.5
        NormalDistributionImpl distribution1 = new NormalDistributionImpl(5, 2);
        assertEquals(3, distribution1.getInitialDomain(0.3), 0);

        // Test with p > 0.5
        NormalDistributionImpl distribution2 = new NormalDistributionImpl(5, 2);
        assertEquals(7, distribution2.getInitialDomain(0.8), 0);

        // Test with p = 0.5
        NormalDistributionImpl distribution3 = new NormalDistributionImpl(5, 2);
        assertEquals(5, distribution3.getInitialDomain(0.5), 0);
    }

}