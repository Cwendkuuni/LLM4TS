import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.0, 1.0);
        assertEquals(2.0, distribution.getMean(), 0.0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.0, 1.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.3989422804014337, distribution.density(0.0), 1e-15);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        try {
            assertEquals(0.5, distribution.cumulativeProbability(0.0), 1e-15);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        try {
            assertEquals(0.0, distribution.inverseCumulativeProbability(0.5), 1e-15);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSample() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        try {
            double sample = distribution.sample();
            assertTrue(sample <= 3.0 && sample >= -3.0);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testDomainBounds() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.3), 0.0);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.3), 0.0);
        assertEquals(-1.0, distribution.getInitialDomain(0.4), 0.0);
    }
}