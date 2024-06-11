import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(5, 2);
        assertEquals(5, normalDist.getMean(), 0.0001);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        normalDist.setMean(8);
        assertEquals(8, normalDist.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(3, 4);
        assertEquals(4, normalDist.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        normalDist.setStandardDeviation(6);
        assertEquals(6, normalDist.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        try {
            double result = normalDist.cumulativeProbability(1);
            assertEquals(0.8413, result, 0.0001);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        try {
            double result = normalDist.inverseCumulativeProbability(0.5);
            assertEquals(0, result, 0.0001);
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        double result = normalDist.getDomainLowerBound(0.3);
        assertEquals(-Double.MAX_VALUE, result, 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        double result = normalDist.getDomainUpperBound(0.7);
        assertEquals(Double.MAX_VALUE, result, 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        double result = normalDist.getInitialDomain(0.5);
        assertEquals(1, result, 0.0001);
    }
}