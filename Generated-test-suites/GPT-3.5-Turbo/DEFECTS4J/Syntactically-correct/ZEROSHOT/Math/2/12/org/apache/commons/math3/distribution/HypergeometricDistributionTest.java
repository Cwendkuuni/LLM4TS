import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HypergeometricDistributionTest {

    private HypergeometricDistribution distribution;

    @Before
    public void setUp() {
        distribution = new HypergeometricDistribution(10, 5, 3);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        assertEquals(5, distribution.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        assertEquals(10, distribution.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        assertEquals(3, distribution.getSampleSize());
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0);
        assertEquals(0.16666666666666666, distribution.cumulativeProbability(1), 0);
        assertEquals(1.0, distribution.cumulativeProbability(3), 0);
        assertEquals(1.0, distribution.cumulativeProbability(4), 0);
        assertEquals(1.0, distribution.cumulativeProbability(5), 0);
        assertEquals(1.0, distribution.cumulativeProbability(6), 0);
    }

    @Test
    public void testProbability() {
        assertEquals(0.0, distribution.probability(-1), 0);
        assertEquals(0.0, distribution.probability(0), 0);
        assertEquals(0.16666666666666666, distribution.probability(1), 0);
        assertEquals(0.3333333333333333, distribution.probability(2), 0);
        assertEquals(0.5, distribution.probability(3), 0);
        assertEquals(0.0, distribution.probability(4), 0);
        assertEquals(0.0, distribution.probability(5), 0);
        assertEquals(0.0, distribution.probability(6), 0);
    }

    @Test
    public void testUpperCumulativeProbability() {
        assertEquals(1.0, distribution.upperCumulativeProbability(-1), 0);
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0);
        assertEquals(1.0, distribution.upperCumulativeProbability(1), 0);
        assertEquals(0.8333333333333334, distribution.upperCumulativeProbability(2), 0);
        assertEquals(0.6666666666666666, distribution.upperCumulativeProbability(3), 0);
        assertEquals(0.5, distribution.upperCumulativeProbability(4), 0);
        assertEquals(0.5, distribution.upperCumulativeProbability(5), 0);
        assertEquals(0.5, distribution.upperCumulativeProbability(6), 0);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(1.5, distribution.getNumericalMean(), 0);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(0.6666666666666666, distribution.getNumericalVariance(), 0);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(2, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(3, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        assertEquals(true, distribution.isSupportConnected());
    }

}