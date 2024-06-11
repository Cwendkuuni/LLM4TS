import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.junit.Test;

public class NormalDistributionImplTest {

    private static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1e-9;

    @Test
    public void testDefaultConstructor() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        assertEquals(0, normal.getMean(), 0);
        assertEquals(1, normal.getStandardDeviation(), 0);
    }

    @Test
    public void testConstructorWithMeanAndSD() {
        NormalDistributionImpl normal = new NormalDistributionImpl(5, 2.5);
        assertEquals(5, normal.getMean(), 0);
        assertEquals(2.5, normal.getStandardDeviation(), 0);
    }

    @Test(expected = Exception.class)
    public void testConstructorWithZeroSD() {
        NormalDistributionImpl normal = new NormalDistributionImpl(5, 0);
        fail("Should have thrown NotStrictlyPositiveException");
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        double result = normal.density(0.5);
        assertEquals(0.352065, result, 0.000001);
    }

    @Test
    public void testDensityWithNegativeX() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        double result = normal.density(-1.5);
        assertEquals(0.129518, result, 0.000001);
    }

    @Test
    public void testDensityWithLargeX() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        double result = normal.density(5);
        assertEquals(1.48672e-06, result, 1e-12);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        double result = normal.cumulativeProbability(1.5);
        assertEquals(0.933192, result, 0.000001);
    }

    @Test
    public void testCumulativeProbabilityWithNegativeX() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        double result = normal.cumulativeProbability(-1.5);
        assertEquals(0.066807, result, 0.000001);
    }

    @Test
    public void testCumulativeProbabilityWithLargeX() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        double result = normal.cumulativeProbability(5);
        assertEquals(0.999999, result, 0.000001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        double result = normal.inverseCumulativeProbability(0.5);
        assertEquals(0, result, DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityWithInvalidProbability() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        double result = normal.inverseCumulativeProbability(1.5);
        fail("Should have thrown OutOfRangeException");
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        double result = normal.sample();
        assertTrue(result >= Double.NEGATIVE_INFINITY && result <= Double.POSITIVE_INFINITY);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        double result = normal.getSolverAbsoluteAccuracy();
        assertEquals(DEFAULT_INVERSE_ABSOLUTE_ACCURACY, result, 0);
    }

}