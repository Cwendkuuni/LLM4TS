import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstructorWithMeanAndStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl(0.5, 1.0);
        Assert.assertEquals(0.5, distribution.getMean(), 0.0);
        Assert.assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplConstructorWithNegativeStandardDeviation() {
        new NormalDistributionImpl(0.5, -1.0);
    }

    @Test
    public void testDensity() {
        NormalDistribution distribution = new NormalDistributionImpl(0.5, 1.0);
        double result = distribution.density(0.6);
        Assert.assertEquals(0.11263756042898955, result, 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistribution distribution = new NormalDistributionImpl(0.5, 1.0);
        double result;

        result = distribution.cumulativeProbability(0.1);
        Assert.assertEquals(0.15403023053491881, result, 0.0001);

        result = distribution.cumulativeProbability(0.5);
        Assert.assertEquals(0.5, result, 0.0001);

        result = distribution.cumulativeProbability(0.9);
        Assert.assertEquals(0.8413447461213452, result, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() {
        NormalDistribution distribution = new NormalDistributionImpl(0.5, 1.0);
        distribution.cumulativeProbability(2.0);
    }

    @Test
    public void testInverseCumulativeProbability() throws Exception {
        NormalDistribution distribution = new NormalDistributionImpl(0.5, 1.0);
        double result;

        result = distribution.inverseCumulativeProbability(0.15403023053491881);
        Assert.assertEquals(0.1, result, 0.0001);

        result = distribution.inverseCumulativeProbability(0.5);
        Assert.assertEquals(0.5, result, 0.0001);

        result = distribution.inverseCumulativeProbability(0.8413447461213452);
        Assert.assertEquals(0.9, result, 0.0001);

        result = distribution.inverseCumulativeProbability(1.0);
        Assert.assertEquals(Double.POSITIVE_INFINITY, result, 0.0);

        result = distribution.inverseCumulativeProbability(0.0);
        Assert.assertEquals(Double.NEGATIVE_INFINITY, result, 0.0);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutOfRange() throws Exception {
        NormalDistribution distribution = new NormalDistributionImpl(0.5, 1.0);
        distribution.inverseCumulativeProbability(1.1);
    }

    @Test
    public void testSample() throws Exception {
        NormalDistribution distribution = new NormalDistributionImpl(0.5, 1.0);
        double result;

        for (int i = 0; i < 100; i++) {
            result = distribution.sample();
            Assert.assertEquals(Double.class, result.getClass());
            Assert.assertTrue(result >= -3.0 && result <= 3.0);
        }
    }
}