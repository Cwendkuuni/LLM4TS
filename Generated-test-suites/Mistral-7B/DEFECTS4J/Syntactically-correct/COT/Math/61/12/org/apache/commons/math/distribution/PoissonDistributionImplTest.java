import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, dist.getMean(), 0.001); // check mean
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbabilityZero() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        double prob = dist.probability(0);
        assertNotNull(prob);
        assertEquals(0.270481238452, prob, 0.000001);
    }

    @Test
    public void testProbabilityPositive() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        double prob = dist.probability(1);
        assertNotNull(prob);
        assertEquals(0.367879441145, prob, 0.000001);
    }

    @Test
    public void testProbabilityMaxValue() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        double prob = dist.probability(Integer.MAX_VALUE);
        assertEquals(0, prob, 0.000001);
    }

    @Test
    public void testCumulativeProbabilityZero() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        double prob = 0;
        try {
            prob = dist.cumulativeProbability(0);
        } catch (MathException e) {
            fail("Exception should not be thrown for zero");
        }
        assertEquals(0.0, prob, 0.000001);
    }

    @Test
    public void testCumulativeProbabilityPositive() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        double prob = 0;
        try {
            prob = dist.cumulativeProbability(1);
        } catch (MathException e) {
            fail("Exception should not be thrown for positive");
        }
        assertEquals(0.367879441145, prob, 0.000001);
    }

    @Test
    public void testCumulativeProbabilityMaxValue() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        double prob = 1;
        try {
            prob = dist.cumulativeProbability(Integer.MAX_VALUE);
        } catch (MathException e) {
            fail("Exception should not be thrown for max value");
        }
        assertEquals(1.0, prob, 0.000001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegative() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        dist.cumulativeProbability(-1);
    }

    @Test
    public void testNormalApproximateProbabilityZero() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        double prob = dist.normalApproximateProbability(0);
        assertNotNull(prob);
        assertEquals(0.367879441145, prob, 0.000001);
    }

    @Test
    public void testNormalApproximateProbabilityPositive() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        double prob = dist.normalApproximateProbability(1);
        assertNotNull(prob);
        assertEquals(0.367879441145, prob, 0.000001);
    }

    @Test
    public void testSampleLessThanMean() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        int sample = dist.sample();
        assertLessThan(sample, 1000 * 2.5);
    }

    @Test
    public void testSampleEqualToMean() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        int sample = dist.sample();
        assertEquals(1000 * 2.5, sample);
    }

    @Test
    public void testSampleGreaterThanMean() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        int sample = dist.sample();
        assertGreaterThan(sample, 1000 * 2.5);
    }

    @Test
    public void testInverseCumulativeProbabilitySmallP() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        int result = dist.inverseCumulativeProbability(0.01);
        assertEquals(0, result);
    }

    @Test
    public void testInverseCumulativeProbabilityMediumP() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        int result = dist.inverseCumulativeProbability(0.5);
        assertEquals(1, result);
    }

    @Test
    public void testInverseCumulativeProbabilityLargeP() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        int result = dist.inverseCumulativeProbability(0.99);
        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutsideRange() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        dist.inverseCumulativeProbability(-0.1);
    }
}