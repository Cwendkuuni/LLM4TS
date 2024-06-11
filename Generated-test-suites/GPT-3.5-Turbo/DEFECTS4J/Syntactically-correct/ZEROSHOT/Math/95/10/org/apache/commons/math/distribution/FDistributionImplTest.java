import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl f = new FDistributionImpl(1, 2);
        assertEquals(0.0, f.cumulativeProbability(0.0), 0.001);
        assertEquals(0.629, f.cumulativeProbability(1.0), 0.001);
        assertEquals(0.861, f.cumulativeProbability(2.0), 0.001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl f = new FDistributionImpl(1, 2);
        assertEquals(0.0, f.inverseCumulativeProbability(0.0), 0.001);
        assertEquals(Double.POSITIVE_INFINITY, f.inverseCumulativeProbability(1.0), 0.001);
        assertEquals(1.914193073, f.inverseCumulativeProbability(0.9), 0.001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl f = new FDistributionImpl(1, 2);
        assertEquals(0.0, f.getDomainLowerBound(0.5), 0.001);
        assertEquals(0.0, f.getDomainLowerBound(1.0), 0.001);
    }

    @Test
    public void testGetDomainUpperBound() {
       FDistributionImpl f = new FDistributionImpl(1, 2);
        assertEquals(Double.MAX_VALUE, f.getDomainUpperBound(0.5), 0.001);
        assertEquals(Double.MAX_VALUE, f.getDomainUpperBound(1.0), 0.001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl f = new FDistributionImpl(1, 2);
        assertEquals(2.0, f.getInitialDomain(0.5), 0.001);
        assertEquals(2.0, f.getInitialDomain(1.0), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl f = new FDistributionImpl(1, 2);
        f.setNumeratorDegreesOfFreedom(0);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl f = new FDistributionImpl(1, 2);
        assertEquals(1.0, f.getNumeratorDegreesOfFreedom(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl f = new FDistributionImpl(1, 2);
        f.setDenominatorDegreesOfFreedom(0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl f = new FDistributionImpl(1, 2);
        assertEquals(2.0, f.getDenominatorDegreesOfFreedom(), 0.001);
    }
}