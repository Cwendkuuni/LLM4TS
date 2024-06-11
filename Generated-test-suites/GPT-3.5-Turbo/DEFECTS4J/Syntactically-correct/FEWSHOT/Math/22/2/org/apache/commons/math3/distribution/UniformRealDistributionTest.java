import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(1.0, dist.density(0), 0.0001);
        assertEquals(1.0, dist.density(0.5), 0.0001);
        assertEquals(1.0, dist.density(1), 0.0001);
        assertEquals(0.0, dist.density(-1), 0.0001);
        assertEquals(0.0, dist.density(2), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(0.0, dist.cumulativeProbability(-1), 0.0001);
        assertEquals(0.0, dist.cumulativeProbability(0), 0.0001);
        assertEquals(0.5, dist.cumulativeProbability(0.5), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(1), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(2), 0.0001);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidBounds() {
        new UniformRealDistribution(1, 0);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 10);
        assertEquals(5.0, dist.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 10);
        assertEquals(8.3333, dist.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0001);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertTrue(!dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double sample = dist.sample();
        assertTrue(sample >= 0 && sample <= 1);
    }

}