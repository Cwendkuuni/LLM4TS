import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        assertEquals(1, urd.density(0.5), 0.0001);
        assertEquals(0, urd.density(1.5), 0.0001);
        assertEquals(0, urd.density(-0.5), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        assertEquals(0, urd.cumulativeProbability(-0.5), 0.0001);
        assertEquals(0, urd.cumulativeProbability(0), 0.0001);
        assertEquals(0.5, urd.cumulativeProbability(0.5), 0.0001);
        assertEquals(1, urd.cumulativeProbability(1), 0.0001);
        assertEquals(1, urd.cumulativeProbability(1.5), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        assertEquals(0.5, urd.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        assertEquals(0.0833, urd.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        assertEquals(0, urd.getSupportLowerBound(), 0.0001);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        assertEquals(1, urd.getSupportUpperBound(), 0.0001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        assertTrue(urd.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        assertFalse(urd.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        assertTrue(urd.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        double value = urd.sample();
        assertTrue(value >= 0 && value < 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorException() {
        new UniformRealDistribution(1, 0);
    }
}