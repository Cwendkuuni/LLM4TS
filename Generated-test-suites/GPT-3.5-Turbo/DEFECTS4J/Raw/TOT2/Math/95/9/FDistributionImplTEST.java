import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.MathException;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        try {
            assertEquals(0.0, fDistribution.cumulativeProbability(0.0), 0.0001);
            assertEquals(0.09431, fDistribution.cumulativeProbability(1.0), 0.0001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        try {
            assertEquals(0.0, fDistribution.inverseCumulativeProbability(0.0), 0.0001);
            assertEquals(Double.POSITIVE_INFINITY, fDistribution.inverseCumulativeProbability(1.0), 0.0001);
            assertEquals(1.025791, fDistribution.inverseCumulativeProbability(0.9), 0.0001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.5), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.5), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(1.5, fDistribution.getInitialDomain(0.5), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomException() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        fDistribution.setNumeratorDegreesOfFreedom(0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomException() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        fDistribution.setDenominatorDegreesOfFreedom(0.0);
    }

}