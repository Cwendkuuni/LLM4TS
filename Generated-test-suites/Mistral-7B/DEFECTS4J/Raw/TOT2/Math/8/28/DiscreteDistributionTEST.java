import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testConstructorNullInput() {
        try {
            new DiscreteDistribution(null);
            fail("Expected NotNullException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            new DiscreteDistribution(null, new ArrayList<>());
            fail("Expected NotNullException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorEmptyInput() {
        try {
            new DiscreteDistribution(new ArrayList<>());
            fail("Expected NotPositiveException or MathArithmeticException");
        } catch (NotPositiveException | MathArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorInvalidProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.0));
        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        samples.set(0, new Pair<>(Integer.valueOf(1), Double.POSITIVE_INFINITY));
        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorValidInput() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.1));
        samples.add(new Pair<>(Integer.valueOf(2), 0.2));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        assertEquals(3, distribution.singletons.size());
        assertEquals(0.1, distribution.probabilities[0], 0.01);
        assertEquals(0.2, distribution.probabilities[1], 0.01);
        assertEquals(0.3, distribution.probabilities[2], 0.01);
    }

    @Test
    public void testProbabilityValidInput() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.1));
        samples.add(new Pair<>(Integer.valueOf(2), 0.2));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        assertEquals(0.1, distribution.probability(Integer.valueOf(1)), 0.01);
        assertEquals(0.2, distribution.probability(Integer.valueOf(2)), 0.01);
        assertEquals(0.3, distribution.probability(Integer.valueOf(3)), 0.01);

        assertEquals(0.0, distribution.probability(Integer.valueOf(4)), 0.01);
    }

    @Test
    public void testProbabilityNullInput() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(Pair.of(Integer.valueOf(1), 0.1), Pair.of(Integer.valueOf(2), 0.2)));

        try {
            distribution.probability(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSampleValidInput() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.1));
        samples.add(new Pair<>(Integer.valueOf(2), 0.2));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        T sample = distribution.sample();
        Integer expected = Integer.valueOf(1);
        assertEquals(expected.getClass(), sample.getClass());
        assertEquals(expected, sample);

        double cumulativeProbability = 0.0;
        for (int i = 0; i < distribution.probabilities.length; i++) {
            cumulativeProbability += distribution.probabilities[i];
            if (cumulativeProbability >= 0.5) {
                expected = distribution.singletons.get(i);
                break;
            }
        }
        sample = distribution.sample();
        assertEquals(expected, sample);
    }

    @Test
    public void testSampleNullInput() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(Pair.of(Integer.valueOf(1), 0.1), Pair.of(Integer.valueOf(2), 0.2)));

        try {
            distribution.sample(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSampleNegativeSize() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(Pair.of(Integer.valueOf(1), 0.1), Pair.of(Integer.valueOf(2), 0.2)));

        try {
            distribution.sample(-1);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testSampleZeroSize() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(Pair.of(Integer.valueOf(1), 0.1), Pair.of(Integer.valueOf(2), 0.2)));

        T[] samples = distribution.sample(0);
        assertEquals(0, samples.length);
    }

    @Test
    public void testReseedRandomGenerator() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), Arrays.asList(Pair.of(Integer.valueOf(1), 0.1), Pair.of(Integer.valueOf(2), 0.2)));

        long seed = 42;
        distribution.reseedRandomGenerator(seed);

        Integer sample = distribution.sample().getValue();
        assertEquals(seed, distribution.random.getNextLong());
    }
}