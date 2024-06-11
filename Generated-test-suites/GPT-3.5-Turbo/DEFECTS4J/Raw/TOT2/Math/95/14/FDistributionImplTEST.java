import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.MathException;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDist = new FDistributionImpl(10, 20);
        try {
            assertEquals(0.3582, fDist.cumulativeProbability(1.0), 0.0001);
            assertEquals(1.0, fDist.cumulativeProbability(0.0), 0);
        } catch (MathException e) {
            fail("MathException thrown");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDist = new FDistributionImpl(10, 20);
        try {
            assertEquals(0.2875, fDist.inverseCumulativeProbability(0.5), 0.0001);
            assertEquals(0.0, fDist.inverseCumulativeProbability(0), 0);
            assertEquals(Double.POSITIVE_INFINITY, fDist.inverseCumulativeProbability(1), 0);
        } catch (MathException e) {
            fail("MathException thrown");
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDist = new FDistributionImpl(10, 20);
        assertEquals(0.0, fDist.getDomainLowerBound(0), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDist = new FDistributionImpl(10, 20);
        assertEquals(Double.MAX_VALUE, fDist.getDomainUpperBound(0), 0);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDist = new FDistributionImpl(10, 20);
        assertEquals(1.0, fDist.getInitialDomain(0), 0);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(10, 20);
        fDist.setNumeratorDegreesOfFreedom(15);
        assertEquals(15.0, fDist.getNumeratorDegreesOfFreedom(), 0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(10, 20);
        fDist.setDenominatorDegreesOfFreedom(25);
        assertEquals(25.0, fDist.getDenominatorDegreesOfFreedom(), 0);
    }

}