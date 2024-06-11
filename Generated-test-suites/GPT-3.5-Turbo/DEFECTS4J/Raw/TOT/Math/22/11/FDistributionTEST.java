import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;

public class FDistributionTest {

    private static final double EPSILON = 1e-9;

    @Test
    public void testConstructorWithValidInputs() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(5, fDistribution.getNumeratorDegreesOfFreedom(), EPSILON);
        assertEquals(10, fDistribution.getDenominatorDegreesOfFreedom(), EPSILON);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidNumeratorDegreesOfFreedom() {
        new FDistribution(0, 10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidDenominatorDegreesOfFreedom() {
        new FDistribution(5, 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidDegreesOfFreedom() {
        new FDistribution(0, 0);
    }

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        // Typical use case
        double density = fDistribution.density(2);
        assertEquals(0.0031355772580292695, density, EPSILON);
        
        // Edge case
        density = fDistribution.density(0);
        assertEquals(Double.POSITIVE_INFINITY, density, EPSILON);
        
        // Error scenario
        density = fDistribution.density(-1);
        assertEquals(0, density, EPSILON);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        // Typical use case
        double cumulativeProb = fDistribution.cumulativeProbability(3);
        assertEquals(0.8074335724057195, cumulativeProb, EPSILON);
        
        // Edge case
        cumulativeProb = fDistribution.cumulativeProbability(0);
        assertEquals(0, cumulativeProb, EPSILON);

        // Error scenario
        cumulativeProb = fDistribution.cumulativeProbability(-1);
        assertEquals(0, cumulativeProb, EPSILON);
    }

    @Test
    public void testGetNumericalMean() {
        // Numerator degrees of freedom is 5, denominator degrees of freedom is 10
        FDistribution fDistribution = new FDistribution(5, 10);

        // Numerator degrees of freedom is 11, denominator degrees of freedom is 5
        FDistribution fDistribution2 = new FDistribution(11, 5);

        // Numerator degrees of freedom is 1, denominator degrees of freedom is 2
        FDistribution fDistribution3 = new FDistribution(1, 2);

        // Typical use case
        double mean = fDistribution.getNumericalMean();
        assertEquals(1.25, mean, EPSILON);

        // Edge case
        mean = fDistribution2.getNumericalMean();
        assertEquals(Double.NaN, mean, EPSILON);

        // Error scenario
        mean = fDistribution3.getNumericalMean();
        assertEquals(Double.NaN, mean, EPSILON);
    }

    @Test
    public void testGetNumericalVariance() {
        // Numerator degrees of freedom is 5, denominator degrees of freedom is 10
        FDistribution fDistribution = new FDistribution(5, 10);

        // Numerator degrees of freedom is 11, denominator degrees of freedom is 5
        FDistribution fDistribution2 = new FDistribution(11, 5);

        // Numerator degrees of freedom is 1, denominator degrees of freedom is 2
        FDistribution fDistribution3 = new FDistribution(1, 2);

        // Typical use case
        double variance = fDistribution.getNumericalVariance();
        assertEquals(3.125, variance, EPSILON);

        // Edge case
        variance = fDistribution2.getNumericalVariance();
        assertEquals(Double.NaN, variance, EPSILON);

        // Error scenario
        variance = fDistribution3.getNumericalVariance();
        assertEquals(Double.NaN, variance, EPSILON);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(5, 10);
        // Lower bound of the support is always 0
        double lowerBound = fDistribution.getSupportLowerBound();
        assertEquals(0, lowerBound, EPSILON);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(5, 10);
        // Upper bound of the support is always positive infinity
        double upperBound = fDistribution.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, upperBound, EPSILON);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(5, 10);
        // Support lower bound is inclusive
        assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(5, 10);
        // Support upper bound is exclusive
        assertFalse(fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(5, 10);
        // Support of this distribution is connected
        assertTrue(fDistribution.isSupportConnected());
    }

}