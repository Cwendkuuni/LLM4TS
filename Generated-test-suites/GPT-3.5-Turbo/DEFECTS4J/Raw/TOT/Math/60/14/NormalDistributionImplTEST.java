import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.MathException;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testDensity() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        // Typical use case
        assertEquals(0.3989422804014327, normal.density(0), 1e-15);
        // Edge case
        assertEquals(1.4867195147342977e-6, normal.density(Double.NEGATIVE_INFINITY), 1e-15);
        // Error scenario
        assertEquals(0, normal.density(Double.NaN), 1e-15);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        // Typical use case
        assertEquals(0.5, normal.cumulativeProbability(0), 1e-15);
        // Edge case
        assertEquals(0, normal.cumulativeProbability(Double.NEGATIVE_INFINITY), 1e-15);
        // Error scenario
        assertEquals(Double.NaN, normal.cumulativeProbability(Double.NaN), 1e-15);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        // Typical use case
        assertEquals(0, normal.inverseCumulativeProbability(0), 1e-15);
        // Edge cases
        assertEquals(Double.NEGATIVE_INFINITY, normal.inverseCumulativeProbability(0), 1e-15);
        assertEquals(Double.POSITIVE_INFINITY, normal.inverseCumulativeProbability(1), 1e-15);
        // Error scenario
        try {
            normal.inverseCumulativeProbability(2);
            fail("Expected MathException");
        } catch (MathException e) {
            assertEquals("Expected exception message", e.getMessage());
        }
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        // Typical use case
        double sample = normal.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
        // Edge case
        for (int i = 0; i < 1000000; i++) {
            sample = normal.sample();
            assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
        }
    }

    @Test
    public void testGetMean() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        // Typical use case
        assertEquals(0, normal.getMean(), 1e-15);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        // Typical use case
        assertEquals(1, normal.getStandardDeviation(), 1e-15);
    }

    @Test
    public void testConstructor() {
        // Typical use case
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        assertEquals(0, normal.getMean(), 1e-15);
        assertEquals(1, normal.getStandardDeviation(), 1e-15);
        // Error scenario
        try {
            normal = new NormalDistributionImpl(0, -1);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            assertEquals("Expected exception message", e.getMessage());
        }
    }
}