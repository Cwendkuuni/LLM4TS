import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.util.LocalizedFormats;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(3.5);
        Assert.assertEquals(3.5, poisson.getMean(), 0.0001);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.0);

        // Typical use case
        Assert.assertEquals(0.2706705664732254, poisson.probability(2), 0.0001);

        // Edge cases
        Assert.assertEquals(0.049787068367863944, poisson.probability(0), 0.0001);
        Assert.assertEquals(7.389056098930649E-4, poisson.probability(5), 0.0001);

        // Error scenarios
        Assert.assertEquals(0.0, poisson.probability(-1), 0.0001);
        Assert.assertEquals(0.0, poisson.probability(Integer.MAX_VALUE), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);

        // Typical use case
        Assert.assertEquals(0.9775855164714905, poisson.cumulativeProbability(3), 0.0001);

        // Edge cases
        Assert.assertEquals(0.08030139707139407, poisson.cumulativeProbability(0), 0.0001);
        Assert.assertEquals(0.1572992070502865, poisson.cumulativeProbability(2), 0.0001);

        // Error scenarios
        Assert.assertEquals(0.0, poisson.cumulativeProbability(-1), 0.0001);
        Assert.assertEquals(1.0, poisson.cumulativeProbability(Integer.MAX_VALUE), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(5.0);

        // Typical use case
        Assert.assertEquals(0.3789551378774387, poisson.normalApproximateProbability(4), 0.0001);

        // Edge cases
        Assert.assertEquals(0.5331705815149133, poisson.normalApproximateProbability(5), 0.0001);
        Assert.assertEquals(0.7638940078157809, poisson.normalApproximateProbability(7), 0.0001);

        // Error scenarios
        Assert.assertEquals(0.0, poisson.normalApproximateProbability(-1), 0.0001);
        Assert.assertEquals(1.0, poisson.normalApproximateProbability(Integer.MAX_VALUE), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(3.0);

        // Generate 100 samples and verify if they are within range
        for (int i = 0; i < 100; i++) {
            int sample = poisson.sample();
            Assert.assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(4.0);
        Assert.assertEquals(0, poisson.getDomainLowerBound(0.5), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(4.0);
        Assert.assertEquals(Integer.MAX_VALUE, poisson.getDomainUpperBound(0.5), 0.0001);
    }
}