import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UniformRealDistributionTest {

    // Test 1: Test the constructor with default values
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0001);
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.0001);
        assertEquals(0.0, distribution.getNumericalMean(), 0.0001);
        assertEquals(1.0 / 12, distribution.getNumericalVariance(), 0.0001);
        assertEquals(true, distribution.isSupportLowerBoundInclusive());
        assertEquals(false, distribution.isSupportUpperBoundInclusive());
        assertEquals(true, distribution.isSupportConnected());
    }

    // Test 2: Test the constructor with valid lower and upper bounds
    @Test
    public void testValidConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution(1.0, 5.0);
        assertEquals(1.0, distribution.getSupportLowerBound(), 0.0001);
        assertEquals(5.0, distribution.getSupportUpperBound(), 0.0001);
        assertEquals(3.0, distribution.getNumericalMean(), 0.0001);
        assertEquals(4.0 / 12, distribution.getNumericalVariance(), 0.0001);
        assertEquals(true, distribution.isSupportLowerBoundInclusive());
        assertEquals(false, distribution.isSupportUpperBoundInclusive());
        assertEquals(true, distribution.isSupportConnected());
    }

    // Test 3: Test the constructor with invalid lower and upper bounds
    @Test
    public void testInvalidConstructor() {
        assertThrows(NumberIsTooLargeException.class, () -> {
            UniformRealDistribution distribution = new UniformRealDistribution(5.0, 1.0);
        });
    }

    // Test 4: Test the density function with values inside the range
    @Test
    public void testDensityInsideRange() {
        UniformRealDistribution distribution = new UniformRealDistribution(1.0, 5.0);
        assertEquals(0.25, distribution.density(2.0), 0.0001);
    }

    // Test 5: Test the density function with values outside the range
    @Test
    public void testDensityOutsideRange() {
        UniformRealDistribution distribution = new UniformRealDistribution(1.0, 5.0);
        assertEquals(0.0, distribution.density(6.0), 0.0001);
    }

    // Test 6: Test the cumulativeProbability function with values inside the range
    @Test
    public void testCumulativeProbabilityInsideRange() {
        UniformRealDistribution distribution = new UniformRealDistribution(1.0, 5.0);
        assertEquals(0.5, distribution.cumulativeProbability(3.0), 0.0001);
    }

    // Test 7: Test the cumulativeProbability function with values outside the range
    @Test
    public void testCumulativeProbabilityOutsideRange() {
        UniformRealDistribution distribution = new UniformRealDistribution(1.0, 5.0);
        assertEquals(1.0, distribution.cumulativeProbability(10.0), 0.0001);
    }

    // Test 8: Test the sample function with valid lower and upper bounds
    @Test
    public void testSampleWithValidBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(1.0, 5.0);
        double value = distribution.sample();
        assertEquals(true, value >= 1.0 && value < 5.0);
    }

    // Test 9: Test the sample function with invalid lower and upper bounds
    @Test
    public void testSampleWithInvalidBounds() {
        assertThrows(NumberIsTooLargeException.class, () -> {
            UniformRealDistribution distribution = new UniformRealDistribution(5.0, 1.0);
            double value = distribution.sample();
        });
    }
}