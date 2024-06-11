import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public class HypergeometricDistributionTest {

    // Test constructor
    @Test
    public void testConstructor() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(5, distribution.getNumberOfSuccesses());
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(3, distribution.getSampleSize());
    }

    // Test constructor with random generator
    @Test
    public void testConstructorWithRandomGenerator() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        MockRandomGenerator mockRandomGenerator = new MockRandomGenerator();
        HypergeometricDistribution distribution = new HypergeometricDistribution(mockRandomGenerator, 10, 5, 3);
        assertEquals(5, distribution.getNumberOfSuccesses());
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(3, distribution.getSampleSize());
    }

    // Test cumulativeProbability
    @Test
    public void testCumulativeProbability() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.000001);
        assertEquals(1.0, distribution.cumulativeProbability(5), 0.000001);
        assertEquals(0.4, distribution.cumulativeProbability(2), 0.000001);
    }

    // Test getDomain
    @Test
    public void testGetDomain() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        int[] domain = distribution.getDomain(10, 5, 3);
        assertEquals(0, domain[0]);
        assertEquals(3, domain[1]);
    }

    // Test getLowerDomain
    @Test
    public void testGetLowerDomain() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        int lowerDomain = distribution.getLowerDomain(10, 5, 3);
        assertEquals(2, lowerDomain);
    }

    // Test getNumberOfSuccesses
    @Test
    public void testGetNumberOfSuccesses() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(5, distribution.getNumberOfSuccesses());
    }

    // Test getPopulationSize
    @Test
    public void testGetPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, distribution.getPopulationSize());
    }

    // Test getSampleSize
    @Test
    public void testGetSampleSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, distribution.getSampleSize());
    }

    // Test getUpperDomain
    @Test
    public void testGetUpperDomain() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        int upperDomain = distribution.getUpperDomain(5, 3);
        assertEquals(3, upperDomain);
    }

    // Test probability
    @Test
    public void testProbability() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, distribution.probability(0), 0.000001);
        assertEquals(0.0, distribution.probability(10), 0.000001);
        assertEquals(0.2142857, distribution.probability(3), 0.000001);
    }

    // Test upperCumulativeProbability
    @Test
    public void testUpperCumulativeProbability() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.000001);
        assertEquals(0.0, distribution.upperCumulativeProbability(5), 0.000001);
        assertEquals(0.6, distribution.upperCumulativeProbability(2), 0.000001);
    }

    // Test innerCumulativeProbability
    @Test
    public void testInnerCumulativeProbability() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double innerCumulativeProbability = distribution.innerCumulativeProbability(0, 3, 1);
        assertEquals(0.2142857, innerCumulativeProbability, 0.000001);
    }

    // Test getNumericalMean
    @Test
    public void testGetNumericalMean() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, distribution.getNumericalMean(), 0.000001);
    }

    // Test getNumericalVariance
    @Test
    public void testGetNumericalVariance() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.464285714, distribution.getNumericalVariance(), 0.000001);
    }

    // Test calculateNumericalVariance
    @Test
    public void testCalculateNumericalVariance() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double calculateNumericalVariance = distribution.calculateNumericalVariance();
        assertEquals(0.464285714, calculateNumericalVariance, 0.000001);
    }

    // Test getSupportLowerBound
    @Test
    public void testGetSupportLowerBound() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(2, distribution.getSupportLowerBound());
    }

    // Test getSupportUpperBound
    @Test
    public void testGetSupportUpperBound() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, distribution.getSupportUpperBound());
    }

    // Test isSupportConnected
    @Test
    public void testIsSupportConnected() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertTrue(distribution.isSupportConnected());
    }

    // Utility class for creating mock random generator
    private static class MockRandomGenerator extends RandomGenerator {
        @Override
        public void setSeed(long seed) {
        }

        @Override
        public void setSeed(int[] seed) {
        }

        @Override
        public void setSeed(int seed) {
        }

        @Override
        public void nextBytes(byte[] bytes) {
        }

        @Override
        public int nextInt() {
            return 0;
        }

        @Override
        public int nextInt(int n) {
            return 0;
        }

        @Override
        public long nextLong() {
            return 0;
        }

        @Override
        public boolean nextBoolean() {
            return false;
        }

        @Override
        public float nextFloat() {
            return 0;
        }

        @Override
        public double nextDouble() {
            return 0;
        }

        @Override
        public double nextGaussian() {
            return 0;
        }
    }
}