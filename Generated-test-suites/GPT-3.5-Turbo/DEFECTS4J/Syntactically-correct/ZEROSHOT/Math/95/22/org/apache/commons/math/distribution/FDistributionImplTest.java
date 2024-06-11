import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FDistributionImplTest {

    private FDistributionImpl fDistribution;

    @Before
    public void setUp() {
        fDistribution = new FDistributionImpl(2, 3);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.0, fDistribution.cumulativeProbability(-1), 0.0001);
        assertEquals(0.0, fDistribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.1999999999999997, fDistribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.39999999999999973, fDistribution.cumulativeProbability(2), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(0.0, fDistribution.inverseCumulativeProbability(0), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.inverseCumulativeProbability(1), 0.0001);
        assertEquals(4.3616473025097345, fDistribution.inverseCumulativeProbability(0.5), 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(0.0, fDistribution.getDomainLowerBound(0), 0.0001);
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.5), 0.0001);
        assertEquals(0.0, fDistribution.getDomainLowerBound(1), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0), 0.0001);
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.5), 0.0001);
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(1), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        assertEquals(1.5, fDistribution.getInitialDomain(0), 0.0001);
        assertEquals(1.5, fDistribution.getInitialDomain(0.5), 0.0001);
        assertEquals(1.5, fDistribution.getInitialDomain(1), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomException() {
        fDistribution.setNumeratorDegreesOfFreedom(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomException() {
        fDistribution.setDenominatorDegreesOfFreedom(-1);
    }
}