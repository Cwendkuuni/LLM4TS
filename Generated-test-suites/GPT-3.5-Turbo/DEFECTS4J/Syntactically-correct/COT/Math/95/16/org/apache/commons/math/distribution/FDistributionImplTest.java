import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(1.0, 2.0);
        double result = fDistribution.cumulativeProbability(0.5);
        assertEquals(0.4538196607443539, result, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(1.0, 2.0);
        double result = fDistribution.inverseCumulativeProbability(0.5);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(1.0, 2.0);
        double result = fDistribution.getDomainLowerBound(0.5);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(1.0, 2.0);
        double result = fDistribution.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, result, 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(1.0, 2.0);
        double result = fDistribution.getInitialDomain(0.5);
        assertEquals(2.0, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomNegative() {
        FDistributionImpl fDistribution = new FDistributionImpl(1.0, 2.0);
        fDistribution.setNumeratorDegreesOfFreedom(-1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomNegative() {
        FDistributionImpl fDistribution = new FDistributionImpl(1.0, 2.0);
        fDistribution.setDenominatorDegreesOfFreedom(-1.0);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(1.0, 2.0);
        double result = fDistribution.getNumeratorDegreesOfFreedom();
        assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(1.0, 2.0);
        double result = fDistribution.getDenominatorDegreesOfFreedom();
        assertEquals(2.0, result, 0.0001);
    }
}