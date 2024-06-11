import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testPoissonDistributionImpl_ValidConstruction() {
        PoissonDistribution distribution = new PoissonDistributionImpl(2.5);
        assertNotNull(distribution);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPoissonDistributionImpl_InvalidConstruction() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testGetMean() {
        PoissonDistribution distribution = new PoissonDistributionImpl(3.2);
        assertEquals(3.2, distribution.getMean(), 0.001);
    }

    @Test
    public void testProbability_Zero() {
        PoissonDistribution distribution = new PoissonDistributionImpl(1.0);
        assertEquals(0.2400987351231, distribution.probability(0), 0.000001);
    }

    @Test
    public void testProbability_Valid() {
        PoissonDistribution distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.1055056043521, distribution.probability(1), 0.000001);
        assertEquals(0.0804681336665, distribution.probability(2), 0.000001);
        assertEquals(0.1159557615733, distribution.probability(3), 0.000001);
    }

    @Test
    public void testProbability_Invalid() {
        PoissonDistribution distribution = new PoissonDistributionImpl(4.5);
        assertEquals(0, distribution.probability(-1), 0.0);
        assertEquals(0, distribution.probability(Integer.MAX_VALUE), 0.0);
    }

    @Test
    public void testCumulativeProbability_Zero() {
        PoissonDistribution distribution = new PoissonDistributionImpl(1.0);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.000001);
    }

    @Test
    public void testCumulativeProbability_Valid() {
        PoissonDistribution distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.1836556058226, distribution.cumulativeProbability(1), 0.000001);
        assertEquals(0.3184124821587, distribution.cumulativeProbability(2), 0.000001);
        assertEquals(0.501381856525, distribution.cumulativeProbability(3), 0.000001);
    }

    @Test
    public void testCumulativeProbability_Invalid() {
        PoissonDistribution distribution = new PoissonDistributionImpl(4.5);
        assertEquals(0, distribution.cumulativeProbability(-1), 0.0);
        assertEquals(1, distribution.cumulativeProbability(Integer.MAX_VALUE), 0.000001);
    }

    @Test
    public void testNormalApproximateProbability_Zero() {
        PoissonDistribution distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.045588112332, distribution.normalApproximateProbability(0), 0.000001);
    }

    @Test
    public void testNormalApproximateProbability_Valid() {
        PoissonDistribution distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.105623682623, distribution.normalApproximateProbability(1), 0.000001);
        assertEquals(0.080553741313, distribution.normalApproximateProbability(2), 0.000001);
        assertEquals(0.115975179552, distribution.normalApproximateProbability(3), 0.000001);
    }

    @Test
    public void testSample() {
        PoissonDistribution distribution = new PoissonDistributionImpl(3.5);
        int sample = distribution.sample();
        assertEquals(3, sample);
    }

    @Test
    public void testSample_LargeMean() {
        PoissonDistribution distribution = new PoissonDistributionImpl(100.5);
        int sample = distribution.sample();
        assertTrue(sample >= 95 && sample <= 106);
    }

    @Test
    public void testSample_SmallMean() {
        PoissonDistribution distribution = new PoissonDistributionImpl(0.5);
        int sample = distribution.sample();
        assertTrue(sample >= 0 && sample <= 6);
    }

    @Test
    public void testInverseCumulativeProbability_Zero() {
        PoissonDistribution distribution = new PoissonDistributionImpl(1.0);
        double p = 0.0;
        int result = distribution.inverseCumulativeProbability(p);
        assertEquals(0, result);
    }

    @Test
    public void testInverseCumulativeProbability_One() {
        PoissonDistribution distribution = new PoissonDistributionImpl(1.0);
        double p = 1.0;
        int result = distribution.inverseCumulativeProbability(p);
        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test
    public void testInverseCumulativeProbability_Valid() {
        PoissonDistribution distribution = new PoissonDistributionImpl(2.5);
        double p = 0.3;
        int result = distribution.inverseCumulativeProbability(p);
        assertEquals(1, result);
    }

    @Test
    public void testInverseCumulativeProbability_LargeProbability() {
        PoissonDistribution distribution = new PoissonDistributionImpl(2.5);
        double p = 0.999;
        int result = distribution.inverseCumulativeProbability(p);
        assertEquals(Integer.MAX_VALUE - 2, result);
    }
}