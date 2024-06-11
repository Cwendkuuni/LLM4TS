import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.util.LocalizedFormats;

import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    private static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1e-9;
    private static final double EPSILON = 1e-9;

    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        assertEquals(0.0, normalDistribution.getMean(), EPSILON);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, normalDistribution.getStandardDeviation(), EPSILON);
    }

    @Test
    public void testDensity() {
        // Typical use case
        assertEquals(0.3989422804014327, normalDistribution.density(0.0), EPSILON);

        // Edge cases
        assertEquals(0.0, normalDistribution.density(Double.NEGATIVE_INFINITY), EPSILON);
        assertEquals(0.0, normalDistribution.density(Double.POSITIVE_INFINITY), EPSILON);
        assertEquals(0.0, normalDistribution.density(Double.NaN), EPSILON);

        // Error scenario
        try {
            normalDistribution.density(-1.0);
            fail("Expected MathException to be thrown");
        } catch (MathException e) {
            assertTrue(e.getMessage().contains(LocalizedFormats.INVALID_RANGE_BOUNDS.toString()));
        }
    }

    @Test
    public void testCumulativeProbability() {
        // Typical use case
        assertEquals(0.5, normalDistribution.cumulativeProbability(0.0), EPSILON);

        // Edge cases
        assertEquals(0.0, normalDistribution.cumulativeProbability(Double.NEGATIVE_INFINITY), EPSILON);
        assertEquals(1.0, normalDistribution.cumulativeProbability(Double.POSITIVE_INFINITY), EPSILON);
        assertEquals(0.5, normalDistribution.cumulativeProbability(Double.NaN), EPSILON);

        // Error scenario
        try {
            normalDistribution.cumulativeProbability(-1.0);
            fail("Expected MathException to be thrown");
        } catch (MathException e) {
            assertTrue(e.getMessage().contains(LocalizedFormats.INVALID_RANGE_BOUNDS.toString()));
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        // Typical use case
        assertEquals(0.0, normalDistribution.inverseCumulativeProbability(0.5), EPSILON);

        // Edge cases
        assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0.0), EPSILON);
        assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1.0), EPSILON);
        assertEquals(Double.NaN, normalDistribution.inverseCumulativeProbability(Double.NaN), EPSILON);

        // Error scenario
        try {
            normalDistribution.inverseCumulativeProbability(-0.1);
            fail("Expected MathException to be thrown");
        } catch (MathException e) {
            assertTrue(e.getMessage().contains(LocalizedFormats.INVALID_RANGE_BOUNDS.toString()));
        }
    }

    @Test
    public void testSample() {
        // No concrete tests for random values
        // Just check if a value is returned without any error
        try {
            double value = normalDistribution.sample();
            assertTrue(Double.isFinite(value));
        } catch (MathException e) {
            fail("Failed to generate random value");
        }
    }

    @Test
    public void testInverseCumulativeProbabilityMethod() {
        // Specific tests for the method used within inverseCumulativeProbability

        // Typical use cases
        assertEquals(-1.2815515665801652, normalDistribution.inverseCumulativeProbabilityMethod(0.1), EPSILON);
        assertEquals(0.0, normalDistribution.inverseCumulativeProbabilityMethod(0.5), EPSILON);
        assertEquals(1.2815515665801652, normalDistribution.inverseCumulativeProbabilityMethod(0.9), EPSILON);

        // Edge cases
        assertEquals(-Double.MAX_VALUE, normalDistribution.inverseCumulativeProbabilityMethod(0.0), EPSILON);
        assertEquals(Double.MAX_VALUE, normalDistribution.inverseCumulativeProbabilityMethod(1.0), EPSILON);

        // Error scenarios
        try {
            normalDistribution.inverseCumulativeProbabilityMethod(-0.1);
            fail("Expected MathException to be thrown");
        } catch (MathException e) {
            assertTrue(e.getMessage().contains(LocalizedFormats.INVALID_RANGE_BOUNDS.toString()));
        }

        try {
            normalDistribution.inverseCumulativeProbabilityMethod(1.1);
            fail("Expected MathException to be thrown");
        } catch (MathException e) {
            assertTrue(e.getMessage().contains(LocalizedFormats.INVALID_RANGE_BOUNDS.toString()));
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        // p < 0.5
        assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.1), EPSILON);

        // p >= 0.5
        assertEquals(0.0, normalDistribution.getDomainLowerBound(0.6), EPSILON);
    }

    @Test
    public void testGetDomainUpperBound() {
        // p < 0.5
        assertEquals(0.0, normalDistribution.getDomainUpperBound(0.1), EPSILON);

        // p >= 0.5
        assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.6), EPSILON);
    }

    @Test
    public void testGetInitialDomain() {
        // p < 0.5
        assertEquals(-1.0, normalDistribution.getInitialDomain(0.1), EPSILON);

        // p > 0.5
        assertEquals(1.0, normalDistribution.getInitialDomain(0.6), EPSILON);

        // p = 0.5
        assertEquals(0.0, normalDistribution.getInitialDomain(0.5), EPSILON);
    }

}