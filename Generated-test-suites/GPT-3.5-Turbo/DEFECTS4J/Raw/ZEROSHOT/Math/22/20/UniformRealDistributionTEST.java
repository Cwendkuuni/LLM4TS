import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;

    @Before
    public void setUp() {
        distribution = new UniformRealDistribution(0, 1);
    }

    @Test
    public void testDensity() {
        assertEquals(0.0, distribution.density(-0.5), 0.0001);
        assertEquals(0.5, distribution.density(0.5), 0.0001);
        assertEquals(0.0, distribution.density(1.5), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.0, distribution.cumulativeProbability(-0.5), 0.0001);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(1.5), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(0.5, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(0.083333333, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0001);
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.0001);
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
        assertTrue(sample >= 0.0 && sample < 1.0);
    }
}