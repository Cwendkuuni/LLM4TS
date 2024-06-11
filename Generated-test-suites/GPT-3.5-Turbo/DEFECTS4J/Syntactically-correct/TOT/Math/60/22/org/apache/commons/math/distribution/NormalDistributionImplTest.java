import static org.junit.Assert.*;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(5, 2);
        assertEquals(5, normalDist.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(5, 2);
        assertEquals(2, normalDist.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989422804014327, normalDist.density(0), 0.00000001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(0.5, normalDist.cumulativeProbability(0), 0.0001);
        } catch (MathException e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(0, normalDist.inverseCumulativeProbability(0), 0.0001);
        } catch (MathException e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testSample() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        try {
            double sample = normalDist.sample();
            assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
        } catch (MathException e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(1e-9, normalDist.getSolverAbsoluteAccuracy(), 0);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(-Double.MAX_VALUE, normalDist.getDomainLowerBound(0.2), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(Double.MAX_VALUE, normalDist.getDomainUpperBound(0.8), 0);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(-1, normalDist.getInitialDomain(0.2), 0);
    }

}