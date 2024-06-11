import org.apache.commons.math3.distribution.FDistribution;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    // Test constructor for valid degrees of freedom inputs
    @Test
    public void testConstructorValidDegreesOfFreedom() {
        FDistribution fdistr = new FDistribution(5, 10);

        assertEquals(5, fdistr.getNumeratorDegreesOfFreedom(), 0);
        assertEquals(10, fdistr.getDenominatorDegreesOfFreedom(), 0);
    }

    // Test constructor for invalid degrees of freedom inputs (negative values)
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidDegreesOfFreedom() {
        FDistribution fdistr = new FDistribution(-1, 10);
    }

    // Test density calculation for valid input
    @Test
    public void testDensityWithValidInput() {
        FDistribution fdistr = new FDistribution(5, 10);
        double x = 1.5;
        double expectedDensity = 0.2398160673;

        assertEquals(expectedDensity, fdistr.density(x), 1e-9);
    }

    // Test density calculation for valid input (edge case: x = 0)
    @Test
    public void testDensityWithValidInputEdgeCase1() {
        FDistribution fdistr = new FDistribution(5, 10);
        double x = 0;
        double expectedDensity = 0;

        assertEquals(expectedDensity, fdistr.density(x), 1e-9);
    }

    // Test density calculation for valid input (edge case: x = infinity)
    @Test
    public void testDensityWithValidInputEdgeCase2() {
        FDistribution fdistr = new FDistribution(5, 10);
        double x = Double.POSITIVE_INFINITY;
        double expectedDensity = 0;

        assertEquals(expectedDensity, fdistr.density(x), 1e-9);
    }

    // Test cumulative probability calculation for valid input
    @Test
    public void testCumulativeProbabilityWithValidInput() {
        FDistribution fdistr = new FDistribution(5, 10);
        double x = 1.5;
        double expectedProbability = 0.3776730405;

        assertEquals(expectedProbability, fdistr.cumulativeProbability(x), 1e-9);
    }

    // Test cumulative probability calculation for valid input (edge case: x = 0)
    @Test
    public void testCumulativeProbabilityWithValidInputEdgeCase1() {
        FDistribution fdistr = new FDistribution(5, 10);
        double x = 0;
        double expectedProbability = 0;

        assertEquals(expectedProbability, fdistr.cumulativeProbability(x), 1e-9);
    }

    // Test cumulative probability calculation for valid input (edge case: x = infinity)
    @Test
    public void testCumulativeProbabilityWithValidInputEdgeCase2() {
        FDistribution fdistr = new FDistribution(5, 10);
        double x = Double.POSITIVE_INFINITY;
        double expectedProbability = 1;

        assertEquals(expectedProbability, fdistr.cumulativeProbability(x), 1e-9);
    }

    // Test numerical mean calculation for valid input
    @Test
    public void testGetNumericalMeanWithValidInput() {
        FDistribution fdistr = new FDistribution(5, 10);
        double expectedMean = 1.25;

        assertEquals(expectedMean, fdistr.getNumericalMean(), 1e-9);
    }

    // Test numerical mean calculation for valid input (edge case: denominator degrees of freedom <= 2)
    @Test
    public void testGetNumericalMeanWithValidInputEdgeCase1() {
        FDistribution fdistr = new FDistribution(5, 2);
        double expectedMean = Double.NaN;

        assertEquals(expectedMean, fdistr.getNumericalMean(), 1e-9);
    }

    // Test numerical variance calculation for valid input
    @Test
    public void testGetNumericalVarianceWithValidInput() {
        FDistribution fdistr = new FDistribution(5, 10);
        double expectedVariance = 3.75;

        assertEquals(expectedVariance, fdistr.getNumericalVariance(), 1e-9);
    }

    // Test numerical variance calculation for valid input (edge case: denominator degrees of freedom <= 4)
    @Test
    public void testGetNumericalVarianceWithValidInputEdgeCase1() {
        FDistribution fdistr = new FDistribution(5, 4);
        double expectedVariance = Double.NaN;

        assertEquals(expectedVariance, fdistr.getNumericalVariance(), 1e-9);
    }

    // Test support lower bound
    @Test
    public void testGetSupportLowerBound() {
        FDistribution fdistr = new FDistribution(5, 10);
        double expectedLowerBound = 0;

        assertEquals(expectedLowerBound, fdistr.getSupportLowerBound(), 0);
    }

    // Test support upper bound
    @Test
    public void testGetSupportUpperBound() {
        FDistribution fdistr = new FDistribution(5, 10);
        double expectedUpperBound = Double.POSITIVE_INFINITY;

        assertEquals(expectedUpperBound, fdistr.getSupportUpperBound(), 0);
    }

    // Test if lower bound inclusive
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fdistr = new FDistribution(5, 10);

        assertTrue(fdistr.isSupportLowerBoundInclusive());
    }

    // Test if upper bound inclusive
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fdistr = new FDistribution(5, 10);

        assertFalse(fdistr.isSupportUpperBoundInclusive());
    }

    // Test if support is connected
    @Test
    public void testIsSupportConnected() {
        FDistribution fdistr = new FDistribution(5, 10);

        assertTrue(fdistr.isSupportConnected());
    }
}