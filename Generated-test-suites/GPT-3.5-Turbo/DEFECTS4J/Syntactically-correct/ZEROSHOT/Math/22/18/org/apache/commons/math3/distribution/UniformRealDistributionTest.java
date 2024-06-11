import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class UniformRealDistributionTest {
    private UniformRealDistribution distribution;

    @Before
    public void setUp() {
        distribution = new UniformRealDistribution(0, 1);
    }

    @Test
    public void testDensity() {
        // Test density when x is within the range
        assertEquals(1.0, distribution.density(0.5), 1e-9);
        assertEquals(1.0, distribution.density(0), 1e-9);
        assertEquals(1.0, distribution.density(1), 1e-9);

        // Test density when x is outside the range
        assertEquals(0.0, distribution.density(-1), 1e-9);
        assertEquals(0.0, distribution.density(2), 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        // Test cumulative probability when x is within the range
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 1e-9);
        assertEquals(0.0, distribution.cumulativeProbability(0), 1e-9);
        assertEquals(1.0, distribution.cumulativeProbability(1), 1e-9);

        // Test cumulative probability when x is outside the range
        assertEquals(0.0, distribution.cumulativeProbability(-1), 1e-9);
        assertEquals(1.0, distribution.cumulativeProbability(2), 1e-9);
        assertEquals(0.0, distribution.cumulativeProbability(0, 0.5), 1e-9);
    }

    @Test
    public void testNumericalMean() {
        assertEquals(0.5, distribution.getNumericalMean(), 1e-9);
    }

    @Test
    public void testNumericalVariance() {
        assertEquals(1.0/12.0, distribution.getNumericalVariance(), 1e-9);
    }

    @Test
    public void testSupportLowerBound() {
        assertEquals(0.0, distribution.getSupportLowerBound(), 1e-9);
    }

    @Test
    public void testSupportUpperBound() {
        assertEquals(1.0, distribution.getSupportUpperBound(), 1e-9);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        double sample = distribution.sample();
        assertTrue(sample >= 0 && sample < 1);
    }
}