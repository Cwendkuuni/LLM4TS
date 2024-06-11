import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiscreteDistributionTest {

    @Test
    public void testConstructorWithValidSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        assertEquals(3, distribution.singletons.size());
        assertEquals(0.3, distribution.probability(1), 0.01);
        assertEquals(0.4, distribution.probability(2), 0.01);
        assertEquals(0.3, distribution.probability(3), 0.01);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.5));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithSumZeroProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.2));
        samples.add(new Pair<>(3, -0.4));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithInfiniteProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testProbability() {
        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), sampleData());

        assertEquals(0.3, distribution.probability(1), 0.01);
        assertEquals(0.4, distribution.probability(2), 0.01);
        assertEquals(0.3, distribution.probability(3), 0.01);
    }

    @Test
    public void testSample() {
        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), sampleData());

        assertEquals(1, distribution.sample().intValue());
        assertEquals(2, distribution.sample().intValue());
        assertEquals(3, distribution.sample().intValue());
    }

    @Test
    public void testSampleArray() {
        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), sampleData());

        int[] expectedSample = {1, 2, 3, 1, 3, 3, 2, 1, 1, 3};
        int[] sample = distribution.sample(11);

        assertArrayEquals(expectedSample, sample);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNegativeSize() {
        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), sampleData());

        try {
            distribution.sample(-1);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testGetSamples() {
        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), sampleData());

        List<Pair<Integer, Double>> samples = distribution.getSamples();

        assertEquals(3, samples.size());
        assertEquals(0.3, samples.get(0).getValue(), 0.01);
        assertEquals(0.4, samples.get(1).getValue(), 0.01);
        assertEquals(0.3, samples.get(2).getValue(), 0.01);
    }

    private List<Pair<Integer, Double>> sampleData() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));
        return samples;
    }
}