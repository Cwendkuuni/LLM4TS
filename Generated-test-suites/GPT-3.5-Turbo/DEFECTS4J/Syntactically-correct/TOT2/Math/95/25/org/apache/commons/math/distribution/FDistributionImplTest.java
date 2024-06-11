import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDist = new FDistributionImpl(5, 10);
        assertEquals(0.0, fDist.cumulativeProbability(0), 0.0001);
        assertEquals(0.36973, fDist.cumulativeProbability(1), 0.0001);
        assertEquals(1.0, fDist.cumulativeProbability(Double.MAX_VALUE), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDist = new FDistributionImpl(5, 10);
        assertEquals(0.0, fDist.inverseCumulativeProbability(0), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, fDist.inverseCumulativeProbability(1), 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDist = new FDistributionImpl(5, 10);
        assertEquals(0.0, fDist.getDomainLowerBound(0.5), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDist = new FDistributionImpl(5, 10);
        assertEquals(Double.MAX_VALUE, fDist.getDomainUpperBound(0.5), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDist = new FDistributionImpl(5, 10);
        assertEquals(2.5, fDist.getInitialDomain(0.5), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(5, 10);
        fDist.setNumeratorDegreesOfFreedom(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(5, 10);
        fDist.setDenominatorDegreesOfFreedom(0);
    }
}