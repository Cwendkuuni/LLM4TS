import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {
    
    private static final double DEFAULT_EPSILON = 1e-9;
    private static final double MEAN = 0.0;
    private static final double STANDARD_DEVIATION = 1.0;
    private static final double INVERSE_CUM_ACCURACY = 1e-9;
    
    private NormalDistributionImpl distribution;

    @Before
    public void setup() {
        distribution = new NormalDistributionImpl(MEAN, STANDARD_DEVIATION, INVERSE_CUM_ACCURACY);
    }

    @Test
    public void testMean() {
        assertEquals(MEAN, distribution.getMean(), DEFAULT_EPSILON);
    }

    @Test
    public void testStandardDeviation() {
        assertEquals(STANDARD_DEVIATION, distribution.getStandardDeviation(), DEFAULT_EPSILON);
    }
    
    @Test
    public void testDensity() {
        assertEquals(0.3989422804014327, distribution.density(0.0), DEFAULT_EPSILON);
        assertEquals(0.24197072451914337, distribution.density(-1.0), DEFAULT_EPSILON);
        assertEquals(0.24197072451914337, distribution.density(1.0), DEFAULT_EPSILON);
        assertEquals(0.05399096651318806, distribution.density(-2.0), DEFAULT_EPSILON);
        assertEquals(0.05399096651318806, distribution.density(2.0), DEFAULT_EPSILON);
    }

    @Test
    public void testCumulativeProbability() {
        try {
            assertEquals(0.5, distribution.cumulativeProbability(MEAN), DEFAULT_EPSILON);
            assertEquals(0.8413447460685429, distribution.cumulativeProbability(1.0), DEFAULT_EPSILON);
            assertEquals(0.15865525393145707, distribution.cumulativeProbability(-1.0), DEFAULT_EPSILON);
            assertEquals(0.02275013194817921, distribution.cumulativeProbability(-2.0), DEFAULT_EPSILON);
            assertEquals(0.9772498680518208, distribution.cumulativeProbability(2.0), DEFAULT_EPSILON);
        } catch (MathException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        try {
            assertEquals(MEAN, distribution.inverseCumulativeProbability(0.5), DEFAULT_EPSILON);
            assertEquals(1.0, distribution.inverseCumulativeProbability(0.8413447460685429), DEFAULT_EPSILON);
            assertEquals(-1.0, distribution.inverseCumulativeProbability(0.15865525393145707), DEFAULT_EPSILON);
            assertEquals(-2.0, distribution.inverseCumulativeProbability(0.02275013194817921), DEFAULT_EPSILON);
            assertEquals(2.0, distribution.inverseCumulativeProbability(0.9772498680518208), DEFAULT_EPSILON);
        } catch (MathException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testInverseCumulativeProbabilityException() {
        try {
            distribution.inverseCumulativeProbability(2.0);
            fail("Exception not thrown");
        } catch (MathException e) {
            assertTrue(e instanceof org.apache.commons.math.exception.OutOfRangeException);
            assertEquals("out of range (0, 1): 2.0", e.getMessage());
        }
    }

    @Test
    public void testSample() {
        try {
            double sample = distribution.sample();
            assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
        } catch (MathException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}