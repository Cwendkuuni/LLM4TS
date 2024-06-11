import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.0, distribution.getLowerBound(), 0.0);
        assertEquals(1.0, distribution.getUpperBound(), 0.0);
        assertEquals(1.0 / (1.0 - 0.0), distribution.density(0.5), 0.01);
    }

    @Test
    public void testUniformRealDistributionCustomConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution(3.0, 6.0);
        assertEquals(3.0, distribution.getLowerBound(), 0.0);
        assertEquals(6.0, distribution.getUpperBound(), 0.0);
        assertEquals(1.0 / (6.0 - 3.0), distribution.density(4.5), 0.01);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionInvalidLowerBoundAndUpperBound() {
        new UniformRealDistribution(5.0, 5.0);
    }

    @Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.1, 0.3);
        double sample = distribution.sample();
        assertTrue(sample >= 0.1 && sample < 0.3);
    }

    @Test
    public void testUniformRealDistributionMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.5, distribution.getNumericalMean(), 0.01);
    }

    @Test
    public void testUniformRealDistributionVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.25, distribution.getNumericalVariance(), 0.01);
    }

    @Test
    public void testUniformRealDistributionSupportBoundaries() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testUniformRealDistributionSupportInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testUniformRealDistributionSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertTrue(distribution.isSupportConnected());
    }
}