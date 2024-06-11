import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DiscreteDistributionTest {
    private static final double EPSILON = 1e-10;
    private DiscreteDistribution<Integer> distribution;
    private RandomGenerator rng;

    @Before
    public void setUp() {
        rng = new Well19937c(0);
    }

    @Test
    public void testConstructorThrowsNotPositiveException() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.5));
        assertThrows(NotPositiveException.class, () ->
                new DiscreteDistribution<>(samples));
    }

    @Test
    public void testConstructorThrowsMathArithmeticException() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.5));
        assertThrows(MathArithmeticException.class, () ->
                new DiscreteDistribution<>(samples));
    }

    @Test
    public void testConstructorThrowsMathIllegalArgumentException() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        assertThrows(MathIllegalArgumentException.class, () ->
                new DiscreteDistribution<>(samples));
    }

    @Test
    public void testConstructor() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.5));
        distribution = new DiscreteDistribution<>(samples);
        assertEquals(samples, distribution.getSamples());
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.5));
        distribution = new DiscreteDistribution<>(samples);
        distribution.reseedRandomGenerator(1234);
        // Test that the same value is generated after reseeding
        assertEquals(1, (int) distribution.sample());
        assertEquals(2, (int) distribution.sample());
    }

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        distribution = new DiscreteDistribution<>(samples);
        assertEquals(0.2, distribution.probability(1), EPSILON);
        assertEquals(0.3, distribution.probability(2), EPSILON);
        assertEquals(0.5, distribution.probability(3), EPSILON);
        assertEquals(0.0, distribution.probability(0), EPSILON); // Value not in samples
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        distribution = new DiscreteDistribution<>(samples);
        int numSamples = 1000;
        int[] sampleCounts = new int[3];
        for (int i = 0; i < numSamples; i++) {
            int sample = distribution.sample();
            switch (sample) {
                case 1:
                    sampleCounts[0]++;
                    break;
                case 2:
                    sampleCounts[1]++;
                    break;
                case 3:
                    sampleCounts[2]++;
                    break;
            }
        }
        double[] sampleProbabilities = new double[3];
        for (int i = 0; i < sampleProbabilities.length; i++) {
            sampleProbabilities[i] = (double) sampleCounts[i] / numSamples;
        }
        assertArrayEquals(samples.stream().mapToDouble(Pair::getValue).toArray(), sampleProbabilities, EPSILON);
    }

    @Test
    public void testSampleThrowsNotStrictlyPositiveException() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        distribution = new DiscreteDistribution<>(samples);
        assertThrows(NotStrictlyPositiveException.class, () -> distribution.sample(0));
    }

    @Test
    public void testSampleArray() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        distribution = new DiscreteDistribution<>(samples);
        int numSamples = 1000;
        Integer[] sampleArray = distribution.sample(numSamples);
        int[] sampleCounts = new int[3];
        for (int i = 0; i < sampleArray.length; i++) {
            switch (sampleArray[i]) {
                case 1:
                    sampleCounts[0]++;
                    break;
                case 2:
                    sampleCounts[1]++;
                    break;
                case 3:
                    sampleCounts[2]++;
                    break;
            }
        }
        double[] sampleProbabilities = new double[3];
        for (int i = 0; i < sampleProbabilities.length; i++) {
            sampleProbabilities[i] = (double) sampleCounts[i] / numSamples;
        }
        assertArrayEquals(samples.stream().mapToDouble(Pair::getValue).toArray(), sampleProbabilities, EPSILON);
    }
}