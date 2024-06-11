import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        assertEquals(0.248969, fDistribution.cumulativeProbability(1.2), 0.000001);
        assertEquals(0.857552, fDistribution.cumulativeProbability(2.0), 0.000001);
        assertEquals(0.977924, fDistribution.cumulativeProbability(4.0), 0.000001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        assertEquals(1.271902, fDistribution.inverseCumulativeProbability(0.3), 0.000001);
        assertEquals(3.489538, fDistribution.inverseCumulativeProbability(0.8), 0.000001);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.inverseCumulativeProbability(1.0), 0.000001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.5), 0.000001);
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.8), 0.000001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.5), 0.000001);
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.8), 0.000001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        assertEquals(3.0, fDistribution.getInitialDomain(0.5), 0.000001);
        assertEquals(3.0, fDistribution.getInitialDomain(0.8), 0.000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomIllegalArgumentException() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        fDistribution.setNumeratorDegreesOfFreedom(0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomIllegalArgumentException() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        fDistribution.setDenominatorDegreesOfFreedom(0.0);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        assertEquals(2.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.000001);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        assertEquals(3.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.000001);
    }
}