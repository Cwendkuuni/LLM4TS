import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.special.Gamma;
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithValidMean() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(5.0);
        Assert.assertEquals(5.0, poissonDistribution.getMean(), 0.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithInvalidMean() {
        new PoissonDistributionImpl(-1.0);
    }

    @Test
    public void testProbabilityMethod() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        Assert.assertEquals(0.0697, poissonDistribution.probability(0), 0.001);
        Assert.assertEquals(0.1133, poissonDistribution.probability(1), 0.001);
        Assert.assertEquals(0.2853, poissonDistribution.probability(2), 0.001);
        Assert.assertEquals(0.2853, poissonDistribution.probability(3), 0.001);
        Assert.assertEquals(0.1983, poissonDistribution.probability(4), 0.001);
        Assert.assertEquals(0.1133, poissonDistribution.probability(5), 0.001);
        Assert.assertEquals(0.0697, poissonDistribution.probability(6), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityMethodWithInvalidArgument() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        poissonDistribution.probability(-1);
    }

    @Test
    public void testCumulativeProbabilityMethod() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        Assert.assertEquals(0.0, poissonDistribution.cumulativeProbability(Integer.MIN_VALUE), 0.0);
        Assert.assertEquals(0.0697, poissonDistribution.cumulativeProbability(0), 0.001);
        Assert.assertEquals(0.1393, poissonDistribution.cumulativeProbability(1), 0.001);
        Assert.assertEquals(0.2486, poissonDistribution.cumulativeProbability(2), 0.001);
        Assert.assertEquals(0.4179, poissonDistribution.cumulativeProbability(3), 0.001);
        Assert.assertEquals(0.5925, poissonDistribution.cumulativeProbability(4), 0.001);
        Assert.assertEquals(0.7705, poissonDistribution.cumulativeProbability(5), 0.001);
        Assert.assertEquals(0.8912, poissonDistribution.cumulativeProbability(6), 0.001);
        Assert.assertEquals(1.0, poissonDistribution.cumulativeProbability(Integer.MAX_VALUE), 0.001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testCumulativeProbabilityMethodWithMathException() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        poissonDistribution.cumulativeProbability(Integer.MAX_VALUE + 1);
    }

    @Test
    public void testNormalApproximateProbabilityMethod() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        Assert.assertEquals(0.0697, poissonDistribution.normalApproximateProbability(0), 0.001);
        Assert.assertEquals(0.1133, poissonDistribution.normalApproximateProbability(1), 0.001);
        Assert.assertEquals(0.1983, poissonDistribution.normalApproximateProbability(2), 0.001);
        Assert.assertEquals(0.2853, poissonDistribution.normalApproximateProbability(3), 0.001);
        Assert.assertEquals(0.2853, poissonDistribution.normalApproximateProbability(4), 0.001);
        Assert.assertEquals(0.1133, poissonDistribution.normalApproximateProbability(5), 0.001);
        Assert.assertEquals(0.0697, poissonDistribution.normalApproximateProbability(6), 0.001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testSampleMethodWithMathException() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        poissonDistribution.sample(); // normalApproximateProbability method used internally calls it
    }
}