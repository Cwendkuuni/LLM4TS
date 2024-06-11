import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl distribution = new FDistributionImpl(3.0, 4.0);
        assertEquals(0.0, distribution.cumulativeProbability(0.0), 0.0001);
        assertEquals(0.1513, distribution.cumulativeProbability(1.0), 0.0001);
        assertEquals(0.4386, distribution.cumulativeProbability(2.0), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl distribution = new FDistributionImpl(3.0, 4.0);
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.0), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1.0), 0.0001);
        assertEquals(2.1773, distribution.inverseCumulativeProbability(0.95), 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl distribution = new FDistributionImpl(3.0, 4.0);
        assertEquals(0.0, distribution.getDomainLowerBound(0.5), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl distribution = new FDistributionImpl(3.0, 4.0);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.5), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl distribution = new FDistributionImpl(3.0, 4.0);
        assertEquals(2.0, distribution.getInitialDomain(0.5), 0.0001);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(3.0, 4.0);
        distribution.setNumeratorDegreesOfFreedom(5.0);
        assertEquals(5.0, distribution.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomNegative() {
        FDistributionImpl distribution = new FDistributionImpl(3.0, 4.0);
        distribution.setNumeratorDegreesOfFreedom(-5.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(3.0, 4.0);
        distribution.setDenominatorDegreesOfFreedom(6.0);
        assertEquals(6.0, distribution.getDenominatorDegreesOfFreedom(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomNegative() {
        FDistributionImpl distribution = new FDistributionImpl(3.0, 4.0);
        distribution.setDenominatorDegreesOfFreedom(-6.0);
    }
}