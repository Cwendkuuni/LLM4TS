import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {
    private FDistribution dist;

    @Before
    public void setUp() {
        this.dist = new FDistribution(new Well19937c(), 2.0, 3.0);
    }

    @Test
    public void testConstructor() {
        assertEquals(2.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
        assertNotNull(dist.getCumulativeProbability(0.0));
        assertNotNull(dist.getCumulativeProbability(1.0));
        assertNotNull(dist.getCumulativeProbability(2.0));
        assertNotNull(dist.getCumulativeProbability(3.0));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeDegreesOfFreedom() {
        new FDistribution(new Well19937c(), -1.0, 3.0);
    }

    @Test
    public void testDensity() {
        double eps = 1e-6;
        double x = 1.0;
        double expected = 0.43706996879305734;
        double result = dist.density(x);
        assertEquals(expected, result, eps);
    }

    @Test
    public void testCumulativeProbability() {
        double eps = 1e-6;
        double x = 1.0;
        double expected = 0.6913253522207129;
        double result = dist.cumulativeProbability(x);
        assertEquals(expected, result, eps);
    }

    @Test
    public void testInverseCumulativeProbability() {
        double eps = 1e-6;
        double p = 0.95;
        double expected = 2.444948974278317;
        double result = dist.inverseCumulativeProbability(p);
        assertEquals(expected, result, eps);
    }

    @Test
    public void testMean() {
        double expected = 1.5;
        double result = dist.getNumericalMean();
        assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testVariance() {
        double expected = 1.2121212121212121;
        double result = dist.getNumericalVariance();
        assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testSupportBounds() {
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, dist.getSupportUpperBound(), 0.0);
        assertTrue(dist.isSupportLowerBoundInclusive());
        assertFalse(dist.isSupportUpperBoundInclusive());
        assertTrue(dist.isSupportConnected());
    }
}