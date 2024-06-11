import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1, distribution.density(0.5), 0);
        assertEquals(0, distribution.density(1.5), 0);
        assertEquals(0, distribution.density(-0.5), 0);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0, distribution.cumulativeProbability(-0.5), 0);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0);
        assertEquals(1, distribution.cumulativeProbability(1.5), 0);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), 0);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.5, distribution.getNumericalMean(), 0);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1.0/12, distribution.getNumericalVariance(), 0);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0, distribution.getSupportLowerBound(), 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1, distribution.getSupportUpperBound(), 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double sample = distribution.sample();
        assertTrue(sample >= 0 && sample < 1);
    }
}