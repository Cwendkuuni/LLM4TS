import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DiscreteDistributionTest {

    @Test
    public void testConstructorWithValidInput() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        assertEquals(3, distribution.singletons.size());
        assertEquals(0.2, distribution.probabilities[0], 0.01);
        assertEquals(0.3, distribution.probabilities[1], 0.01);
        assertEquals(0.5, distribution.probabilities[2], 0.01);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithProbabilitiesSumToZero() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, -0.1));

        new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithInfiniteProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException e) {
            fail("Expected MathIllegalArgumentException");
        }
    }

    @Test
    public void testProbabilityMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        assertEquals(0.2, distribution.probability((Integer) 1), 0.01);
        assertEquals(0.3, distribution.probability((Integer) 2), 0.01);
        assertEquals(0.5, distribution.probability((Integer) 3), 0.01);
        assertEquals(0.0, distribution.probability((Integer) 4), 0.01);
    }

    @Test
    public void testSampleMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            int sample = distribution.sample();
            assertTrue(Arrays.binarySearch(samples.stream().mapToInt(p -> p.getKey().intValue()).toArray(), sample) >= 0);
        }
    }

    @Test
    public void testSampleMethodWithLargeSampleSize() throws NotStrictlyPositiveException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        T[] sample = distribution.sample(1000000);

        // Due to floating point precision issues, we can't directly check if all samples are valid. Instead, we'll check if the number of invalid samples is below a certain threshold.
        int invalidSamples = 0;
        for (int i = 0; i < sample.length; i++) {
            if (Arrays.binarySearch(samples.stream().mapToInt(p -> p.getKey().intValue()).toArray(), sample[i].intValue()) < 0) {
                invalidSamples++;
            }
        }
        assertEquals(0, invalidSamples);
    }
}