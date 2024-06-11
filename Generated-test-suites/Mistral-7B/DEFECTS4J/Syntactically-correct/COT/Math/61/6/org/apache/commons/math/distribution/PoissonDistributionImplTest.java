import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PoissonDistributionImplTest {

    PoissonDistributionImpl dist;

    @Before
    public void setup() {
        dist = new PoissonDistributionImpl(5.0);
    }

    @Test
    public void testConstructorWithPositiveMean() {
        Assert.assertEquals(5.0, dist.getMean(), Precision.SAFE_EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-1.0);
    }

    @Test
    public void testProbabilityZero() {
        double prob = dist.probability(0);
        Assert.assertEquals(Math.exp(-dist.getMean()), prob, Precision.SAFE_EPSILON);
    }

    @Test
    public void testProbabilityPositiveLessThanMax() {
        double prob = dist.probability(2);
        double normalProb = dist.normalApproximateProbability(2);
        Assert.assertEquals(prob, normalProb, Precision.SAFE_EPSILON);
    }

    @Test
    public void testProbabilityMaxValue() {
        double prob = dist.probability(Integer.MAX_VALUE);
        double normalProb = dist.normalApproximateProbability(Integer.MAX_VALUE);
        Assert.assertEquals(1.0, prob, Precision.SAFE_EPSILON);
        Assert.assertEquals(1.0, normalProb, Precision.SAFE_EPSILON);
    }

    @Test
    public void testProbabilityNegativeAndMaxValue() {
        double probZero = dist.probability(0);
        double probNegative = dist.probability(-1);
        Assert.assertEquals(probZero, 0.0, Precision.SAFE_EPSILON);
        Assert.assertEquals(probNegative, 0.0, Precision.SAFE_EPSILON);
    }

    @Test
    public void testCumulativeProbabilityZero() throws MathException {
        double prob = dist.cumulativeProbability(0);
        Assert.assertEquals(0.0, prob, Precision.SAFE_EPSILON);
    }

    @Test
    public void testCumulativeProbabilityPositive() throws MathException {
        double prob = dist.cumulativeProbability(1);
        double normalProb = dist.normalApproximateCumulativeProbability(1);
        Assert.assertEquals(prob, normalProb, Precision.SAFE_EPSILON);
    }

    @Test
    public void testCumulativeProbabilityMaxValue() throws MathException {
        double prob = dist.cumulativeProbability(Integer.MAX_VALUE);
        Assert.assertEquals(1.0, prob, Precision.SAFE_EPSILON);
    }

    @Test
    public void testCumulativeProbabilityNegative() throws MathException {
        double prob = dist.cumulativeProbability(-1);
        Assert.assertEquals(0.0, prob, Precision.SAFE_EPSILON);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithException() throws MathException {
        dist.cumulativeProbability(Integer.MAX_VALUE + 1);
    }

    @Test
    public void testSample() throws MathException {
        int sample = dist.sample();
        int expected = (int) Math.round(dist.normalApproximateProbability(sample));
        Assert.assertEquals(expected, sample);
    }

    @Test
    public void testSampleWithRejectionAlgorithm() throws MathException {
        PoissonDistributionImpl distLargeMean = new PoissonDistributionImpl(1000.0);
        int sample = distLargeMean.sample();
        int expected = sample;
        Assert.assertEquals(expected, sample);
    }

    @Test(expected = MathException.class)
    public void testSampleWithException() throws MathException {
        PoissonDistributionImpl distNegativeMean = new PoissonDistributionImpl(-1.0);
        distNegativeMean.sample();
    }
}