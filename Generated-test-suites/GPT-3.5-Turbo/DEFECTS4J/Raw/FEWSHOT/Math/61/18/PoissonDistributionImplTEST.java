import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.MathException;
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        Assert.assertEquals(5.0, distribution.getMean(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMean() {
        new PoissonDistributionImpl(-1.0);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);

        Assert.assertEquals(0.049787068367863944, distribution.probability(0), 0.0001);
        Assert.assertEquals(0.14936120510359185, distribution.probability(1), 0.0001);
        Assert.assertEquals(0.22404180765538775, distribution.probability(2), 0.0001);
        Assert.assertEquals(0.22404180765538775, distribution.probability(3), 0.0001);
        Assert.assertEquals(0.1680313557415408, distribution.probability(4), 0.0001);
        Assert.assertEquals(0.10081881344492449, distribution.probability(5), 0.0001);
        Assert.assertEquals(0.050409406722462246, distribution.probability(6), 0.0001);
        Assert.assertEquals(0.0216040313096911, distribution.probability(7), 0.0001);
        Assert.assertEquals(0.00810151109622268, distribution.probability(8), 0.0001);
        Assert.assertEquals(0.002753077930567296, distribution.probability(9), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(4.0);

        Assert.assertEquals(0.01831563888873418, distribution.cumulativeProbability(0), 0.0001);
        Assert.assertEquals(0.09157819444367083, distribution.cumulativeProbability(1), 0.0001);
        Assert.assertEquals(0.23810330555354436, distribution.cumulativeProbability(2), 0.0001);
        Assert.assertEquals(0.4187553130323622, distribution.cumulativeProbability(3), 0.0001);
        Assert.assertEquals(0.6288365038747299, distribution.cumulativeProbability(4), 0.0001);
        Assert.assertEquals(0.8152632409236707, distribution.cumulativeProbability(5), 0.0001);
        Assert.assertEquals(0.9319066542894481, distribution.cumulativeProbability(6), 0.0001);
        Assert.assertEquals(0.9783484429996573, distribution.cumulativeProbability(7), 0.0001);
        Assert.assertEquals(0.995389828554038, distribution.cumulativeProbability(8), 0.0001);
        Assert.assertEquals(0.9991220383722734, distribution.cumulativeProbability(9), 0.0001);
        Assert.assertEquals(0.999919520529672, distribution.cumulativeProbability(10), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(4.0);

        Assert.assertEquals(0.06059784926654068, distribution.normalApproximateProbability(0), 0.0001);
        Assert.assertEquals(0.23752549236160587, distribution.normalApproximateProbability(1), 0.0001);
        Assert.assertEquals(0.46209812037329684, distribution.normalApproximateProbability(2), 0.0001);
        Assert.assertEquals(0.6703200460356393, distribution.normalApproximateProbability(3), 0.0001);
        Assert.assertEquals(0.8399943225356417, distribution.normalApproximateProbability(4), 0.0001);
        Assert.assertEquals(0.9532813584280713, distribution.normalApproximateProbability(5), 0.0001);
        Assert.assertEquals(0.9906271557182537, distribution.normalApproximateProbability(6), 0.0001);
        Assert.assertEquals(0.9979812567332073, distribution.normalApproximateProbability(7), 0.0001);
        Assert.assertEquals(0.9997447767173132, distribution.normalApproximateProbability(8), 0.0001);
        Assert.assertEquals(0.9999794687802138, distribution.normalApproximateProbability(9), 0.0001);
        Assert.assertEquals(0.9999980893435181, distribution.normalApproximateProbability(10), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        for (int i = 0; i < 1000; i++) {
            int sample = distribution.sample();
            Assert.assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        Assert.assertEquals(0, distribution.getDomainLowerBound(0.0), 0.0001);
        Assert.assertEquals(0, distribution.getDomainLowerBound(0.5), 0.0001);
        Assert.assertEquals(0, distribution.getDomainLowerBound(1.0), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        Assert.assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.0), 0.0001);
        Assert.assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5), 0.0001);
        Assert.assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(1.0), 0.0001);
    }

}