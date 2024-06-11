import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.special.Erf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {

    private NormalDistributionImpl normalDistribution;

    @Before
    public void setup() {
        normalDistribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        Assert.assertEquals(0, normalDistribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        Assert.assertEquals(1, normalDistribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        Assert.assertEquals(0.398942, normalDistribution.density(0), 0.000001);
    }

    @Test
    public void testDensity_withNegativeInput() {
        Assert.assertEquals(0.241971, normalDistribution.density(-1), 0.000001);
    }

    @Test
    public void testDensity_withPositiveInput() {
        Assert.assertEquals(0.241971, normalDistribution.density(1), 0.000001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        Assert.assertEquals(0.5, normalDistribution.cumulativeProbability(0), 0.000001);
    }

    @Test
    public void testCumulativeProbability_withNegativeInput() throws MathException {
        Assert.assertEquals(0.158655, normalDistribution.cumulativeProbability(-1), 0.000001);
    }

    @Test
    public void testCumulativeProbability_withPositiveInput() throws MathException {
        Assert.assertEquals(0.841345, normalDistribution.cumulativeProbability(1), 0.000001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbability_withInvalidInput() throws MathException {
        normalDistribution.cumulativeProbability(Double.NaN);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        Assert.assertEquals(0, normalDistribution.inverseCumulativeProbability(0), 0.000001);
    }

    @Test
    public void testInverseCumulativeProbability_withInvalidInput() {
        Assert.assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0), 0.000001);
        Assert.assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1), 0.000001);
    }

    @Test
    public void testSample() throws MathException {
        double sample = normalDistribution.sample();
        Assert.assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        Assert.assertEquals(1e-9, normalDistribution.getSolverAbsoluteAccuracy(), 0);
    }

    @Test
    public void testGetDomainLowerBound() {
        Assert.assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.4), 0);
        Assert.assertEquals(0, normalDistribution.getDomainLowerBound(0.6), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        Assert.assertEquals(0, normalDistribution.getDomainUpperBound(0.4), 0);
        Assert.assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.6), 0);
    }

    @Test
    public void testGetInitialDomain() {
        Assert.assertEquals(-1, normalDistribution.getInitialDomain(0.4), 0);
        Assert.assertEquals(1, normalDistribution.getInitialDomain(0.6), 0);
    }
}