import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {
    private NormalDistribution distribution;

    @Before
    public void setUp() {
        distribution = new NormalDistributionImpl(0, 1);
    }

    @Test
    public void testDefaultConstructor() {
        NormalDistribution impl = new NormalDistributionImpl();
        Assert.assertEquals(0.0, impl.getMean(), 0.0);
        Assert.assertEquals(1.0, impl.getStandardDeviation(), 0.0);
    }

    @Test
    public void testConstructors() {
        NormalDistribution impl1 = new NormalDistributionImpl(1.0, 2.0);
        Assert.assertEquals(1.0, impl1.getMean(), 0.0);
        Assert.assertEquals(2.0, impl1.getStandardDeviation(), 0.0);

        NormalDistribution impl2 = new NormalDistributionImpl(1.0, 2.0, 0.001);
        Assert.assertEquals(1.0, impl2.getMean(), 0.0);
        Assert.assertEquals(2.0, impl2.getStandardDeviation(), 0.0);
        Assert.assertEquals(0.001, impl2.getSolverAbsoluteAccuracy(), 0.0);

        try {
            new NormalDistributionImpl(-1.0, 2.0);
            Assert.fail();
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testGetMean() {
        Assert.assertEquals(0.0, distribution.getMean(), 0.0);
    }

    @Test
    public void testGetStandardDeviation() {
        Assert.assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testDensity() {
        for (double x = -10.0; x <= 10.0; x += 0.1) {
            double density = distribution.density(x);
            double expected = FastMath.exp(-0.5 * Math.pow(x, 2)) / (1.0 * Math.sqrt(2 * Math.PI));
            Assert.assertEquals(expected, density, 0.001);
        }
    }

    @Test
    public void testCumulativeProbability() {
        for (double x = -10.0; x <= 10.0; x += 0.1) {
            double p = distribution.cumulativeProbability(x);
            double expected = 0.5 * (1.0 + Erf.erf(Math.abs(x) / (1.0 * Math.sqrt(2.0))));
            Assert.assertEquals(expected, p, 0.001);
        }
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() {
        double x = Double.NEGATIVE_INFINITY;
        distribution.cumulativeProbability(x);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        Assert.assertEquals(0.001, distribution.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testInverseCumulativeProbability() {
        double p = 0.5;
        Assert.assertEquals(0.0, distribution.inverseCumulativeProbability(p), 0.0);

        p = 0.95;
        Assert.assertEquals(1.644853626951624,
                distribution.inverseCumulativeProbability(p), 0.001);

        p = 0.05;
        Assert.assertEquals(-1.644853626951624,
                distribution.inverseCumulativeProbability(p), 0.001);

        p = 0.0;
        Assert.assertEquals(Double.NEGATIVE_INFINITY,
                distribution.inverseCumulativeProbability(p));

        p = 1.0;
        Assert.assertEquals(Double.POSITIVE_INFINITY,
                distribution.inverseCumulativeProbability(p));
    }

    @Test(expected = MathException.class)
    public void testSampleOutOfRange() {
        distribution.sample();
    }

    @Test
    public void testSample() {
        for (int i = 0; i < 10000; i++) {
            double x = distribution.sample();
            double mean = distribution.getMean();
            double sd = distribution.getStandardDeviation();
            Assert.assertFalse(x < mean - 3 * sd);
            Assert.assertFalse(x > mean + 3 * sd);
        }
    }
}