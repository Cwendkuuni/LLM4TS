import org.junit.Test;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FDistributionTest {

    @Test
    public void testConstructor() {
        // Test valid input
        try {
            FDistribution fDistribution = new FDistribution(5, 10);
            assertEquals(5.0, fDistribution.getNumeratorDegreesOfFreedom(), 0);
            assertEquals(10.0, fDistribution.getDenominatorDegreesOfFreedom(), 0);
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException should not have been thrown");
        }

        // Test invalid input (negative numerator degrees of freedom)
        try {
            FDistribution fDistribution = new FDistribution(-5, 10);
            fail("NotStrictlyPositiveException should have been thrown");
        } catch (NotStrictlyPositiveException e) {
            assertTrue(true);
        }

        // Test invalid input (negative denominator degrees of freedom)
        try {
            FDistribution fDistribution = new FDistribution(5, -10);
            fail("NotStrictlyPositiveException should have been thrown");
        } catch (NotStrictlyPositiveException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        // Test density at x = 1 (typical use case)
        double density = fDistribution.density(1);
        assertEquals(0.16331160853767812, density, 1e-9);
        
        // Test density at x = 0 (edge case)
        density = fDistribution.density(0);
        assertEquals(0, density, 0);
        
        // Test density at x = -1 (error scenario)
        density = fDistribution.density(-1);
        assertEquals(Double.NaN, density, 0);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        // Test cumulative probability at x = 1 (typical use case)
        double probability = fDistribution.cumulativeProbability(1);
        assertEquals(0.5521051295052294, probability, 1e-9);
        
        // Test cumulative probability at x = 0 (edge case)
        probability = fDistribution.cumulativeProbability(0);
        assertEquals(0, probability, 0);

        // Test cumulative probability at x = -1 (error scenario)
        probability = fDistribution.cumulativeProbability(-1);
        assertEquals(0, probability, 0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        // Test numerical mean (typical use case)
        double mean = fDistribution.getNumericalMean();
        assertEquals(1.25, mean, 0);
        
        // Test numerical mean (edge case)
        fDistribution = new FDistribution(2, 10);
        mean = fDistribution.getNumericalMean();
        assertEquals(Double.NaN, mean, 0);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        // Test numerical variance (typical use case)
        double variance = fDistribution.getNumericalVariance();
        assertEquals(10, variance, 0);
        
        // Test numerical variance (edge case)
        fDistribution = new FDistribution(2, 10);
        variance = fDistribution.getNumericalVariance();
        assertEquals(Double.NaN, variance, 0);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        // Test support lower bound
        double lowerBound = fDistribution.getSupportLowerBound();
        assertEquals(0, lowerBound, 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        // Test support upper bound
        double upperBound = fDistribution.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, upperBound, 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        // Test if support lower bound is inclusive
        boolean isLowerBoundInclusive = fDistribution.isSupportLowerBoundInclusive();
        assertTrue(isLowerBoundInclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        // Test if support upper bound is inclusive
        boolean isUpperBoundInclusive = fDistribution.isSupportUpperBoundInclusive();
        assertTrue(!isUpperBoundInclusive);
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        // Test if support is connected
        boolean isSupportConnected = fDistribution.isSupportConnected();
        assertTrue(isSupportConnected);
    }

}