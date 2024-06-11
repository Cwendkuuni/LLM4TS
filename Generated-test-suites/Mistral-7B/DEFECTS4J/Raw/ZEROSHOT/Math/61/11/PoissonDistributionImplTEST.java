import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.descript.moment.Moment;
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testPoissonDistributionCreation() {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(2.5, dist.getMean(), 0.001);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testPoissonDistributionWithNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbability() {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        double probability = dist.probability(1);
        Assert.assertEquals(0.2231407447636485, probability, 0.0001);
        probability = dist.probability(3);
        Assert.assertEquals(0.2251374922329313, probability, 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        double cumulativeProbability = dist.cumulativeProbability(0);
        Assert.assertEquals(0.0, cumulativeProbability, 0.001);
        cumulativeProbability = dist.cumulativeProbability(1);
        Assert.assertEquals(0.6826894711457521, cumulativeProbability, 0.001);
        cumulativeProbability = dist.cumulativeProbability(5);
        Assert.assertEquals(0.9993332487142388, cumulativeProbability, 0.001);
    }

    @Test
    public void testNormalApproximateProbability() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        double normalApproximateProbability = dist.normalApproximateProbability(1);
        Assert.assertEquals(0.2231407447636485, normalApproximateProbability, 0.001);
        normalApproximateProbability = dist.normalApproximateProbability(3);
        Assert.assertEquals(0.2251374922329313, normalApproximateProbability, 0.001);
    }

    @Test
    public void testSample() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        int sample = dist.sample();
        Assert.assertEquals(2, sample);
        sample = dist.sample();
        Assert.assertEquals(3, sample);
    }

    @Test
    public void testMoments() {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        Moment moment = dist.moment(Moment.MEAN);
        Assert.assertEquals(2.5, moment.getValue(), 0.001);
        moment = dist.moment(Moment.VARIANCE);
        Assert.assertEquals(2.5, moment.getValue(), 0.001);
        moment = dist.moment(Moment.SKEWNESS);
        Assert.assertEquals(0.0, moment.getValue(), 0.001);
        moment = dist.moment(Moment.KURTOSIS);
        Assert.assertEquals(3.0, moment.getValue(), 0.001);
    }
}