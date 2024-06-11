package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructors() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;

        // Test constructor with default random number generator
        HypergeometricDistribution hd1 = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, hd1.getPopulationSize());
        assertEquals(numberOfSuccesses, hd1.getNumberOfSuccesses());
        assertEquals(sampleSize, hd1.getSampleSize());

        // Test constructor with specified random number generator
        Well19937c rng = new Well19937c();
        HypergeometricDistribution hd2 = new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
        assertEquals(rng, hd2.getRandomGenerator());
        assertEquals(populationSize, hd2.getPopulationSize());
        assertEquals(numberOfSuccesses, hd2.getNumberOfSuccesses());
        assertEquals(sampleSize, hd2.getSampleSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testNumberOfSuccessesNegative() {
        HypergeometricDistribution hd = new HypergeometricDistribution(1, -1, 1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testPopulationSizeZero() {
        HypergeometricDistribution hd = new HypergeometricDistribution(1, 1, 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testPopulationSizeNegative() {
        HypergeometricDistribution hd = new HypergeometricDistribution(1, 1, -1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberOfSuccessesGreaterThanPopulationSize() {
        HypergeometricDistribution hd = new HypergeometricDistribution(1, 2, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleSizeGreaterThanPopulationSize() {
        HypergeometricDistribution hd = new HypergeometricDistribution(1, 1, 2);
    }

    @Test
    public void testCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] expectedCumulativeProbabilities = { 0.0, 0.003269935, 0.01695625, 0.04127969, 0.06183263, 0.11856757, 0.1369426, 0.20982747, 0.26403734, 0.32341693, 0.35565703, 0.40225138, 0.42215447, 0.46382321, 0.48772679, 0.50848617, 0.53412645, 0.54810735, 0.56182348, 0.57377393, 0.58272404, 0.59013779, 0.59586844, 0.60023527, 0.60339848, 0.60554302, 0.60684953, 0.60754731, 0.60780981, 0.60785636, 0.60779112, 0.60758552, 0.60696495, 0.60585029, 0.60371252, 0.60069891, 0.59579551, 0.58849238, 0.57748661, 0.56338683, 0.54556575, 0.52217616, 0.4877931, 0.44774144, 0.39371865, 0.32272975, 0.23086759, 0.11936167, 0.003269935, 0.0 };

        for (int i = 0; i < expectedCumulativeProbabilities.length; i++) {
            assertEquals(expectedCumulativeProbabilities[i], hd.cumulativeProbability(i), 1e-12);
        }
    }

    @Test
    public void testProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double[] expectedProbabilities = { 0.03269935, 0.01695625, 0.04127969, 0.06183263, 0.11856757, 0.1369426, 0.16842556, 0.19083508, 0.20982747, 0.23337884, 0.24679279, 0.25742359, 0.26403734, 0.26696259, 0.26679194, 0.26427939, 0.25863061, 0.24848491, 0.23381294, 0.21376814, 0.18618727, 0.15376187, 0.11856757, 0.08186055, 0.04127969, 0.01695625, 0.003269935, 0.0 };

        for (int i = 0; i < expectedProbabilities.length; i++) {
            assertEquals(expectedProbabilities[i], hd.probability(i), 1e-12);
        }
    }

    @Test
    public void testUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double[] expectedUpperCumulativeProbabilities = { 0.99673055, 0.99382956, 0.99022069, 0.98611916, 0.98217289, 0.97773045, 0.97287405, 0.96795538, 0.96224973, 0.95598007, 0.94839033, 0.9398517, 0.93088199, 0.92134769, 0.90997784, 0.89732765, 0.88246976, 0.86538053, 0.84596475, 0.81465964, 0.77285902, 0.69746076, 0.003269935, 0.0 };

        for (int i = 0; i < expectedUpperCumulativeProbabilities.length; i++) {
            assertEquals(expectedUpperCumulativeProbabilities[i], hd.upperCumulativeProbability(i), 1e-12);
        }
    }

    @Test
    public void testNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double expectedNumericalMean = (double) sampleSize * numberOfSuccesses / (double) populationSize;
        assertEquals(expectedNumericalMean, hd.getNumericalMean(), 1e-12);
    }

    @Test
    public void testNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double expectedNumericalVariance = (double) sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses) / (double) (populationSize * populationSize * (populationSize - 1));
        assertEquals(expectedNumericalVariance, hd.getNumericalVariance(), 1e-12);
    }

    @Test
    public void testSupportLowerBound() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int expectedSupportLowerBound = populationSize + numberOfSuccesses - sampleSize;
        assertEquals(expectedSupportLowerBound, hd.getSupportLowerBound());
    }

    @Test
    public void testSupportUpperBound() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int expectedSupportUpperBound = Math.min(numberOfSuccesses, sampleSize);
        assertEquals(expectedSupportUpperBound, hd.getSupportUpperBound());
    }
}