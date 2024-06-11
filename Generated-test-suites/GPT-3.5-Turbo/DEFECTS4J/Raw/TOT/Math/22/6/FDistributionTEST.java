import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FDistributionTest {

    private FDistribution fDistribution;

    @Before
    public void setUp() {
        fDistribution = new FDistribution(5, 7);
    }

    @Test
    public void testDensity() {
        double result = fDistribution.density(2);
        assertEquals(0.14603698160586148, result, 1e-9);
    }

    @Test
    public void testDensity_ZeroInput() {
        double result = fDistribution.density(0);
        assertEquals(0, result, 0);
    }

    @Test
    public void testCumulativeProbability() {
        double result = fDistribution.cumulativeProbability(3);
        assertEquals(0.9052845475048526, result, 1e-9);
    }

    @Test
    public void testCumulativeProbability_ZeroInput() {
        double result = fDistribution.cumulativeProbability(0);
        assertEquals(0, result, 0);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        double result = fDistribution.getNumeratorDegreesOfFreedom();
        assertEquals(5, result, 0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        double result = fDistribution.getDenominatorDegreesOfFreedom();
        assertEquals(7, result, 0);
    }

    @Test
    public void testGetNumericalMean() {
        double result = fDistribution.getNumericalMean();
        assertEquals(1.7499999999999998, result, 1e-9);
    }

    @Test
    public void testGetNumericalMean_DenominatorDFLessThan2() {
        try {
            fDistribution = new FDistribution(5, 1);
            fDistribution.getNumericalMean();
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            assertEquals(LocalizedFormats.DEGREES_OF_FREEDOM, e.getSpecifier());
        }
    }

    @Test
    public void testGetNumericalVariance() {
        double result = fDistribution.getNumericalVariance();
        assertEquals(6.923076923076923, result, 1e-9);
    }

    @Test
    public void testCalculateNumericalVariance_DenominatorDFLessThan4() {
        try {
            fDistribution = new FDistribution(5, 3);
            fDistribution.getNumericalVariance();
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            assertEquals(LocalizedFormats.DEGREES_OF_FREEDOM, e.getSpecifier());
        }
    }

    @Test
    public void testGetSupportLowerBound() {
        double result = fDistribution.getSupportLowerBound();
        assertEquals(0, result, 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        double result = fDistribution.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, result, 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        boolean result = fDistribution.isSupportLowerBoundInclusive();
        assertTrue(result);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        boolean result = fDistribution.isSupportUpperBoundInclusive();
        assertTrue(!result);
    }

    @Test
    public void testIsSupportConnected() {
        boolean result = fDistribution.isSupportConnected();
        assertTrue(result);
    }

}