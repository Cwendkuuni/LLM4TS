import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

public class DiscreteDistributionTest {
    @Test
    public void testConstructor1WithValidSamples() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        assertNotNull(distribution);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor1WithNegativeProbabilitySamples() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, -0.5));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructor1WithInfiniteProbabilitySamples() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
    }

    @Test
    public void testConstructor2WithValidSamples() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.1));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.6));

        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        assertNotNull(distribution);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor2WithNegativeProbabilitySamples() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.1));
        samples.add(new Pair<>("B", -0.3));
        samples.add(new Pair<>("C", 0.6));

        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructor2WithInfiniteProbabilitySamples() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", Double.NEGATIVE_INFINITY));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.6));

        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test
    public void testReseedRandomGenerator() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        distribution.reseedRandomGenerator(12345L);

        assertNotNull(distribution);
    }

    @Test
    public void testProbabilityWithExistingValue() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        double probability = distribution.probability(2);

        assertTrue(probability >= 0);
    }

    @Test
    public void testProbabilityWithNonExistingValue() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        double probability = distribution.probability(4);

        assertTrue(probability == 0);
    }

    @Test
    public void testGetSamples() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        List<Pair<Integer, Double>> result = distribution.getSamples();

        assertEquals(samples, result);
    }

    @Test
    public void testSample() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Integer result = distribution.sample();

        assertTrue(result >= 1 && result <= 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithNonPositiveSampleSize() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Integer[] result = distribution.sample(0);
    }

    @Test
    public void testSampleWithValidSampleSize() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Integer[] result = distribution.sample(5);

        assertEquals(5, result.length);
    }
}