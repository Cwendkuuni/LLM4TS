import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl distribution = new FDistributionImpl(5, 10);
        double result = distribution.cumulativeProbability(2.5);
        assertEquals(0.842769627, result, 0.000000001);
    }

    @Test
    public void testCumulativeProbabilityWithZero() {
        FDistributionImpl distribution = new FDistributionImpl(2, 7);
        double result = distribution.cumulativeProbability(0);
        assertEquals(0, result, 0);
    }

    @Test
    public void testCumulativeProbabilityWithNegativeValue() {
        FDistributionImpl distribution = new FDistributionImpl(3, 8);
        double result = distribution.cumulativeProbability(-2);
        assertEquals(0, result, 0);
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl distribution = new FDistributionImpl(5, 10);
        double result = distribution.inverseCumulativeProbability(0.8);
        assertEquals(1.834338148, result, 0.000000001);
    }

    @Test
    public void testInverseCumulativeProbabilityWithZero() {
        FDistributionImpl distribution = new FDistributionImpl(2, 7);
        double result = distribution.inverseCumulativeProbability(0);
        assertEquals(0, result, 0);
    }

    @Test
    public void testInverseCumulativeProbabilityWithOne() {
        FDistributionImpl distribution = new FDistributionImpl(3, 8);
        double result = distribution.inverseCumulativeProbability(1);
        assertEquals(Double.POSITIVE_INFINITY, result, 0);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl distribution = new FDistributionImpl(5, 10);
        double result = distribution.getDomainLowerBound(0.5);
        assertEquals(0, result, 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl distribution = new FDistributionImpl(2, 7);
        double result = distribution.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, result, 0);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl distribution = new FDistributionImpl(3, 8);
        double result = distribution.getInitialDomain(0.5);
        assertEquals(1.6, result, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomWithZero() {
        FDistributionImpl distribution = new FDistributionImpl(0, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomWithNegativeValue() {
        FDistributionImpl distribution = new FDistributionImpl(-5, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomWithZero() {
        FDistributionImpl distribution = new FDistributionImpl(5, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomWithNegativeValue() {
        FDistributionImpl distribution = new FDistributionImpl(5, -10);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(5, 10);
        double result = distribution.getNumeratorDegreesOfFreedom();
        assertEquals(5, result, 0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(5, 10);
        double result = distribution.getDenominatorDegreesOfFreedom();
        assertEquals(10, result, 0);
    }
}