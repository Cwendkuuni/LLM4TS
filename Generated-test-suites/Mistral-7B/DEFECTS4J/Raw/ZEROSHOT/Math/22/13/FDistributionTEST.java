import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testFDistributionConstruction() throws NotStrictlyPositiveException {
        FDistribution fd = new FDistribution(3.0, 5.0);
        assertNotNull(fd);
        assertEquals(3.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(5.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeParameters() throws NotStrictlyPositiveException {
        new FDistribution(-3.0, 5.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroParameters() throws NotStrictlyPositiveException {
        new FDistribution(0.0, 5.0);
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        double density = fd.density(2.0);
        double expected = 0.0744229452780157;
        assertEquals(expected, density, 1e-6);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        double cumProb = fd.cumulativeProbability(2.0);
        double expected = 0.948550080681672;
        assertEquals(expected, cumProb, 1e-6);
    }

    @Test
    public void testFDistributionGetSupportBounds() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        double lowerBound = fd.getSupportLowerBound();
        double upperBound = fd.getSupportUpperBound();
        assertEquals(0.0, lowerBound, 1e-6);
        assertEquals(Double.POSITIVE_INFINITY, upperBound, 1e-6);
    }

    @Test
    public void testFDistributionIsSupportInclusive() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        assertTrue(fd.isSupportLowerBoundInclusive());
        assertFalse(fd.isSupportUpperBoundInclusive());
    }

    @Test
    public void testFDistributionIsSupportConnected() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        assertTrue(fd.isSupportConnected());
    }

    @Test
    public void testFDistributionMean() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        double mean = fd.getNumericalMean();
        double expected = 3.0 / 2.0;
        assertEquals(expected, mean, 1e-6);
    }

    @Test
    public void testFDistributionVariance() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        double variance = fd.getNumericalVariance();
        double expected = 2.441382389113166;
        assertEquals(expected, variance, 1e-6);
    }

    @Test
    public void testFDistributionSample() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        RandomGenerator rng = new Well19937c();
        fd.setSeed(rng);
        for (int i = 0; i < 1000; i++) {
            double sample = fd.inverseCumulativeProbability(rng.nextDouble());
            assertTrue(sample >= 0.0);
            assertTrue(Double.isFinite(sample));
        }
    }
}