import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    private FDistributionImpl fd;

    public FDistributionImplTest() {
        fd = new FDistributionImpl(2.0, 3.0);
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        assertEquals(0.0, fd.cumulativeProbability(0.0), 0.0001);
        assertEquals(0.3086, fd.cumulativeProbability(1.0), 0.0001);
        assertEquals(0.3692, fd.cumulativeProbability(1.5), 0.0001);
        assertEquals(0.4540, fd.cumulativeProbability(2.0), 0.0001);
        assertEquals(0.5856, fd.cumulativeProbability(3.0), 0.0001);
        assertEquals(0.7522, fd.cumulativeProbability(5.0), 0.0001);
        assertEquals(0.9030, fd.cumulativeProbability(10.0), 0.0001);
        assertEquals(0.9602, fd.cumulativeProbability(20.0), 0.0001);
        assertEquals(0.9861, fd.cumulativeProbability(50.0), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws Exception {
        assertEquals(0.0, fd.inverseCumulativeProbability(0.0), 0.0001);
        assertEquals(0.5153, fd.inverseCumulativeProbability(0.1), 0.0001);
        assertEquals(0.8073, fd.inverseCumulativeProbability(0.2), 0.0001);
        assertEquals(1.3863, fd.inverseCumulativeProbability(0.3), 0.0001);
        assertEquals(2.6865, fd.inverseCumulativeProbability(0.4), 0.0001);
        assertEquals(6.5554, fd.inverseCumulativeProbability(0.5), 0.0001);
        assertEquals(17.7767, fd.inverseCumulativeProbability(0.6), 0.0001);
        assertEquals(158.2622, fd.inverseCumulativeProbability(0.9), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, fd.inverseCumulativeProbability(1.0), 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(0.0, fd.getDomainLowerBound(0.0), 0.0001);
        assertEquals(0.0, fd.getDomainLowerBound(0.1), 0.0001);
        assertEquals(0.0, fd.getDomainLowerBound(0.2), 0.0001);
        assertEquals(0.0, fd.getDomainLowerBound(0.3), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.0), 0.0001);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.1), 0.0001);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.2), 0.0001);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.3), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        assertEquals(1.5, fd.getInitialDomain(0.0), 0.0001);
        assertEquals(1.5, fd.getInitialDomain(0.1), 0.0001);
        assertEquals(1.5, fd.getInitialDomain(0.2), 0.0001);
        assertEquals(1.5, fd.getInitialDomain(0.3), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomThrowsException() {
        fd.setNumeratorDegreesOfFreedom(0.0);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        fd.setNumeratorDegreesOfFreedom(2.0);
        assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomThrowsException() {
        fd.setDenominatorDegreesOfFreedom(0.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        fd.setDenominatorDegreesOfFreedom(3.0);
        assertEquals(3.0, fd.getDenominatorDegreesOfFreedom(), 0.0001);
    }
}