import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fDistributionImpl = new FDistributionImpl(10, 5);
        assertEquals(0.0, fDistributionImpl.cumulativeProbability(0.0), 0.00001);
        assertEquals(1.0, fDistributionImpl.cumulativeProbability(Double.POSITIVE_INFINITY), 0.00001);
        assertEquals(0.40865301596315244, fDistributionImpl.cumulativeProbability(0.5), 0.00001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fDistributionImpl = new FDistributionImpl(10, 5);
        assertEquals(0.0, fDistributionImpl.inverseCumulativeProbability(0.0), 0.00001);
        assertEquals(Double.POSITIVE_INFINITY, fDistributionImpl.inverseCumulativeProbability(1.0), 0.00001);
        assertEquals(1.6857912708403869, fDistributionImpl.inverseCumulativeProbability(0.5), 0.00001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistributionImpl = new FDistributionImpl(10, 5);
        assertEquals(0.0, fDistributionImpl.getDomainLowerBound(0.0), 0.00001);
        assertEquals(0.0, fDistributionImpl.getDomainLowerBound(0.5), 0.00001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistributionImpl = new FDistributionImpl(10, 5);
        assertEquals(Double.MAX_VALUE, fDistributionImpl.getDomainUpperBound(0.0), 0.00001);
        assertEquals(Double.MAX_VALUE, fDistributionImpl.getDomainUpperBound(0.5), 0.00001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistributionImpl = new FDistributionImpl(10, 5);
        assertEquals(5.0, fDistributionImpl.getInitialDomain(0.0), 0.00001);
        assertEquals(5.0, fDistributionImpl.getInitialDomain(0.5), 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistributionImpl = new FDistributionImpl(10, 5);
        fDistributionImpl.setNumeratorDegreesOfFreedom(0);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistributionImpl = new FDistributionImpl(10, 5);
        assertEquals(10, fDistributionImpl.getNumeratorDegreesOfFreedom(), 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistributionImpl = new FDistributionImpl(10, 5);
        fDistributionImpl.setDenominatorDegreesOfFreedom(0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistributionImpl = new FDistributionImpl(10, 5);
        assertEquals(5, fDistributionImpl.getDenominatorDegreesOfFreedom(), 0.00001);
    }
}