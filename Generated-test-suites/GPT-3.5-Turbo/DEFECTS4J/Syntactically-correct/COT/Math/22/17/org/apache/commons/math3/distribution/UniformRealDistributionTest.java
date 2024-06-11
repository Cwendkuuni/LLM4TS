import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    // Test constructor with default values
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0, distribution.getSupportLowerBound(), 0);
        assertEquals(1, distribution.getSupportUpperBound(), 0);
    }

    // Test constructor with lower bound lower than upper bound
    @Test
    public void testConstructorWithValidBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0, distribution.getSupportLowerBound(), 0);
        assertEquals(1, distribution.getSupportUpperBound(), 0);
    }

    // Test constructor with lower bound greater than or equal to upper bound
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 0);
    }

    // Test constructor with specified inverseCumAccuracy
    @Test
    public void testConstructorWithInverseCumAccuracy() {
        double inverseCumAccuracy = 0.001;
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1, inverseCumAccuracy);
        assertEquals(inverseCumAccuracy, distribution.getSolverAbsoluteAccuracy(), 0);
    }

    // Test constructor with RandomGenerator
    @Test
    public void testConstructorWithRandomGenerator() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution distribution = new UniformRealDistribution(rng, 0, 1);
        assertNotNull(distribution.getRandomGenerator());
    }

    // Test density() method with value outside the range
    @Test
    public void testDensityWithOutOfRangeValue() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0, distribution.density(-1), 0);
        assertEquals(0, distribution.density(2), 0);
    }

    // Test density() method with value within the range
    @Test
    public void testDensityWithInRangeValue() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1, distribution.density(0.5), 0);
        assertEquals(1, distribution.density(0), 0);
        assertEquals(1, distribution.density(1), 0);
    }

    // Test cumulativeProbability() method with value below the lower bound
    @Test
    public void testCumulativeProbabilityWithLowerValue() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0, distribution.cumulativeProbability(-1), 0);
        assertEquals(0, distribution.cumulativeProbability(0), 0);
    }

    // Test cumulativeProbability() method with value above the upper bound
    @Test
    public void testCumulativeProbabilityWithUpperValue() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1, distribution.cumulativeProbability(2), 0);
        assertEquals(1, distribution.cumulativeProbability(1), 0);
    }

    // Test cumulativeProbability() method with value within the range
    @Test
    public void testCumulativeProbabilityWithInRangeValue() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0);
        assertEquals(0.75, distribution.cumulativeProbability(0.75), 0);
        assertEquals(0.25, distribution.cumulativeProbability(0.25), 0);
    }

    // Test getNumericalMean() method
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.5, distribution.getNumericalMean(), 0);
    }

    // Test getNumericalVariance() method
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1.0 / 12, distribution.getNumericalVariance(), 1e-6);
    }

    // Test getSupportLowerBound() method
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0, distribution.getSupportLowerBound(), 0);
    }

    // Test getSupportUpperBound() method
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1, distribution.getSupportUpperBound(), 0);
    }

    // Test isSupportLowerBoundInclusive() method
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    // Test isSupportUpperBoundInclusive() method
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    // Test isSupportConnected() method
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertTrue(distribution.isSupportConnected());
    }

    // Test sample() method
    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double sample = distribution.sample();
        assertTrue(sample >= 0);
        assertTrue(sample < 1);
    }
}