import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        try {
            assertEquals(0.25, fDistribution.cumulativeProbability(1.0), 0.0001);
        } catch (MathException e) {
            fail("MathException should not be thrown.");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        try {
            assertEquals(1.0, fDistribution.inverseCumulativeProbability(0.25), 0.0001);
        } catch (MathException e) {
            fail("MathException should not be thrown.");
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.25), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.25), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(1.5, fDistribution.getInitialDomain(0.25), 0.0001);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        fDistribution.setNumeratorDegreesOfFreedom(3.0);
        assertEquals(3.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        fDistribution.setDenominatorDegreesOfFreedom(4.0);
        assertEquals(4.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
    }

}