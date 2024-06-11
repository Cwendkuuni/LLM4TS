import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.MathException;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fdistribution = new FDistributionImpl(3.0, 4.0);
        assertEquals(0.0, fdistribution.cumulativeProbability(0.0), 0.0001);
        assertEquals(0.2812, fdistribution.cumulativeProbability(1.0), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fdistribution = new FDistributionImpl(3.0, 4.0);
        assertEquals(0.0, fdistribution.inverseCumulativeProbability(0.0), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, fdistribution.inverseCumulativeProbability(1.0), 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fdistribution = new FDistributionImpl(3.0, 4.0);
        assertEquals(0.0, fdistribution.getDomainLowerBound(0.0), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fdistribution = new FDistributionImpl(3.0, 4.0);
        assertEquals(Double.MAX_VALUE, fdistribution.getDomainUpperBound(0.0), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fdistribution = new FDistributionImpl(3.0, 4.0);
        assertEquals(2.0, fdistribution.getInitialDomain(0.0), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fdistribution = new FDistributionImpl(3.0, 4.0);
        fdistribution.setNumeratorDegreesOfFreedom(-2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fdistribution = new FDistributionImpl(3.0, 4.0);
        fdistribution.setDenominatorDegreesOfFreedom(-2.0);
    }

}