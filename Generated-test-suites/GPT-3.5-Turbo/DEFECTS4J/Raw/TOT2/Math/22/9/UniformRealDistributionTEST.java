import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.5, distribution.density(0.5), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0.0001);
    }

    @Test
    public void testMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.5, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1.0/12, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0001);
    }

    @Test
    public void testSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.0001);
    }

    @Test
    public void testSupportInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double sample = distribution.sample();
        assertTrue(sample >= 0 && sample <= 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidRange() {
        new UniformRealDistribution(1, 0);
    }

    @Test
    public void testEdgeCaseDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.0, distribution.density(-1), 0.0001);
        assertEquals(0.0, distribution.density(2), 0.0001);
    }

    @Test
    public void testEdgeCaseCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(2), 0.0001);
    }

    @Test
    public void testExceptionHandling() {
        try {
            new UniformRealDistribution(0, 1, -1); // Should throw exception due to negative accuracy
            fail("NumberIsTooLargeException was not thrown");
        } catch (NumberIsTooLargeException e) {
            // Exception expected
        }
    }

}