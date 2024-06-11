
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.junit.Test;

public class NormalDistributionImplTest {

    private static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1e-9;

    @Test
    public void testConstructor() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.getMean(), 0);
        assertEquals(1, distribution.getStandardDeviation(), 0);
        assertEquals(DEFAULT_INVERSE_ABSOLUTE_ACCURACY, distribution.getSolverAbsoluteAccuracy(), 0);
    }

    @Test
    public void testConstructorWithInverseCumAccuracy() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1, 1e-6);
        assertEquals(0, distribution.getMean(), 0);
        assertEquals(1, distribution.getStandardDeviation(), 0);
        assertEquals(1e-6, distribution.getSolverAbsoluteAccuracy(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidStandardDeviation() {
        new NormalDistributionImpl(0, -1);
    }

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        assertEquals(10, distribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        assertEquals(2, distribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989422804014327, distribution.density(0), 1e-9);
        assertEquals(0.24197072451914337, distribution.density(1), 1e-9);
        assertEquals(0.05399096651318806, distribution.density(-1), 1e-9);
        assertEquals(0, distribution.density(-10), 0);
        assertEquals(0, distribution.density(10), 0);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.cumulativeProbability(-10), 0);
        assertEquals(0.5, distribution.cumulativeProbability(0), 0);
        assertEquals(1, distribution.cumulativeProbability(10), 0);
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0), 0);
        assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1), 0);
        try {
            distribution.inverseCumulativeProbability(-1);
            fail("MathException not thrown");
        } catch (MathException e) {
            // Expected exception
        }
        try {
            distribution.inverseCumulativeProbability(2);
            fail("MathException not thrown");
        } catch (MathException e) {
            // Expected exception
        }
    }

    @Test
    public void testSample() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double sample = distribution.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(Double.NEGATIVE_INFINITY, distribution.getDomainLowerBound(0), 0);
        assertEquals(-1.7976931348623157E308, distribution.getDomainLowerBound(0.5), 0);
        assertEquals(-1.7976931348623157E308, distribution.getDomainLowerBound(1), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(1.7976931348623157E308, distribution.getDomainUpperBound(0), 0);
        assertEquals(1.7976931348623157E308, distribution.getDomainUpperBound(0.5), 0);
        assertEquals(Double.POSITIVE_INFINITY, distribution.getDomainUpperBound(1), 0);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(-1.7976931348623157E308, distribution.getInitialDomain(0), 0);
        assertEquals(0, distribution.getInitialDomain(0.5), 0);
        assertEquals(1, distribution.getInitialDomain(1), 0);
    }
}
