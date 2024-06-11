import org.junit.Test;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<String, Double>("A", 0.2));
        samples.add(new Pair<String, Double>("B", 0.3));
        samples.add(new Pair<String, Double>("C", 0.1));
        samples.add(new Pair<String, Double>("D", 0.4));

        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        assertEquals(0.2, distribution.probability("A"), 0.0001);
        assertEquals(0.3, distribution.probability("B"), 0.0001);
        assertEquals(0.1, distribution.probability("C"), 0.0001);
        assertEquals(0.4, distribution.probability("D"), 0.0001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<String, Double>("A", 0.2));
        samples.add(new Pair<String, Double>("B", 0.3));
        samples.add(new Pair<String, Double>("C", 0.1));

        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        List<Pair<String, Double>> result = distribution.getSamples();

        assertEquals(samples, result);
    }

    @Test
    public void testSample() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<String, Double>("A", 0.2));
        samples.add(new Pair<String, Double>("B", 0.3));
        samples.add(new Pair<String, Double>("C", 0.1));

        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        String result = distribution.sample();

        assertTrue(samples.stream().anyMatch(pair -> pair.getKey().equals(result)));
    }

    @Test
    public void testSampleSize() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<String, Double>("A", 0.2));
        samples.add(new Pair<String, Double>("B", 0.3));
        samples.add(new Pair<String, Double>("C", 0.1));

        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        int sampleSize = 5;
        String[] result = distribution.sample(sampleSize);

        assertEquals(sampleSize, result.length);

        for (String value : result) {
            assertTrue(samples.stream().anyMatch(pair -> pair.getKey().equals(value)));
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleSizeNegative() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<String, Double>("A", 0.2));
        samples.add(new Pair<String, Double>("B", 0.3));
        samples.add(new Pair<String, Double>("C", 0.1));

        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        int sampleSize = -5;
        distribution.sample(sampleSize);
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<String, Double>("A", 0.2));
        samples.add(new Pair<String, Double>("B", 0.3));
        samples.add(new Pair<String, Double>("C", 0.1));

        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        long seed = 123456789;
        distribution.reseedRandomGenerator(seed);
    }

    @Test
    public void testConstructor() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<String, Double>("A", 0.2));
        samples.add(new Pair<String, Double>("B", 0.3));
        samples.add(new Pair<String, Double>("C", 0.1));

        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        assertEquals(samples, distribution.getSamples());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeProbability() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<String, Double>("A", 0.2));
        samples.add(new Pair<String, Double>("B", -0.3));
        samples.add(new Pair<String, Double>("C", 0.1));

        new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorZeroSumProbability() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<String, Double>("A", 0.2));
        samples.add(new Pair<String, Double>("B", 0.7));
        samples.add(new Pair<String, Double>("C", -0.1));

        new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorInfiniteProbability() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<String, Double>("A", 0.2));
        samples.add(new Pair<String, Double>("B", Double.POSITIVE_INFINITY));
        samples.add(new Pair<String, Double>("C", 0.1));

        new DiscreteDistribution<>(samples);
    }

}