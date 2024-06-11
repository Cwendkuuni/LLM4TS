import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(1, 2);
        double result = fDistribution.cumulativeProbability(0.5);
        assertEquals(0.120299, result, 0.000001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(1, 2);
        double result = fDistribution.inverseCumulativeProbability(0.5);
        assertEquals(1.000000, result, 0.000001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(1, 2);
        double result = fDistribution.getDomainLowerBound(0.5);
        assertEquals(0.000000, result, 0.000001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(1, 2);
        double result = fDistribution.getDomainUpperBound(0.5);
        assertEquals(1.7976931348623157E308, result, 0.000001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(1, 2);
        double result = fDistribution.getInitialDomain(0.5);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(1, 2);
        fDistribution.setNumeratorDegreesOfFreedom(0);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(1, 2);
        double result = fDistribution.getNumeratorDegreesOfFreedom();
        assertEquals(1, result, 0.000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(1, 2);
        fDistribution.setDenominatorDegreesOfFreedom(0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(1, 2);
        double result = fDistribution.getDenominatorDegreesOfFreedom();
        assertEquals(2, result, 0.000001);
    }

}