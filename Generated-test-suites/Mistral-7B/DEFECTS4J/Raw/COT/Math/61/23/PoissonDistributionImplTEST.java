import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.special.Gamma;
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(2.5, dist.getMean(), 0.001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbabilityZero() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double prob = dist.probability(0);
        Assert.assertEquals(0.073565855355673, prob, 0.001);
    }

    @Test
    public void testProbabilityWithPositiveValue() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double prob = dist.probability(3);
        Assert.assertEquals(0.048812568521112, prob, 0.001);
    }

    @Test
    public void testProbabilityWithMaxValue() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        int maxValue = Integer.MAX_VALUE;
        double prob = dist.probability(maxValue);
        Assert.assertEquals(0.0, prob, 0.0);
    }

    @Test
    public void testCumulativeProbabilityZero() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double prob = dist.cumulativeProbability(0);
        Assert.assertEquals(0.0, prob, 0.0);
    }

    @Test
    public void testCumulativeProbabilityWithPositiveValue() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double prob = dist.cumulativeProbability(2);
        Assert.assertEquals(0.682688375585301, prob, 0.001);
    }

    @Test
    public void testCumulativeProbabilityWithMaxValue() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double prob = dist.cumulativeProbability(Integer.MAX_VALUE);
        Assert.assertEquals(1.0, prob, 0.0);
    }

    @Test
    public void testNormalApproximateProbabilityWithMinusOne() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double prob = dist.normalApproximateProbability(-1);
        Assert.assertEquals(0.0, prob, 0.0);
    }

    @Test
    public void testNormalApproximateProbabilityWithZero() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double prob = dist.normalApproximateProbability(0);
        Assert.assertEquals(0.073472649773623, prob, 0.001);
    }

    @Test
    public void testNormalApproximateProbabilityWithPositiveValue() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double prob = dist.normalApproximateProbability(3);
        Assert.assertEquals(0.048781659256367, prob, 0.001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        int sample = dist.sample();
        Assert.assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testInverseCumulativeProbabilityZero() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double prob = dist.cumulativeProbability(0);
        int expected = dist.inverseCumulativeProbability(0);
        Assert.assertEquals(0, expected);
    }

    @Test
    public void testInverseCumulativeProbabilityOne() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double prob = dist.cumulativeProbability(1);
        int expected = dist.inverseCumulativeProbability(prob);
        Assert.assertEquals(1, expected);
    }

    @Test
    public void testInverseCumulativeProbabilityWithProbabilityGreaterThanOne() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double prob = dist.cumulativeProbability(1.1);
        int expected = dist.inverseCumulativeProbability(prob);
        Assert.assertEquals(2, expected);
    }

    @Test
    public void testInverseCumulativeProbabilityWithProbabilityLessThanZero() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double prob = dist.cumulativeProbability(-1);
        Assert.assertThrows(MathException.class, () -> dist.inverseCumulativeProbability(prob));
    }
}