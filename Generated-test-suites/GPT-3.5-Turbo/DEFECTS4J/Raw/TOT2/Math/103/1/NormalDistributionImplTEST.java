import static org.junit.Assert.*;
import org.junit.Test;

public class NormalDistributionImplTest {

    private NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);

    @Test
    public void testConstructor() {
        assertEquals(0.0, normalDist.getMean(), 0.0001);
        assertEquals(1.0, normalDist.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testSetMean() {
        normalDist.setMean(2.5);
        assertEquals(2.5, normalDist.getMean(), 0.0001);
    }

    @Test
    public void testSetStandardDeviation() {
        normalDist.setStandardDeviation(2.0);
        assertEquals(2.0, normalDist.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        double result = normalDist.cumulativeProbability(1.0);
        assertEquals(0.8413, result, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        double result = normalDist.inverseCumulativeProbability(0.5);
        assertEquals(0.0, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationException() {
        normalDist.setStandardDeviation(-1.0);
    }
    
    @Test
    public void testInverseCumulativeProbabilityException() {
        try {
            normalDist.inverseCumulativeProbability(1.5);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected Exception
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        double result = normalDist.getDomainLowerBound(0.3);
        assertEquals(-Double.MAX_VALUE, result, 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        double result = normalDist.getDomainUpperBound(0.7);
        assertEquals(Double.MAX_VALUE, result, 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        double result = normalDist.getInitialDomain(0.5);
        assertEquals(1.0, result, 0.0001);
    }
}