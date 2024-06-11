import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(2.5, distribution.getMean(), 0.0);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbabilityZero() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        double probability = distribution.probability(0);
        Assert.assertEquals(0.27066026534264067, probability, 1e-6);
    }

    @Test
    public void testProbabilityPositive() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        double probability = distribution.probability(1);
        Assert.assertEquals(0.36792433402487358, probability, 1e-6);
    }

    @Test
    public void testProbabilityBigPositive() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(10.0);
        double probability = distribution.probability(5);
        Assert.assertEquals(0.3055273867364956, probability, 1e-6);
    }

    @Test
    public void testCumulativeProbabilityZero() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        double cumulativeProbability = 0.0;
        try {
            cumulativeProbability = distribution.cumulativeProbability(0);
        } catch (MathException e) {
            Assert.fail("Exception should not be thrown for cumulativeProbability(0)");
        }
        Assert.assertEquals(0.0, cumulativeProbability, 1e-6);
    }

    @Test
    public void testCumulativeProbabilityPositive() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        double cumulativeProbability = 0.0;
        try {
            cumulativeProbability = distribution.cumulativeProbability(1);
        } catch (MathException e) {
            Assert.fail("Exception should not be thrown for cumulativeProbability(1)");
        }
        Assert.assertEquals(0.36792433402487358, cumulativeProbability, 1e-6);
    }

    @Test
    public void testCumulativeProbabilityBigPositive() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(10.0);
        double cumulativeProbability = 0.0;
        try {
            cumulativeProbability = distribution.cumulativeProbability(5);
        } catch (MathException e) {
            Assert.fail("Exception should not be thrown for cumulativeProbability(5)");
        }
        Assert.assertEquals(0.8083484752932393, cumulativeProbability, 1e-6);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityBeyondMaxValue() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        distribution.cumulativeProbability(Integer.MAX_VALUE);
    }

    @Test
    public void testNormalApproximateProbabilityZero() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        double probability = distribution.normalApproximateProbability(0);
        Assert.assertEquals(0.22313373493177848, probability, 1e-6);
    }

    @Test
    public void testNormalApproximateProbabilityPositive() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0.5);
        double probability = distribution.normalApproximateProbability(1);
        Assert.assertEquals(0.3177189353333616, probability, 1e-6);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        int sample = distribution.sample();
        Assert.assertEquals(5, sample);
    }

    @Test
    public void testSampleNegativeMean() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(-1.0);
        int sample = distribution.sample();
        Assert.assertEquals(0, sample);
    }

    @Test
    public void testInverseCumulativeProbabilityZero() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        double inverseCumulativeProbability = 0.0;
        try {
            inverseCumulativeProbability = distribution.inverseCumulativeProbability(0.0);
        } catch (MathException e) {
            Assert.fail("Exception should not be thrown for inverseCumulativeProbability(0.0)");
        }
        Assert.assertEquals(0, inverseCumulativeProbability);
    }

    @Test
    public void testInverseCumulativeProbabilityOne() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        double inverseCumulativeProbability = 0.0;
        try {
            inverseCumulativeProbability = distribution.inverseCumulativeProbability(1.0);
        } catch (MathException e) {
            Assert.fail("Exception should not be thrown for inverseCumulativeProbability(1.0)");
        }
        Assert.assertEquals(1, inverseCumulativeProbability);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityBeyondRange() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0);
        distribution.inverseCumulativeProbability(1.5);
    }
}