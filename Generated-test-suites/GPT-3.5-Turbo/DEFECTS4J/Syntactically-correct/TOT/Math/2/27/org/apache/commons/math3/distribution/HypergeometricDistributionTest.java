import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;

public class HypergeometricDistributionTest {

    private HypergeometricDistribution distribution;

    @Before
    public void setup() {
        distribution = new HypergeometricDistribution(50, 20, 10);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        assertEquals(0.9807, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.9936, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(100), 0.0001);
    }

    @Test
    public void testProbability() {
        assertEquals(0.0, distribution.probability(-1), 0.0001);
        assertEquals(0.0098, distribution.probability(0), 0.0001);
        assertEquals(0.0039, distribution.probability(1), 0.0001);
        assertEquals(0.0, distribution.probability(2), 0.0001);
        assertEquals(0.0, distribution.probability(100), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        assertEquals(1.0, distribution.upperCumulativeProbability(-1), 0.0001);
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.0193, distribution.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.0064, distribution.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(100), 0.0001);
    }

    @Test
    public void testGetDomain() {
        int[] domain = distribution.getDomain(50, 20, 10);
        assertEquals(10, domain[0]);
        assertEquals(20, domain[1]);
    }

    @Test
    public void testGetLowerDomain() {
        int lowerDomain = distribution.getLowerDomain(50, 20, 10);
        assertEquals(10, lowerDomain);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        assertEquals(20, distribution.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        assertEquals(50, distribution.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        assertEquals(10, distribution.getSampleSize());
    }

    @Test
    public void testGetUpperDomain() {
        int upperDomain = distribution.getUpperDomain(20, 10);
        assertEquals(10, upperDomain);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(4.0, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(0.8, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(10, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }
}