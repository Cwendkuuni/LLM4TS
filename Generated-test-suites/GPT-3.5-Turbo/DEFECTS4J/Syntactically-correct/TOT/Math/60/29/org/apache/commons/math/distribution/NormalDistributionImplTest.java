import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {

    private static final double EPSILON = 1e-9;
    private NormalDistribution distribution;

    @Before
    public void setUp() {
        distribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        assertEquals(0.0, distribution.getMean(), EPSILON);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, distribution.getStandardDeviation(), EPSILON);
    }

    @Test
    public void testDensity() {
        double density = distribution.density(0.0);
        assertEquals(0.3989422804014327, density, EPSILON);
    }

    @Test
    public void testCumulativeProbability() {
        double cumulativeProbability = 0.0;
        try {
            cumulativeProbability = distribution.cumulativeProbability(0.0);
        } catch (Exception e) {
            fail("Exception thrown");
        }
        assertEquals(0.5, cumulativeProbability, EPSILON);
    }

    @Test
    public void testInverseCumulativeProbability() {
        double inverseCumulativeProbability = 0.0;
        try {
            inverseCumulativeProbability = distribution.inverseCumulativeProbability(0.5);
        } catch (Exception e) {
            fail("Exception thrown");
        }
        assertEquals(0.0, inverseCumulativeProbability, EPSILON);
    }

    @Test
    public void testInverseCumulativeProbabilityWithZeroProbability() {
        double inverseCumulativeProbability = 0;
        try {
            inverseCumulativeProbability = distribution.inverseCumulativeProbability(0.0);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception throw");
        }
        assertEquals(Double.NEGATIVE_INFINITY, inverseCumulativeProbability, EPSILON);
    }

    @Test
    public void testInverseCumulativeProbabilityWithOneProbability() {
        double inverseCumulativeProbability = 0;
        try {
            inverseCumulativeProbability = distribution.inverseCumulativeProbability(1.0);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception throw");
        }
        assertEquals(Double.POSITIVE_INFINITY, inverseCumulativeProbability, EPSILON);
    }

    @Test
    public void testSample() {
        try {
            double sample = distribution.sample();
            assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
}