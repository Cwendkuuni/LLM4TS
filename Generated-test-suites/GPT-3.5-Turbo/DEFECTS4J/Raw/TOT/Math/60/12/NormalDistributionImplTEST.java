import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    // Test case for the constructor with mean and standard deviation
    @Test
    public void testConstructorWithMeanAndStandardDeviation() {
        // Typical test case
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, normalDistribution.getMean(), 0);
        assertEquals(1, normalDistribution.getStandardDeviation(), 0);

        // Edge case
        normalDistribution = new NormalDistributionImpl(Double.MAX_VALUE, Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, normalDistribution.getMean(), 0);
        assertEquals(Double.MAX_VALUE, normalDistribution.getStandardDeviation(), 0);

        // Error scenario
        try {
            NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    // Test case for the constructor with mean, standard deviation, and inverse cumulative distribution accuracy
    @Test
    public void testConstructorWithMeanStandardDeviationAndInverseCumulativeAccuracy() {
        // Typical test case
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1, 1e-9);
        assertEquals(0, normalDistribution.getMean(), 0);
        assertEquals(1, normalDistribution.getStandardDeviation(), 0);
        assertEquals(1e-9, normalDistribution.getSolverAbsoluteAccuracy(), 0);

        // Edge case
        normalDistribution = new NormalDistributionImpl(Double.MAX_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
        assertEquals(Double.MAX_VALUE, normalDistribution.getMean(), 0);
        assertEquals(Double.MAX_VALUE, normalDistribution.getStandardDeviation(), 0);
        assertEquals(Double.MIN_VALUE, normalDistribution.getSolverAbsoluteAccuracy(), 0);

        // Error scenario
        try {
            NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 0, -1);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    // Test case for the density method
    @Test
    public void testDensity() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);

        // Typical test case
        double density = normalDistribution.density(0);
        assertEquals(0.3989422804014327, density, 0.0000001);

        // Edge case
        density = normalDistribution.density(Double.POSITIVE_INFINITY);
        assertEquals(0, density, 0);

        // Error scenario
        density = normalDistribution.density(Double.NaN);
        assertTrue(Double.isNaN(density));
    }

    // Test case for the cumulativeProbability method
    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);

        // Typical test case
        try {
            double cumulativeProbability = normalDistribution.cumulativeProbability(0);
            assertEquals(0.5, cumulativeProbability, 0);
        } catch (MathException e) {
            fail("Unexpected MathException");
        }

        // Edge case
        try {
            double cumulativeProbability = normalDistribution.cumulativeProbability(Double.NEGATIVE_INFINITY);
            assertEquals(0, cumulativeProbability, 0);
        } catch (MathException e) {
            fail("Unexpected MathException");
        }

        // Error scenario
        try {
            double cumulativeProbability = normalDistribution.cumulativeProbability(Double.NaN);
            assertTrue(Double.isNaN(cumulativeProbability));
        } catch (MathException e) {
            fail("Unexpected MathException");
        }
    }

    // Test case for the inverseCumulativeProbability method
    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);

        // Typical test case
        try {
            double inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(0.5);
            assertEquals(0, inverseCumulativeProbability, 0);
        } catch (MathException e) {
            fail("Unexpected MathException");
        }

        // Edge case
        try {
            double inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(0);
            assertEquals(Double.NEGATIVE_INFINITY, inverseCumulativeProbability, 0);
        } catch (MathException e) {
            fail("Unexpected MathException");
        }

        try {
            double inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(1);
            assertEquals(Double.POSITIVE_INFINITY, inverseCumulativeProbability, 0);
        } catch (MathException e) {
            fail("Unexpected MathException");
        }

        // Error scenario
        try {
            double inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(2);
            fail("Expected OutOfRangeException");
        } catch (MathException e) {
            fail("Unexpected MathException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }

    // Test case for the sample method
    @Test
    public void testSample() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);

        // Typical test case
        try {
            double sample = normalDistribution.sample();
            assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
        } catch (MathException e) {
            fail("Unexpected MathException");
        }

        // Error scenario
        try {
            normalDistribution = new NormalDistributionImpl(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
            double sample = normalDistribution.sample();
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected exception
        }
    }

    // Test case for the getDomainLowerBound method
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);

        // Typical test case
        double lowerBound = normalDistribution.getDomainLowerBound(0.5);
        assertEquals(Double.NEGATIVE_INFINITY, lowerBound, 0);

        // Edge case
        lowerBound = normalDistribution.getDomainLowerBound(Double.MIN_VALUE);
        assertEquals(-Double.MAX_VALUE, lowerBound, 0);

        // Error scenario
        lowerBound = normalDistribution.getDomainLowerBound(Double.NaN);
        assertTrue(Double.isNaN(lowerBound));
    }

    // Test case for the getDomainUpperBound method
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);

        // Typical test case
        double upperBound = normalDistribution.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, upperBound, 0);

        // Edge case
        upperBound = normalDistribution.getDomainUpperBound(Double.MIN_VALUE);
        assertEquals(Double.MAX_VALUE, upperBound, 0);

        // Error scenario
        upperBound = normalDistribution.getDomainUpperBound(Double.NaN);
        assertTrue(Double.isNaN(upperBound));
    }

    // Test case for the getInitialDomain method
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);

        // Typical test case
        double initialDomain = normalDistribution.getInitialDomain(0.5);
        assertEquals(1, initialDomain, 0);

        // Edge case
        initialDomain = normalDistribution.getInitialDomain(0);
        assertEquals(0, initialDomain, 0);

        initialDomain = normalDistribution.getInitialDomain(1);
        assertEquals(2, initialDomain, 0);

        // Error scenario
        initialDomain = normalDistribution.getInitialDomain(Double.NaN);
        assertTrue(Double.isNaN(initialDomain));
    }
}