import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_constructor_meanPositive_standardDeviationPositive() {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        Assert.assertEquals(1.0, distribution.getMean(), 0.0);
        Assert.assertEquals(2.0, distribution.getStandardDeviation(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImpl_constructor_meanNegativeOrZero_standardDeviationPositive() {
        new NormalDistributionImpl(-1.0, 2.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImpl_constructor_meanPositive_standardDeviationZero() {
        new NormalDistributionImpl(1.0, 0.0);
    }

    @Test
    public void testNormalDistributionImpl_density() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        double result = distribution.density(0.5);
        Assert.assertEquals(0.15430806348152763, result, 0.0001);
    }

    @Test
    public void testNormalDistributionImpl_cumulativeProbability() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        double result = 0.0;
        try {
            result = distribution.cumulativeProbability(0.5);
        } catch (MathException e) {
            Assert.fail();
        }
        Assert.assertEquals(0.682689462137);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImpl_cumulativeProbability_valueOutOfRange() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.cumulativeProbability(2.0);
    }

    @Test
    public void testNormalDistributionImpl_inverseCumulativeProbability() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        double result = distribution.inverseCumulativeProbability(0.5);
        Assert.assertEquals(0.0, result, 0.001);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImpl_inverseCumulativeProbability_probabilityOutOfRange() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.inverseCumulativeProbability(1.1);
    }

    @Test
    public void testNormalDistributionImpl_sample() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        double result = distribution.sample();
        Assert.assertTrue(result >= -3.0 && result <= 3.0);
    }

    @Test
    public void testNormalDistributionImpl_getDomainLowerBound() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        double result = distribution.getDomainLowerBound(0.3);
        Assert.assertEquals(-1.64485362987, result, 0.001);
    }

    @Test
    public void testNormalDistributionImpl_getDomainUpperBound() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        double result = distribution.getDomainUpperBound(0.7);
        Assert.assertEquals(1.64485362987, result, 0.001);
    }

    @Test
    public void testNormalDistributionImpl_getInitialDomain() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        double result = distribution.getInitialDomain(0.6);
        Assert.assertEquals(-0.84162134465, result, 0.001);
    }
}