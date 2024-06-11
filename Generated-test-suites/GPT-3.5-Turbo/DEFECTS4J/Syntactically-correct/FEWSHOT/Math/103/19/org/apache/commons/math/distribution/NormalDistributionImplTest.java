
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {
    private static final double EPSILON = 1e-10;
    private NormalDistribution normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        assertEquals(0.0, normalDistribution.getMean(), EPSILON);

        normalDistribution = new NormalDistributionImpl(3.5, 2.0);
        assertEquals(3.5, normalDistribution.getMean(), EPSILON);
    }

    @Test
    public void testSetMean() {
        normalDistribution.setMean(-1.0);
        assertEquals(-1.0, normalDistribution.getMean(), EPSILON);

        normalDistribution.setMean(0.0);
        assertEquals(0.0, normalDistribution.getMean(), EPSILON);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, normalDistribution.getStandardDeviation(), EPSILON);

        normalDistribution = new NormalDistributionImpl(3.5, 2.0);
        assertEquals(2.0, normalDistribution.getStandardDeviation(), EPSILON);
    }

    @Test
    public void testSetStandardDeviation() {
        normalDistribution.setStandardDeviation(2.5);
        assertEquals(2.5, normalDistribution.getStandardDeviation(), EPSILON);

        normalDistribution.setStandardDeviation(1.0);
        assertEquals(1.0, normalDistribution.getStandardDeviation(), EPSILON);
    }

    @Test
    public void testCumulativeProbability() {
        try {
            normalDistribution.cumulativeProbability(0.5);
        } catch (MathException e) {
            fail("cumulativeProbability method should not throw MathException");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        try {
            normalDistribution.inverseCumulativeProbability(0.5);
        } catch (MathException e) {
            fail("inverseCumulativeProbability method should not throw MathException");
        }
    }

    @Test
    public void testInverseCumulativeProbabilityWithLowerBound() {
        try {
            normalDistribution.inverseCumulativeProbability(0.0);
            fail("inverseCumulativeProbability method should throw IllegalArgumentException");
        } catch (MathException e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testInverseCumulativeProbabilityWithUpperBound() {
        try {
            normalDistribution.inverseCumulativeProbability(1.0);
            fail("inverseCumulativeProbability method should throw IllegalArgumentException");
        } catch (MathException e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.25), EPSILON);
        assertEquals(0.0, normalDistribution.getDomainLowerBound(0.75), EPSILON);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(0.0, normalDistribution.getDomainUpperBound(0.25), EPSILON);
        assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.75), EPSILON);
    }

    @Test
    public void testGetInitialDomain() {
        assertEquals(0.0 - 1.0, normalDistribution.getInitialDomain(0.25), EPSILON);
        assertEquals(0.0 + 1.0, normalDistribution.getInitialDomain(0.75), EPSILON);
        assertEquals(0.0, normalDistribution.getInitialDomain(0.5), EPSILON);
    }
}
