import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DiscreteDistributionTest {

    @Test
    public void testConstructorWithNullInput() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        try {
            new DiscreteDistribution<>(samples);
            fail("Expected NotNullException");
        } catch (Exception e) {
            assertEquals(NotNullException.class, e.getClass());
        }

        List<Pair<String, Double>> samplesString = new ArrayList<>();
        try {
            new DiscreteDistribution<>(samplesString);
            fail("Expected NotInstanceOfTypeException");
        } catch (Exception e) {
            assertEquals(InstanceOfException.class, e.getClass());
        }
    }

    @Test
    public void testConstructorWithEmptyList() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        try {
            new DiscreteDistribution<>(samples);
            fail("Expected EmptyException");
        } catch (Exception e) {
            assertEquals(EmptyException.class, e.getClass());
        }
    }

    @Test
    public void testConstructorWithInvalidProbabilities() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, -1.0), new Pair<>(2, 1.0));
        try {
            new DiscreteDistribution<>(samples);
            fail("Expected NotPositiveException or MathArithmeticException");
        } catch (Exception e) {
            if (e instanceof NotPositiveException || e instanceof MathArithmeticException) {
                return;
            }
            throw e;
        }
    }

    @Test
    public void testConstructorWithValidProbabilities() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.5),
                new Pair<>(4, 0.1)
        );
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        assertEquals(samples.size(), distribution.singletons.size());
        assertEquals(samples, distribution.getSamples());
        assertEquals(1.0, Precision.doubleEquals(distribution.probability(1), 0.1, 1e-6), 1e-6);
        assertEquals(1.0, Precision.doubleEquals(distribution.probability(3), 0.5, 1e-6), 1e-6);
        assertEquals(1.0, Precision.doubleEquals(distribution.probability(null), 0.0, 1e-6), 1e-6);
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.5),
                new Pair<>(4, 0.1)
        );
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        int sample1 = distribution.sample().intValue();
        int sample2 = distribution.sample().intValue();
        int sample3 = distribution.sample().intValue();

        assertEquals(1, sample1);
        assertEquals(2, sample2);
        assertEquals(3, sample3);
        assertEquals(1.0, Precision.doubleEquals(distribution.probability(sample1), 0.1, 1e-6), 1e-6);
    }

    @Test
    public void testSampleArray() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.5),
                new Pair<>(4, 0.1)
        );
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Integer[] sampleArray = distribution.sample(3);

        assertEquals(3, sampleArray.length);
        for (int i = 0; i < sampleArray.length; i++) {
            int sample = sampleArray[i].intValue();
            assertEquals(1, sampleArray.length);
            assertEquals(1, sample);
            assertEquals(1.0, Precision.doubleEquals(distribution.probability(sample), 0.1, 1e-6), 1e-6);
        }
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.1),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.5),
                new Pair<>(4, 0.1)
        );
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        long seed = System.nanoTime();
        distribution.reseedRandomGenerator(seed);

        int sample1 = distribution.sample().intValue();
        int sample2 = distribution.sample().intValue();

        int sampleArray[] = distribution.sample(3);

        // Repeat the test several times to make sure that the distribution is still the same
        for (int i = 0; i < 10; i++) {
            assertEquals(samples.size(), distribution.singletons.size());
            assertEquals(samples, distribution.getSamples());
            assertEquals(1.0, Precision.doubleEquals(distribution.probability(sample1), 0.1, 1e-6), 1e-6);
            assertEquals(1.0, Precision.doubleEquals(distribution.probability(sample2), 0.3, 1e-6), 1e-6);
            assertEquals(1.0, Precision.doubleEquals(distribution.probability(sampleArray[0]), 0.1, 1e-6), 1e-6);
            assertEquals(1.0, Precision.doubleEquals(distribution.probability(sampleArray[1]), 0.5, 1e-6), 1e-6);
            assertEquals(1.0, Precision.doubleEquals(distribution.probability(sampleArray[2]), 0.1, 1e-6), 1e-6);
            sample1 = distribution.sample().intValue();
            sample2 = distribution.sample().intValue();
            sampleArray = distribution.sample(3);
        }
    }

}