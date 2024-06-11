import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWhenNegativeDegreesOfFreedom() {
        new FDistribution(-1.0, -1.0);
    }

    @Test
    public void testConstructorWithValidParameters() {
        RandomGenerator rng = new Well19937c();
        FDistribution dist = new FDistribution(rng, 3.0, 4.0);
        assertEquals(3.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testDensity() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        double x = 0.5;
        double density = dist.density(x);
        double expectedDensity = 1.0 / (2.0 * Math.PI) * Math.pow(x, 0.5) / Math.pow(2.0, 2.5) / Math.sqrt(1.0 + x);
        assertEquals(Precision.abs(density, 1e-8), expectedDensity, 1e-8);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        double x = 1.0;
        double cumProb = dist.cumulativeProbability(x);
        double expectedCumProb = Beta.regularizedBeta(1.0, 0.5, 0.5, 1.0);
        assertEquals(Precision.abs(cumProb - expectedCumProb, 1e-8), 0.0, 1e-8);

        x = 0.5;
        cumProb = dist.cumulativeProbability(x);
        expectedCumProb = Beta.regularizedBeta(0.5, 0.5, 1.0, 1.0);
        assertEquals(Precision.abs(cumProb - expectedCumProb, 1e-8), 0.5, 1e-8);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        double degreesOfFreedom = dist.getNumeratorDegreesOfFreedom();
        assertEquals(1.0, degreesOfFreedom, 0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        double degreesOfFreedom = dist.getDenominatorDegreesOfFreedom();
        assertEquals(2.0, degreesOfFreedom, 0.0);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        double accuracy = dist.getSolverAbsoluteAccuracy();
        assertEquals(FDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, accuracy, 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        double mean = dist.getNumericalMean();
        assertEquals(1.5, mean, 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        double variance = dist.getNumericalVariance();
        assertEquals(1.5, variance, 0.0);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        double lowerBound = dist.getSupportLowerBound();
        assertEquals(0.0, lowerBound, 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        double upperBound = dist.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, upperBound, 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        boolean isInclusive = dist.isSupportLowerBoundInclusive();
        assertTrue(isInclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        boolean isInclusive = dist.isSupportUpperBoundInclusive();
        assertFalse(isInclusive);
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution dist = new FDistribution(1.0, 2.0);
        boolean isConnected = dist.isSupportConnected();
        assertTrue(isConnected);
    }

    @Test
    public void testCalculateNumericalVariance() {
        FDistribution dist = new FDistribution(1.0, 3.0);
        double variance = dist.calculateNumericalVariance();
        assertEquals(2.0 / 3.0, variance, 0.0);
    }
}