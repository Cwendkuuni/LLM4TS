import org.junit.Test;
import org.apache.commons.math.distribution.FDistributionImpl;

import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        assertEquals(0.0163392, fDistribution.cumulativeProbability(0.1), 0.0000001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        assertEquals(1.632993, fDistribution.inverseCumulativeProbability(0.95), 0.000001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.1), 0.0);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.1), 0.0);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.getInitialDomain(0.1), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        fDistribution.setNumeratorDegreesOfFreedom(-1);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        assertEquals(2, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        fDistribution.setDenominatorDegreesOfFreedom(-1);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        assertEquals(3, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
    }
}