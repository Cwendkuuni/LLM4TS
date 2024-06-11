import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1.0, distribution.density(0.5), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.5, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1.0/12.0, distribution.getNumericalVariance(), 0.0001);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorException() {
        new UniformRealDistribution(1, 0);
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
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double sample = distribution.sample();
        assertTrue(sample >= 0.0 && sample <= 1.0);
    }
}