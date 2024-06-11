import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FDistributionTest {

    @Test
    public void testFDistributionConstructor() {
        try {
            new FDistribution(1.0, 1.0);
        } catch (NotStrictlyPositiveException e) {
            assertTrue(true);
        }

        try {
            new FDistribution(-1.0, 1.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            assertTrue(true);
        }

        try {
            new FDistribution(1.0, -1.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            assertTrue(true);
        }

        new FDistribution(2.0, 3.0);
    }

    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        RealDistribution rd = new FDistribution(1.0, 1.0);

        double result = fd.density(1.5);
        double expected = 0.240628551236158;
        assertEquals(expected, result, 1e-6);

        result = fd.density(0.5);
        expected = 1.31830292543223;
        assertEquals(expected, result, 1e-6);

        result = fd.density(5.0);
        expected = 0.00051801739818185;
        assertEquals(expected, result, 1e-6);

        result = rd.density(0.5);
        expected = 1.0 / Math.sqrt(Math.PI * 2);
        assertEquals(expected, result, 1e-6);

        result = rd.density(1.5);
        expected = 0.398942280401433;
        assertEquals(expected, result, 1e-6);

        result = rd.density(5.0);
        expected = 0.00053038757236122;
        assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(2.0, 3.0);

        double result = fd.cumulativeProbability(0.5);
        double expected = 0.306941184436385;
        assertEquals(expected, result, 1e-6);

        result = fd.cumulativeProbability(1.5);
        expected = 0.948573482368242;
        assertEquals(expected, result, 1e-6);

        result = fd.cumulativeProbability(5.0);
        expected = 1.0;
        assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double result = fd.getNumeratorDegreesOfFreedom();
        double expected = 2.0;
        assertEquals(expected, result);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double result = fd.getDenominatorDegreesOfFreedom();
        double expected = 3.0;
        assertEquals(expected, result);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double result = fd.getSolverAbsoluteAccuracy();
        // Default value
        double expected = 1e-9;
        assertEquals(expected, result, 1e-9);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double result = fd.getNumericalMean();
        double expected = 3.0 / 2.0;
        assertEquals(expected, result);
    }

    @Test(expected = java.lang.ArithmeticException.class)
    public void testGetNumericalMeanWithInvalidDegreesOfFreedom() {
        FDistribution fd = new FDistribution(2.0, 1.0);
        fd.getNumericalMean();
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double result = fd.getNumericalVariance();
        double expected = 2.12132034355964;
        assertEquals(expected, result, 1e-6);
    }

    @Test(expected = java.lang.ArithmeticException.class)
    public void testGetNumericalVarianceWithInvalidDegreesOfFreedom() {
        FDistribution fd = new FDistribution(2.0, 2.0);
        fd.getNumericalVariance();
    }

    @Test
    public void testSupportBounds() {
        FDistribution fd = new FDistribution(2.0, 3.0);

        double lowerBound = fd.getSupportLowerBound();
        double upperBound = fd.getSupportUpperBound();

        assertTrue(lowerBound >= 0);
        assertFalse(upperBound <= 0);
        assertTrue(Double.isInfinite(upperBound));

        assertEquals(0, lowerBound, 1e-6);
        assertEquals(Double.POSITIVE_INFINITY, upperBound, 1e-6);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        assertTrue(fd.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        assertFalse(fd.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        assertTrue(fd.isSupportConnected());
    }
}