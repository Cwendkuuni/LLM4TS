import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.junit.Assert;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testFDistributionConstruction() throws NotStrictlyPositiveException {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        Assert.assertEquals(numeratorDegreesOfFreedom, fd.getNumeratorDegreesOfFreedom(), 0.0);
        Assert.assertEquals(denominatorDegreesOfFreedom, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(-1.0, 5.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(0.0, 5.0);
    }

    @Test
    public void testFDistributionDensity() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        double x = 1.0;
        double expectedDensity = 0.048026736;
        double actualDensity = fd.density(x);
        Assert.assertEquals(expectedDensity, actualDensity, 1e-9);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        double x = 1.0;
        double expectedCumulativeProbability = 0.156532436;
        double actualCumulativeProbability = fd.cumulativeProbability(x);
        Assert.assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 1e-9);
    }

    @Test
    public void testFDistributionInverseCumulativeProbability() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        double inverseCumulativeProbability = 0.5;
        double expectedValue = 1.40578884;
        double actualValue = fd.inverseCumulativeProbability(inverseCumulativeProbability);
        Assert.assertEquals(expectedValue, actualValue, 1e-9);
    }

    @Test
    public void testFDistributionVariance() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        double expectedVariance = 0.632120529;
        double actualVariance = fd.getNumericalVariance();
        Assert.assertEquals(expectedVariance, actualVariance, 1e-9);
    }

    @Test
    public void testFDistributionMean() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        double expectedMean = 1.5;
        double actualMean = fd.getNumericalMean();
        Assert.assertEquals(expectedMean, actualMean, 1e-9);
    }

}