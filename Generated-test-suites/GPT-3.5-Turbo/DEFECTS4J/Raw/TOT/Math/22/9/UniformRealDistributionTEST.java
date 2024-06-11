import org.junit.Test;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.distribution.UniformRealDistribution;

import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    // Test cumulativeProbability()

    // Typical use case
    @Test
    public void testCumulativeProbability_typical() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        double x = 0.5;
        double expected = 0.5;
        double actual = urd.cumulativeProbability(x);
        assertEquals(expected, actual, 0.0001);
    }

    // Edge case: x equals the lower bound
    @Test
    public void testCumulativeProbability_edgeLower() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        double x = 0.0;
        double expected = 0.0;
        double actual = urd.cumulativeProbability(x);
        assertEquals(expected, actual, 0.0001);
    }

    // Edge case: x equals the upper bound
    @Test
    public void testCumulativeProbability_edgeUpper() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        double x = 1.0;
        double expected = 1.0;
        double actual = urd.cumulativeProbability(x);
        assertEquals(expected, actual, 0.0001);
    }

    // Test density()

    // Typical use case
    @Test
    public void testDensity_typical() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        double x = 0.5;
        double expected = 1.0;
        double actual = urd.density(x);
        assertEquals(expected, actual, 0.0001);
    }

    // Edge case: x equals the lower bound
    @Test
    public void testDensity_edgeLower() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        double x = 0.0;
        double expected = 1.0;
        double actual = urd.density(x);
        assertEquals(expected, actual, 0.0001);
    }

    // Edge case: x equals the upper bound
    @Test
    public void testDensity_edgeUpper() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        double x = 1.0;
        double expected = 1.0;
        double actual = urd.density(x);
        assertEquals(expected, actual, 0.0001);
    }

    // Test getNumericalMean()

    // Typical use case
    @Test
    public void testGetNumericalMean_typical() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        double expected = 0.5;
        double actual = urd.getNumericalMean();
        assertEquals(expected, actual, 0.0001);
    }

    // Test getNumericalVariance()

    // Typical use case
    @Test
    public void testGetNumericalVariance_typical() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        double expected = 1.0 / 12.0;
        double actual = urd.getNumericalVariance();
        assertEquals(expected, actual, 0.0001);
    }

    // Test getSupportLowerBound()

    // Typical use case
    @Test
    public void testGetSupportLowerBound_typical() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        double expected = 0.0;
        double actual = urd.getSupportLowerBound();
        assertEquals(expected, actual, 0.0001);
    }

    // Test getSupportUpperBound()

    // Typical use case
    @Test
    public void testGetSupportUpperBound_typical() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        double expected = 1.0;
        double actual = urd.getSupportUpperBound();
        assertEquals(expected, actual, 0.0001);
    }

    // Test isSupportLowerBoundInclusive()

    // Typical use case
    @Test
    public void testIsSupportLowerBoundInclusive_typical() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        assertTrue(urd.isSupportLowerBoundInclusive());
    }

    // Test isSupportUpperBoundInclusive()

    // Typical use case
    @Test
    public void testIsSupportUpperBoundInclusive_typical() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        assertFalse(urd.isSupportUpperBoundInclusive());
    }

    // Test isSupportConnected()

    // Typical use case
    @Test
    public void testIsSupportConnected_typical() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        assertTrue(urd.isSupportConnected());
    }

    // Test sample()

    // Typical use case
    @Test
    public void testSample_typical() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        double sample = urd.sample();
        assertTrue(sample >= 0.0 && sample < 1.0);
    }

    // Test constructors

    // Typical use case
    @Test
    public void testConstructor_typical() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        assertEquals(0.0, urd.getSupportLowerBound(), 0.0001);
        assertEquals(1.0, urd.getSupportUpperBound(), 0.0001);
        assertEquals(0.5, urd.getNumericalMean(), 0.0001);
        assertEquals(1.0 / 12.0, urd.getNumericalVariance(), 0.0001);
        assertTrue(urd.isSupportLowerBoundInclusive());
        assertFalse(urd.isSupportUpperBoundInclusive());
        assertTrue(urd.isSupportConnected());
    }

    // Error scenario: lower bound is greater than or equal to the upper bound
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_errorBounds() {
        new UniformRealDistribution(2, 1);
    }

}