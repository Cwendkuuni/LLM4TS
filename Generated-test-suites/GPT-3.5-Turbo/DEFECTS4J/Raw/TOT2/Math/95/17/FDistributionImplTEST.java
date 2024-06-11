import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        double result = fDistribution.cumulativeProbability(2.0);
        assertEquals(0.7775, result, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        double result = fDistribution.inverseCumulativeProbability(0.5);
        assertEquals(1.5614, result, 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        double result = fDistribution.getDomainLowerBound(0.3);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        double result = fDistribution.getDomainUpperBound(0.7);
        assertEquals(Double.MAX_VALUE, result, 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        double result = fDistribution.getInitialDomain(0.5);
        assertEquals(1.6666, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomNegative() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        fDistribution.setNumeratorDegreesOfFreedom(-1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomZero() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        fDistribution.setDenominatorDegreesOfFreedom(0.0);
    }
}