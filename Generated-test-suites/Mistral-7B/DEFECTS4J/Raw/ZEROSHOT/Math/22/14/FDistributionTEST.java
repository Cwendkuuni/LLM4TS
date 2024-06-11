import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Assert;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testFDistributionConstruction() throws NotStrictlyPositiveException {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        RealDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        Assert.assertEquals(numeratorDegreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        Assert.assertEquals(denominatorDegreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testFDistributionDensity() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double x = 1.0;
        double expectedDensity = 0.016484821169198275;
        Assert.assertEquals(expectedDensity, distribution.density(x), 0.0001);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double x = 1.0;
        double expectedCumulativeProbability = 0.15662198208191011;
        Assert.assertEquals(expectedCumulativeProbability, distribution.cumulativeProbability(x), 0.0001);
    }

    @Test
    public void testFDistributionNegativeParameters() {
        Assert.assertThrows(NotStrictlyPositiveException.class, () -> {
            new FDistribution(-1.0, 2.0);
        });
        Assert.assertThrows(NotStrictlyPositiveException.class, () -> {
            new FDistribution(2.0, -1.0);
        });
    }

    @Test
    public void testFDistributionGetNumericalMean() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double expectedMean = 1.5;
        Assert.assertEquals(expectedMean, distribution.getNumericalMean(), 0.001);
    }

    @Test
    public void testFDistributionGetNumericalVariance() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double expectedVariance = 2.449352316236521;
        Assert.assertEquals(expectedVariance, distribution.getNumericalVariance(), 0.001);
    }

    @Test
    public void testFDistributionSupportBounds() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        Assert.assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
        Assert.assertTrue(Double.isPositiveInfinite(distribution.getSupportUpperBound()));
    }

    @Test
    public void testFDistributionSupportBoundsInclusive() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        Assert.assertTrue(distribution.isSupportLowerBoundInclusive());
        Assert.assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testFDistributionSupportConnected() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        Assert.assertTrue(distribution.isSupportConnected());
    }
}