import org.junit.Assert;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 3.0);
        double cumulativeProbability = fDistribution.cumulativeProbability(2.0);
        Assert.assertEquals(0.646, cumulativeProbability, 0.001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 3.0);
        double inverseCumulativeProbability = fDistribution.inverseCumulativeProbability(0.646);
        Assert.assertEquals(2.0, inverseCumulativeProbability, 0.001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 3.0);
        double domainLowerBound = fDistribution.getDomainLowerBound(0.5);
        Assert.assertEquals(0.0, domainLowerBound, 0.001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 3.0);
        double domainUpperBound = fDistribution.getDomainUpperBound(0.5);
        Assert.assertEquals(Double.MAX_VALUE, domainUpperBound, 0.001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 3.0);
        double initialDomain = fDistribution.getInitialDomain(0.5);
        Assert.assertEquals(1.5, initialDomain, 0.001);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 3.0);
        fDistribution.setNumeratorDegreesOfFreedom(10.0);
        Assert.assertEquals(10.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 3.0);
        double numeratorDegreesOfFreedom = fDistribution.getNumeratorDegreesOfFreedom();
        Assert.assertEquals(5.0, numeratorDegreesOfFreedom, 0.001);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 3.0);
        fDistribution.setDenominatorDegreesOfFreedom(6.0);
        Assert.assertEquals(6.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.001);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 3.0);
        double denominatorDegreesOfFreedom = fDistribution.getDenominatorDegreesOfFreedom();
        Assert.assertEquals(3.0, denominatorDegreesOfFreedom, 0.001);
    }

}