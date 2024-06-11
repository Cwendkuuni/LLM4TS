import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.random.*;

import static org.junit.Assert.*;
import org.junit.*;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.0, dist.cumulativeProbability(-1), 0.0001);
        assertEquals(0.2, dist.cumulativeProbability(0), 0.0001);
        assertEquals(0.4, dist.cumulativeProbability(1), 0.0001);
        assertEquals(0.6, dist.cumulativeProbability(2), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(3), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(4), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(1.0, dist.upperCumulativeProbability(-1), 0.0001);
        assertEquals(0.8, dist.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.6, dist.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.4, dist.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.0, dist.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.0, dist.upperCumulativeProbability(4), 0.0001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.2, dist.probability(0), 0.0001);
        assertEquals(0.4, dist.probability(1), 0.0001);
        assertEquals(0.4, dist.probability(2), 0.0001);
        assertEquals(0.0, dist.probability(3), 0.0001);
        assertEquals(0.0, dist.probability(4), 0.0001);
    }

    @Test
    public void testNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(1.0, dist.getNumericalMean(), 0.0001);
    }

    @Test
    public void testNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.2631578947, dist.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(2, dist.getSupportLowerBound());
    }

    @Test
    public void testSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(5, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertTrue(dist.isSupportConnected());
    }

    @Test(expected = NotPositiveException.class)
    public void testNotPositiveExceptionPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(-10, 5, 2);
    }

    @Test(expected = NotPositiveException.class)
    public void testNotPositiveExceptionNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, -5, 2);
    }

    @Test(expected = NotPositiveException.class)
    public void testNotPositiveExceptionSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, -2);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException() {
        HypergeometricDistribution dist = new HypergeometricDistribution(0, 5, 2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberIsTooLargeExceptionNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 15, 2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberIsTooLargeExceptionSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 8);
    }
}