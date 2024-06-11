import org.junit.Assert;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(5, 10);
        double density = fd.density(2);
        Assert.assertEquals(0.07621044026904963, density, 0.00001);

        fd = new FDistribution(10, 5);
        density = fd.density(2);
        Assert.assertEquals(0.296958754884325, density, 0.00001);

        fd = new FDistribution(20, 30);
        density = fd.density(2);
        Assert.assertEquals(0.06131079318190443, density, 0.00001);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(5, 10);
        double cumProb = fd.cumulativeProbability(2);
        Assert.assertEquals(0.8962861266440091, cumProb, 0.00001);

        fd = new FDistribution(10, 5);
        cumProb = fd.cumulativeProbability(2);
        Assert.assertEquals(0.7030418796913137, cumProb, 0.00001);

        fd = new FDistribution(20, 30);
        cumProb = fd.cumulativeProbability(2);
        Assert.assertEquals(0.8337382253486225, cumProb, 0.00001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(5, 10);
        double numeratorDF = fd.getNumeratorDegreesOfFreedom();
        Assert.assertEquals(5, numeratorDF, 0);

        fd = new FDistribution(10, 5);
        numeratorDF = fd.getNumeratorDegreesOfFreedom();
        Assert.assertEquals(10, numeratorDF, 0);

        fd = new FDistribution(20, 30);
        numeratorDF = fd.getNumeratorDegreesOfFreedom();
        Assert.assertEquals(20, numeratorDF, 0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(5, 10);
        double denominatorDF = fd.getDenominatorDegreesOfFreedom();
        Assert.assertEquals(10, denominatorDF, 0);

        fd = new FDistribution(10, 5);
        denominatorDF = fd.getDenominatorDegreesOfFreedom();
        Assert.assertEquals(5, denominatorDF, 0);

        fd = new FDistribution(20, 30);
        denominatorDF = fd.getDenominatorDegreesOfFreedom();
        Assert.assertEquals(30, denominatorDF, 0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fd = new FDistribution(5, 10);
        double numericalMean = fd.getNumericalMean();
        Assert.assertEquals(1.25, numericalMean, 0);

        fd = new FDistribution(10, 5);
        numericalMean = fd.getNumericalMean();
        Assert.assertEquals(Double.NaN, numericalMean, 0);

        fd = new FDistribution(20, 30);
        numericalMean = fd.getNumericalMean();
        Assert.assertEquals(30/28, numericalMean, 0);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fd = new FDistribution(5, 10);
        double numericalVariance = fd.getNumericalVariance();
        Assert.assertEquals(Double.NaN, numericalVariance, 0);

        fd = new FDistribution(10, 5);
        numericalVariance = fd.getNumericalVariance();
        Assert.assertEquals(Double.NaN, numericalVariance, 0);

        fd = new FDistribution(20, 30);
        numericalVariance = fd.getNumericalVariance();
        Assert.assertEquals(0.75, numericalVariance, 0);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fd = new FDistribution(5, 10);
        double supportLowerBound = fd.getSupportLowerBound();
        Assert.assertEquals(0, supportLowerBound, 0);

        fd = new FDistribution(10, 5);
        supportLowerBound = fd.getSupportLowerBound();
        Assert.assertEquals(0, supportLowerBound, 0);

        fd = new FDistribution(20, 30);
        supportLowerBound = fd.getSupportLowerBound();
        Assert.assertEquals(0, supportLowerBound, 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fd = new FDistribution(5, 10);
        double supportUpperBound = fd.getSupportUpperBound();
        Assert.assertEquals(Double.POSITIVE_INFINITY, supportUpperBound, 0);

        fd = new FDistribution(10, 5);
        supportUpperBound = fd.getSupportUpperBound();
        Assert.assertEquals(Double.POSITIVE_INFINITY, supportUpperBound, 0);

        fd = new FDistribution(20, 30);
        supportUpperBound = fd.getSupportUpperBound();
        Assert.assertEquals(Double.POSITIVE_INFINITY, supportUpperBound, 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fd = new FDistribution(5, 10);
        boolean supportLowerBoundInclusive = fd.isSupportLowerBoundInclusive();
        Assert.assertEquals(true, supportLowerBoundInclusive);

        fd = new FDistribution(10, 5);
        supportLowerBoundInclusive = fd.isSupportLowerBoundInclusive();
        Assert.assertEquals(true, supportLowerBoundInclusive);

        fd = new FDistribution(20, 30);
        supportLowerBoundInclusive = fd.isSupportLowerBoundInclusive();
        Assert.assertEquals(true, supportLowerBoundInclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fd = new FDistribution(5, 10);
        boolean supportUpperBoundInclusive = fd.isSupportUpperBoundInclusive();
        Assert.assertEquals(false, supportUpperBoundInclusive);

        fd = new FDistribution(10, 5);
        supportUpperBoundInclusive = fd.isSupportUpperBoundInclusive();
        Assert.assertEquals(false, supportUpperBoundInclusive);

        fd = new FDistribution(20, 30);
        supportUpperBoundInclusive = fd.isSupportUpperBoundInclusive();
        Assert.assertEquals(false, supportUpperBoundInclusive);
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fd = new FDistribution(5, 10);
        boolean supportConnected = fd.isSupportConnected();
        Assert.assertEquals(true, supportConnected);

        fd = new FDistribution(10, 5);
        supportConnected = fd.isSupportConnected();
        Assert.assertEquals(true, supportConnected);

        fd = new FDistribution(20, 30);
        supportConnected = fd.isSupportConnected();
        Assert.assertEquals(true, supportConnected);
    }
}