import org.junit.Test;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class FDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidNumeratorDF() {
        FDistribution fDist = new FDistribution(0, 1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidDenominatorDF() {
        FDistribution fDist = new FDistribution(1, -1);
    }

    @Test
    public void testDensityPositiveX() {
        FDistribution fDist = new FDistribution(5, 3);
        double density = fDist.density(2.5);
        // Expected density = 0.3965286914
        assertEquals(0.3965286914, density, 1e-9);
    }

    @Test
    public void testDensityZeroX() {
        FDistribution fDist = new FDistribution(5, 3);
        double density = fDist.density(0);
        // Expected density = 0
        assertEquals(0, density, 0);
    }

    @Test
    public void testDensityNegativeX() {
        FDistribution fDist = new FDistribution(5, 3);
        double density = fDist.density(-2.5);
        // Expected density = 0
        assertEquals(0, density, 0);
    }

    @Test
    public void testCumulativeProbabilityPositiveX() {
        FDistribution fDist = new FDistribution(5, 3);
        double cumulativeProb = fDist.cumulativeProbability(2.5);
        // Expected cumulative probability = 0.8482324234
        assertEquals(0.8482324234, cumulativeProb, 1e-9);
    }

    @Test
    public void testCumulativeProbabilityZeroX() {
        FDistribution fDist = new FDistribution(5, 3);
        double cumulativeProb = fDist.cumulativeProbability(0);
        // Expected cumulative probability = 0
        assertEquals(0, cumulativeProb, 0);
    }

    @Test
    public void testCumulativeProbabilityNegativeX() {
        FDistribution fDist = new FDistribution(5, 3);
        double cumulativeProb = fDist.cumulativeProbability(-2.5);
        // Expected cumulative probability = 0
        assertEquals(0, cumulativeProb, 0);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(5, 3);
        double numeratorDF = fDist.getNumeratorDegreesOfFreedom();
        // Expected numerator degrees of freedom = 5
        assertEquals(5, numeratorDF, 0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(5, 3);
        double denominatorDF = fDist.getDenominatorDegreesOfFreedom();
        // Expected denominator degrees of freedom = 3
        assertEquals(3, denominatorDF, 0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDist = new FDistribution(5, 3);
        double mean = fDist.getNumericalMean();
        // Expected mean = 1.5
        assertEquals(1.5, mean, 0);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDist = new FDistribution(5, 3);
        double variance = fDist.getNumericalVariance();
        // Expected variance = 6.2380952381
        assertEquals(6.2380952381, variance, 1e-9);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDist = new FDistribution(5, 3);
        double lowerBound = fDist.getSupportLowerBound();
        // Expected lower bound = 0
        assertEquals(0, lowerBound, 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDist = new FDistribution(5, 3);
        double upperBound = fDist.getSupportUpperBound();
        // Expected upper bound = Double.POSITIVE_INFINITY
        assertEquals(Double.POSITIVE_INFINITY, upperBound, 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDist = new FDistribution(5, 3);
        boolean inclusive = fDist.isSupportLowerBoundInclusive();
        // Expected lower bound inclusive = true
        assertEquals(true, inclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDist = new FDistribution(5, 3);
        boolean inclusive = fDist.isSupportUpperBoundInclusive();
        // Expected upper bound inclusive = false
        assertEquals(false, inclusive);
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDist = new FDistribution(5, 3);
        boolean connected = fDist.isSupportConnected();
        // Expected support connected = true
        assertEquals(true, connected);
    }
}