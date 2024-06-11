import static org.junit.Assert.*;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fdist = new FDistributionImpl(2.0, 3.0);
        assertEquals(0.0, fdist.cumulativeProbability(-1.0), 0.0001);
        assertEquals(1.0, fdist.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0001);
        
        try {
            fdist.cumulativeProbability(0.0);
            fail("Expected MathException not thrown");
        } catch (MathException e) {
            // Expected exception
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fdist = new FDistributionImpl(2.0, 3.0);
        assertEquals(0.0, fdist.inverseCumulativeProbability(0.0), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, fdist.inverseCumulativeProbability(1.0), 0.0001);
        
        try {
            fdist.inverseCumulativeProbability(1.1);
            fail("Expected MathException not thrown");
        } catch (MathException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fdist = new FDistributionImpl(2.0, 3.0);
        assertEquals(0.0, fdist.getDomainLowerBound(0.5), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fdist = new FDistributionImpl(2.0, 3.0);
        assertEquals(Double.MAX_VALUE, fdist.getDomainUpperBound(0.5), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fdist = new FDistributionImpl(2.0, 3.0);
        assertEquals(2.0, fdist.getInitialDomain(0.5), 0.0001);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fdist = new FDistributionImpl(2.0, 3.0);
        fdist.setNumeratorDegreesOfFreedom(3.0);
        assertEquals(3.0, fdist.getNumeratorDegreesOfFreedom(), 0.0001);
        
        try {
            fdist.setNumeratorDegreesOfFreedom(-1.0);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fdist = new FDistributionImpl(2.0, 3.0);
        fdist.setDenominatorDegreesOfFreedom(4.0);
        assertEquals(4.0, fdist.getDenominatorDegreesOfFreedom(), 0.0001);
        
        try {
            fdist.setDenominatorDegreesOfFreedom(-2.0);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}