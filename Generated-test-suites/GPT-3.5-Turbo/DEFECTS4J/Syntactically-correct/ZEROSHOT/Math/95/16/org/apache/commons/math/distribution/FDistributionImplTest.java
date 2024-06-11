import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);

        assertEquals(0.0, fd.cumulativeProbability(0.0), 0.0001);
        assertEquals(0.5, fd.cumulativeProbability(1.0), 0.0001);
        assertEquals(0.9388, fd.cumulativeProbability(2.0), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);

        assertEquals(0.0, fd.inverseCumulativeProbability(0.0), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, fd.inverseCumulativeProbability(1.0), 0.0001);
        assertEquals(2.0, fd.inverseCumulativeProbability(0.9388), 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);

        assertEquals(0.0, fd.getDomainLowerBound(0.0), 0.0001);
        assertEquals(0.0, fd.getDomainLowerBound(1.0), 0.0001);
        assertEquals(0.0, fd.getDomainLowerBound(0.5), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);

        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.0), 0.0001);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(1.0), 0.0001);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.5), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);

        assertEquals(1.5, fd.getInitialDomain(0.0), 0.0001);
        assertEquals(1.5, fd.getInitialDomain(1.0), 0.0001);
        assertEquals(1.5, fd.getInitialDomain(0.5), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);

        fd.setNumeratorDegreesOfFreedom(0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);

        fd.setDenominatorDegreesOfFreedom(0.0);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);

        assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);

        assertEquals(3.0, fd.getDenominatorDegreesOfFreedom(), 0.0001);
    }
}