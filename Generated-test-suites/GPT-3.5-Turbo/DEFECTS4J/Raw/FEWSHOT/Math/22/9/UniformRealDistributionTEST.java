import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UniformRealDistributionTest {
    private UniformRealDistribution distribution;

    @Before
    public void setUp() {
        distribution = new UniformRealDistribution(0, 1);
    }

    @Test
    public void testDensity() {
        assertEquals(0.0, distribution.density(-0.5), 0.0);
        assertEquals(1.0, distribution.density(0.5), 0.0);
        assertEquals(0.0, distribution.density(1.5), 0.0);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.0, distribution.cumulativeProbability(-0.5), 0.0);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0.0);
        assertEquals(1.0, distribution.cumulativeProbability(1.5), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(0.5, distribution.getNumericalMean(), 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(1.0 / 12.0, distribution.getNumericalVariance(), 0.0);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertEquals(true, distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertEquals(false, distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        assertEquals(true, distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        double sample = distribution.sample();
        assertEquals(true, sample >= 0.0 && sample < 1.0);
    }
}