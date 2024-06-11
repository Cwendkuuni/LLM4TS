import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    private FDistribution distribution;

    @Before
    public void setUp() throws NotStrictlyPositiveException {
        distribution = new FDistribution(5.0, 10.0);
    }

    // Test constructor with valid parameters
    @Test
    public void testFDistributionConstructorValidParameters() {
        assertInstanceOf(FDistribution.class, distribution);
    }

    // Test method density with valid input
    @Test
    public void testDensityValidInput() {
        double density = distribution.density(1.5);
        assertNotNull(density);
    }

    // Test method density with negative input
    @Test(expected = IllegalArgumentException.class)
    public void testDensityNegativeInput() {
        distribution.density(-1.0);
    }

    // Test method cumulativeProbability with valid input
    @Test
    public void testCumulativeProbabilityValidInput() {
        double cumulativeProbability = distribution.cumulativeProbability(1.5);
        assertNotNull(cumulativeProbability);
    }

    // Test method cumulativeProbability with invalid input (less than 0)
    @Test(expected = IllegalArgumentException.class)
    public void testCumulativeProbabilityInvalidInput() {
        distribution.cumulativeProbability(-1.0);
    }

    // Test method getNumeratorDegreesOfFreedom with valid input
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        assertEquals(5.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    // Test method getDenominatorDegreesOfFreedom with valid input
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        assertEquals(10.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    // Test method getSolverAbsoluteAccuracy with valid input
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        assertEquals(DEFAULT_INVERSE_ABSOLUTE_ACCURACY, distribution.getSolverAbsoluteAccuracy(), 0.0);
    }

    // Test method getNumericalMean with valid input
    @Test
    public void testGetNumericalMean() {
        assertEquals(3.0, distribution.getNumericalMean(), 0.0);
    }

    // Test method getNumericalMean with invalid input (denominator degrees of freedom <= 2)
    @Test
    public void testGetNumericalMeanInvalidInput() {
        FDistribution invalidDistribution = new FDistribution(3.0, 1.0);
        assertEquals(Double.NaN, invalidDistribution.getNumericalMean(), 0.0);
    }

    // Test method getNumericalVariance with valid input
    @Test
    public void testGetNumericalVariance() {
        double numericalVariance = distribution.getNumericalVariance();
        assertNotNull(numericalVariance);
    }

    // Test method getNumericalVariance with invalid input (denominator degrees of freedom <= 4)
    @Test
    public void testGetNumericalVarianceInvalidInput() {
        FDistribution invalidDistribution = new FDistribution(3.0, 1.0);
        assertEquals(Double.NaN, invalidDistribution.getNumericalVariance(), 0.0);
    }

    // Test method getSupportLowerBound with valid input
    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
    }

    // Test method getSupportUpperBound with valid input
    @Test
    public void testGetSupportUpperBound() {
        assertEquals(Double.POSITIVE_INFINITY, distribution.getSupportUpperBound(), 0.0);
    }

    // Test method isSupportLowerBoundInclusive with valid input
    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    // Test method isSupportUpperBoundInclusive with valid input
    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    // Test method isSupportConnected with valid input
    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }
}