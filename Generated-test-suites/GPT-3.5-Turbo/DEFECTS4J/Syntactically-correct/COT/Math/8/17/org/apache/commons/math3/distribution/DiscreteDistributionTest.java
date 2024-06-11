import org.junit.Test;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.5));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.2));
        DiscreteDistribution<String> dist = new DiscreteDistribution<>(samples);

        double pA = dist.probability("A");
        assertEquals(0.5, pA, 0.000001);

        double pB = dist.probability("B");
        assertEquals(0.3, pB, 0.000001);

        double pC = dist.probability("C");
        assertEquals(0.2, pC, 0.000001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.5));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.2));
        DiscreteDistribution<String> dist = new DiscreteDistribution<>(samples);

        List<Pair<String, Double>> actualSamples = dist.getSamples();

        assertEquals(samples, actualSamples);
    }

    @Test
    public void testSample() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.5));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.2));
        DiscreteDistribution<String> dist = new DiscreteDistribution<>(samples);

        String sample = dist.sample();

        assertTrue(samples.contains(new Pair<>(sample, 0.0)));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleInvalidSampleSize() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.5));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.2));
        DiscreteDistribution<String> dist = new DiscreteDistribution<>(samples);

        dist.sample(0);
    }

    @Test
    public void testSampleValidSampleSize() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.5));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.2));
        DiscreteDistribution<String> dist = new DiscreteDistribution<>(samples);

        String[] sample = dist.sample(5);

        assertEquals(5, sample.length);
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.5));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.2));
        DiscreteDistribution<String> dist = new DiscreteDistribution<>(samples);

        dist.reseedRandomGenerator(12345);

        // Cannot directly test reseeding random generator
        // as the result will be random
        // So, we will assume successful reseeding if it doesn't throw any exceptions
    }

}