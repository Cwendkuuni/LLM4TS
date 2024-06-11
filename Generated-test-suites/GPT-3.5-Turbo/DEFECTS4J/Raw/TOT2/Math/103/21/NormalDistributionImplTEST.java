import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl normal = new NormalDistributionImpl(5, 2);
        assertEquals(5, normal.getMean(), 0.0);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        normal.setMean(10);
        assertEquals(10, normal.getMean(), 0.0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 3);
        assertEquals(3, normal.getStandardDeviation(), 0.0);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        normal.setStandardDeviation(8);
        assertEquals(8, normal.getStandardDeviation(), 0.0);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(0.5, normal.cumulativeProbability(0), 0.0);
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(Double.NEGATIVE_INFINITY, normal.inverseCumulativeProbability(0), 0.0);
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        assertEquals(-Double.MAX_VALUE, normal.getDomainLowerBound(0.2), 0.0);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        assertEquals(Double.MAX_VALUE, normal.getDomainUpperBound(0.8), 0.0);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        assertEquals(-1, normal.getInitialDomain(0.1), 0.0);
    }

}