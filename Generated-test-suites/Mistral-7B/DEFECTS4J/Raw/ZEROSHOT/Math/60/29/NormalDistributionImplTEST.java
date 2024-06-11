import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstruction() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(0.0, distribution.getMean(), 0.0);
        Assert.assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
        Assert.assertFalse(distribution.isSupportPoint(Double.NaN));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplNegativeStandardDeviation() {
        new NormalDistributionImpl(-1.0, 1.0);
    }

    @Test
    public void testNormalDistributionImplMeanAndStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl(1.5, 2.5);
        Assert.assertEquals(1.5, distribution.getMean(), 0.0);
        Assert.assertEquals(2.5, distribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testNormalDistributionImplDenisty() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double density = distribution.density(0.5);
        Assert.assertEquals(0.1543080189592662, density, 0.00001);
    }

    @Test
    public void testNormalDistributionImplCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double cumulativeProbability = distribution.cumulativeProbability(0.5);
        Assert.assertEquals(0.6826894921373184, cumulativeProbability, 0.00001);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImplCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        distribution.cumulativeProbability(3.0);
    }

    @Test
    public void testNormalDistributionImplInverseCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double inverseCumulativeProbability = distribution.inverseCumulativeProbability(0.5);
        Assert.assertEquals(0.4763594083721725, inverseCumulativeProbability, 0.00001);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImplInverseCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        distribution.inverseCumulativeProbability(-0.1);
    }

    @Test
    public void testNormalDistributionImplSample() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double sample = distribution.sample();
        Assert.assertFalse(Double.isNaN(sample));
        Assert.assertEquals(0.0, sample, 0.01);
    }

    @Test
    public void testNormalDistributionImplSampleMultipleTimes() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        for (int i = 0; i < 100; i++) {
            double sample = distribution.sample();
            Assert.assertFalse(Double.isNaN(sample));
            Assert.assertEquals(0.0, sample, 0.01);
        }
    }

    @Test
    public void testNormalDistributionImplSampleNegativeStandardDeviation() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, -1.0);
        double sample = distribution.sample();
        Assert.assertEquals(-1.0, sample, 0.01);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testNormalDistributionImplInverseCumulativeProbabilityMaxIterationsExceeded() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        distribution.inverseCumulativeProbability(1.5);
    }
}