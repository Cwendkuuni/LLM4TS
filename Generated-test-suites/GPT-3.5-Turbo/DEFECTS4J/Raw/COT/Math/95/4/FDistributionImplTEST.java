import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.distribution.FDistributionImpl;
import org.apache.commons.math.MathException;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl distribution = new FDistributionImpl(3, 4);
        try {
            double result = distribution.cumulativeProbability(1.5);
            Assert.assertEquals(0.8484, result, 0.0001);
        } catch (MathException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl distribution = new FDistributionImpl(3, 4);
        try {
            double result = distribution.inverseCumulativeProbability(0.75);
            Assert.assertEquals(2.578, result, 0.001);
        } catch (MathException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testInverseCumulativeProbabilityWithZero() {
        FDistributionImpl distribution = new FDistributionImpl(3, 4);
        try {
            double result = distribution.inverseCumulativeProbability(0);
            Assert.assertEquals(0, result, 0);
        } catch (MathException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testInverseCumulativeProbabilityWithOne() {
        FDistributionImpl distribution = new FDistributionImpl(3, 4);
        try {
            double result = distribution.inverseCumulativeProbability(1);
            Assert.assertEquals(Double.POSITIVE_INFINITY, result, 0);
        } catch (MathException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl distribution = new FDistributionImpl(3, 4);
        double result = distribution.getDomainLowerBound(0.5);
        Assert.assertEquals(0.0, result, 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl distribution = new FDistributionImpl(3, 4);
        double result = distribution.getDomainUpperBound(0.5);
        Assert.assertEquals(Double.MAX_VALUE, result, 0);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl distribution = new FDistributionImpl(3, 4);
        double result = distribution.getInitialDomain(0.5);
        Assert.assertEquals(4.0, result, 0);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(3, 4);
        distribution.setNumeratorDegreesOfFreedom(5);
        Assert.assertEquals(5.0, distribution.getNumeratorDegreesOfFreedom(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomWithZero() {
        FDistributionImpl distribution = new FDistributionImpl(3, 4);
        distribution.setNumeratorDegreesOfFreedom(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomWithNegative() {
        FDistributionImpl distribution = new FDistributionImpl(3, 4);
        distribution.setNumeratorDegreesOfFreedom(-1);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(3, 4);
        distribution.setDenominatorDegreesOfFreedom(6);
        Assert.assertEquals(6.0, distribution.getDenominatorDegreesOfFreedom(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomWithZero() {
        FDistributionImpl distribution = new FDistributionImpl(3, 4);
        distribution.setDenominatorDegreesOfFreedom(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomWithNegative() {
        FDistributionImpl distribution = new FDistributionImpl(3, 4);
        distribution.setDenominatorDegreesOfFreedom(-1);
    }
}



// JUnit test cases go here

