import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        double result = fDistribution.cumulativeProbability(2.0);
        assertEquals(0.8857917835546, result, 1e-10);
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        double result = fDistribution.inverseCumulativeProbability(0.5);
        assertEquals(1.06066017178, result, 1e-10);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        double result = fDistribution.getDomainLowerBound(0.3);
        assertEquals(0.0, result, 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        double result = fDistribution.getDomainUpperBound(0.3);
        assertEquals(Double.MAX_VALUE, result, 0);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        double result = fDistribution.getInitialDomain(0.3);
        assertEquals(1.25, result, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        fDistribution.setNumeratorDegreesOfFreedom(-1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        fDistribution.setDenominatorDegreesOfFreedom(-1.0);
    }
}