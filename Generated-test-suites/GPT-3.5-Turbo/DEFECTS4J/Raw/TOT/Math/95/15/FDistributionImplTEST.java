import org.apache.commons.math.MathException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FDistributionImplTest {

    private FDistributionImpl fDistribution;

    @Before
    public void setUp() {
        fDistribution = new FDistributionImpl(5, 10);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        // Typical use case
        double cumulativeProbability = fDistribution.cumulativeProbability(1);
        Assert.assertEquals(0.3639244357057702, cumulativeProbability, 0.000001);

        // Edge case
        cumulativeProbability = fDistribution.cumulativeProbability(0);
        Assert.assertEquals(0, cumulativeProbability, 0);

        // Error scenario
        cumulativeProbability = fDistribution.cumulativeProbability(-1);
        Assert.assertEquals(0, cumulativeProbability, 0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        // Typical use case
        double inverseCumulativeProbability = fDistribution.inverseCumulativeProbability(0.5);
        Assert.assertEquals(0.9688555571635616, inverseCumulativeProbability, 0.000001);

        // Edge case
        inverseCumulativeProbability = fDistribution.inverseCumulativeProbability(0);
        Assert.assertEquals(0, inverseCumulativeProbability, 0);

        // Error scenario
        inverseCumulativeProbability = fDistribution.inverseCumulativeProbability(1.1);
        Assert.assertEquals(Double.POSITIVE_INFINITY, inverseCumulativeProbability, 0);
    }

    @Test
    public void testGetDomainLowerBound() {
        // Typical use case
        double domainLowerBound = fDistribution.getDomainLowerBound(0.3);
        Assert.assertEquals(0.0, domainLowerBound, 0);

        // Edge case
        domainLowerBound = fDistribution.getDomainLowerBound(0);
        Assert.assertEquals(0.0, domainLowerBound, 0);

        // Error scenario
        domainLowerBound = fDistribution.getDomainLowerBound(1);
        Assert.assertEquals(0.0, domainLowerBound, 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        // Typical use case
        double domainUpperBound = fDistribution.getDomainUpperBound(0.3);
        Assert.assertEquals(Double.MAX_VALUE, domainUpperBound, 0);

        // Edge case
        domainUpperBound = fDistribution.getDomainUpperBound(0.0);
        Assert.assertEquals(Double.MAX_VALUE, domainUpperBound, 0);

        // Error scenario
        domainUpperBound = fDistribution.getDomainUpperBound(1.0);
        Assert.assertEquals(Double.MAX_VALUE, domainUpperBound, 0);
    }

    @Test
    public void testGetInitialDomain() {
        // Typical use case
        double initialDomain = fDistribution.getInitialDomain(0.3);
        Assert.assertEquals(0.6666666666666666, initialDomain, 0.000001);

        // Edge case
        initialDomain = fDistribution.getInitialDomain(0);
        Assert.assertEquals(5.0, initialDomain, 0);

        // Error scenario
        initialDomain = fDistribution.getInitialDomain(1);
        Assert.assertEquals(5.0, initialDomain, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomWithNegativeValue() {
        fDistribution.setNumeratorDegreesOfFreedom(-1);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedomWithPositiveValue() {
        fDistribution.setNumeratorDegreesOfFreedom(10);
        Assert.assertEquals(10.0, fDistribution.getNumeratorDegreesOfFreedom(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomWithNegativeValue() {
        fDistribution.setDenominatorDegreesOfFreedom(-1);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedomWithPositiveValue() {
        fDistribution.setDenominatorDegreesOfFreedom(20);
        Assert.assertEquals(20.0, fDistribution.getDenominatorDegreesOfFreedom(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeNumeratorDegreesOfFreedom() {
        new FDistributionImpl(-5, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithZeroDenominatorDegreesOfFreedom() {
        new FDistributionImpl(5, 0);
    }

}