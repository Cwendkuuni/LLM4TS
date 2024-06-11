import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Before;
import org.junit.Test;

public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;

    @Before
    public void setup() {
        distribution = new UniformRealDistribution();
    }

    @Test
    public void testDensity() {
        assertEquals(0.0, distribution.density(-1), 0.00001);
        assertEquals(0.0, distribution.density(2), 0.00001);
        assertEquals(0.5, distribution.density(0.5), 0.00001);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.00001);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.00001);
        assertEquals(1.0, distribution.cumulativeProbability(1), 0.00001);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(0.5, distribution.getNumericalMean(), 0.00001);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(1.0 / 12.0, distribution.getNumericalVariance(), 0.00001);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.00001);
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.00001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        double sample = distribution.sample();
        assertTrue(sample >= 0.0 && sample < 1.0);
    }
}