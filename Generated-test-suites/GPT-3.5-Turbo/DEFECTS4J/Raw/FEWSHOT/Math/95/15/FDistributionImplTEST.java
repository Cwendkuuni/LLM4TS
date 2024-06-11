import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Beta;
import org.junit.Before;
import org.junit.Test;

public class FDistributionImplTest {
    private FDistributionImpl fDistribution;

    @Before
    public void setUp() {
        fDistribution = new FDistributionImpl(5.0, 10.0);
    }

    @Test
    public void testCumulativeProbability() {
        try {
            assertEquals(0.0, fDistribution.cumulativeProbability(-1.0), 0.0001);
            assertEquals(0.0, fDistribution.cumulativeProbability(0.0), 0.0001);
            assertEquals(0.3004, fDistribution.cumulativeProbability(1.0), 0.0001);
            assertEquals(0.41, fDistribution.cumulativeProbability(1.5), 0.0001);
            assertEquals(0.6858, fDistribution.cumulativeProbability(2.0), 0.0001);
            assertEquals(0.8979, fDistribution.cumulativeProbability(2.5), 0.0001);
            assertEquals(0.9578, fDistribution.cumulativeProbability(3.0), 0.0001);
            assertEquals(0.9852, fDistribution.cumulativeProbability(3.5), 0.0001);
            assertEquals(0.995, fDistribution.cumulativeProbability(4.0), 0.0001);
            assertEquals(0.998, fDistribution.cumulativeProbability(4.5), 0.0001);
            assertEquals(0.9993, fDistribution.cumulativeProbability(5.0), 0.0001);
            assertEquals(0.9998, fDistribution.cumulativeProbability(5.5), 0.0001);
            assertEquals(0.9999, fDistribution.cumulativeProbability(6.0), 0.0001);
            assertEquals(1.0, fDistribution.cumulativeProbability(7.0), 0.0001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        try {
            assertEquals(0.0, fDistribution.inverseCumulativeProbability(0.0), 0.0001);
            assertEquals(5.0005, fDistribution.inverseCumulativeProbability(0.001), 0.0001);
            assertEquals(5.2042, fDistribution.inverseCumulativeProbability(0.01), 0.0001);
            assertEquals(5.5989, fDistribution.inverseCumulativeProbability(0.05), 0.0001);
            assertEquals(6.3458, fDistribution.inverseCumulativeProbability(0.1), 0.0001);
            assertEquals(6.9993, fDistribution.inverseCumulativeProbability(0.2), 0.0001);
            assertEquals(8.2683, fDistribution.inverseCumulativeProbability(0.3), 0.0001);
            assertEquals(9.8988, fDistribution.inverseCumulativeProbability(0.4), 0.0001);
            assertEquals(14.63, fDistribution.inverseCumulativeProbability(0.9), 0.0001);
            assertEquals(23.24, fDistribution.inverseCumulativeProbability(0.99), 0.0001);
            assertEquals(Double.POSITIVE_INFINITY, fDistribution.inverseCumulativeProbability(1.0), 0.0001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetDomainLowerBound() {
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.01), 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBound()  {
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.01), 0.0001);
    }
    
    @Test
    public void testGetInitialDomain() {
        assertEquals(1.25, fDistribution.getInitialDomain(0.5), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        fDistribution.setNumeratorDegreesOfFreedom(-1.0);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        assertEquals(5.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        fDistribution.setDenominatorDegreesOfFreedom(-1.0);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        assertEquals(10.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
    }
    
}