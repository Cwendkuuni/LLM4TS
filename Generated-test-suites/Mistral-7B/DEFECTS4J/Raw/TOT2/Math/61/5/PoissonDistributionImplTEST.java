import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructor_ValidMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(1.5, distribution.getMean(), 0.001);
    }

    @Test
    public void testConstructor_InvalidMean() {
        try {
            new PoissonDistributionImpl(-0.5);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(LocalizedFormats.NOT_POSITIVE_POISSON_MEAN, e.getMessage());
        }
    }

    @Test
    public void testProbability_Zero() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        double probability = distribution.probability(0);
        assertEquals(0.606511177833512, probability, 0.0001);
    }

    @Test
    public void testProbability_Valid() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        double probability = distribution.probability(1);
        assertEquals(0.223132834623227, probability, 0.0001);
    }

    @Test
    public void testProbability_MaxValue() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        double probability = distribution.probability(Integer.MAX_VALUE);
        assertEquals(0.0, probability, 0.0001);
    }

    @Test
    public void testCumulativeProbability_Zero() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        double cumulativeProbability = distribution.cumulativeProbability(0);
        assertEquals(0.0, cumulativeProbability, 0.0001);
    }

    @Test
    public void testCumulativeProbability_MaxValue() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        double cumulativeProbability = distribution.cumulativeProbability(Integer.MAX_VALUE);
        assertEquals(1.0, cumulativeProbability, 0.0001);
    }

    @Test
    public void testCumulativeProbability_Valid() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        double cumulativeProbability = distribution.cumulativeProbability(1);
        assertEquals(0.682689492137306, cumulativeProbability, 0.0001);
    }

    @Test
    public void testNormalApproximateProbability_Zero() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        double normalApproximateProbability = distribution.normalApproximateProbability(0);
        assertEquals(0.596446726358423, normalApproximateProbability, 0.0001);
    }

    @Test
    public void testNormalApproximateProbability_MaxValue() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        double normalApproximateProbability = distribution.normalApproximateProbability(Integer.MAX_VALUE);
        assertEquals(1.0, normalApproximateProbability, 0.0001);
    }

    @Test
    public void testNormalApproximateProbability_Valid() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        double normalApproximateProbability = distribution.normalApproximateProbability(1);
        assertEquals(0.682689492137306, normalApproximateProbability, 0.0001);
    }

    @Test
    public void testSample_SmallMean() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        int sample = distribution.sample();
        assertTrue(sample >= 0 && sample <= 1000 * 0.5);
    }

    @Test
    public void testSample_LargeMean() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1000.0);
        int sample = distribution.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbability_Exception() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        distribution.cumulativeProbability(Integer.MIN_VALUE);
    }

}