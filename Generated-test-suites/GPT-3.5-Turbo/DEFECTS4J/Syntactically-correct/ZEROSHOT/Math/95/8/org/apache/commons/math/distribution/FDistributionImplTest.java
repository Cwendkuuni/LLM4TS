import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fd = new FDistributionImpl(2, 3);
        assertEquals(0.0468, fd.cumulativeProbability(1.0), 0.0001);
        assertEquals(0.2817, fd.cumulativeProbability(1.5), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fd = new FDistributionImpl(2, 3);
        assertEquals(1.8922, fd.inverseCumulativeProbability(0.2), 0.0001);
        assertEquals(3.4800, fd.inverseCumulativeProbability(0.6), 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fd = new FDistributionImpl(2, 3);
        assertEquals(0.0, fd.getDomainLowerBound(0.2), 0.0001);
        assertEquals(0.0, fd.getDomainLowerBound(0.6), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fd = new FDistributionImpl(2, 3);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.2), 0.0001);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.6), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fd = new FDistributionImpl(2, 3);
        assertEquals(1.5, fd.getInitialDomain(0.2), 0.0001);
        assertEquals(1.5, fd.getInitialDomain(0.6), 0.0001);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(2, 3);
        fd.setNumeratorDegreesOfFreedom(4);
        assertEquals(4, fd.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomWithInvalidValue() {
        FDistributionImpl fd = new FDistributionImpl(2, 3);
        fd.setNumeratorDegreesOfFreedom(0);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(2, 3);
        assertEquals(2, fd.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(2, 3);
        fd.setDenominatorDegreesOfFreedom(4);
        assertEquals(4, fd.getDenominatorDegreesOfFreedom(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomWithInvalidValue() {
        FDistributionImpl fd = new FDistributionImpl(2, 3);
        fd.setDenominatorDegreesOfFreedom(0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(2, 3);
        assertEquals(3, fd.getDenominatorDegreesOfFreedom(), 0.0001);
    }
}