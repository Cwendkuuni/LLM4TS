package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.DiscreteDistribution.Pair;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Pair;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistribution() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            DiscreteDistribution distribution = new DiscreteDistribution<>(samples);
            assertArrayEquals(new Integer[]{1, 2, 3}, distribution.singletons.toArray(new Integer[0]));
            assertArrayEquals(new double[]{0.3, 0.4, 0.3}, distribution.probabilities, 0.001);
            assertEquals(0.3, distribution.probability((Integer) 1), 0.001);
            assertEquals(0.4, distribution.probability((Integer) 2), 0.001);
            assertEquals(0.3, distribution.probability((Integer) 3), 0.001);
            assertEquals(Arrays.asList(new Pair<>(1, 0.3), new Pair<>(2, 0.4), new Pair<>(3, 0.3)),
                    distribution.getSamples());

            RandomGenerator rng = new Well19937c();
            distribution = new DiscreteDistribution<>(rng, samples);
            assertArrayEquals(new Integer[]{1, 2, 3}, distribution.singletons.toArray(new Integer[0]));
            assertArrayEquals(new double[]{0.3, 0.4, 0.3}, distribution.probabilities, 0.001);
            assertEquals(0.3, distribution.probability((Integer) 1), 0.001);
            assertEquals(0.4, distribution.probability((Integer) 2), 0.001);
            assertEquals(0.3, distribution.probability((Integer) 3), 0.001);
            assertEquals(Arrays.asList(new Pair<>(1, 0.3), new Pair<>(2, 0.4), new Pair<>(3, 0.3)),
                    distribution.getSamples());
        } catch (MathIllegalArgumentException | MathArithmeticException | NotPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.3), new Pair<>(2, 0.4),
                new Pair<>(3, 0.3));
        DiscreteDistribution distribution = new DiscreteDistribution<>(samples);
        long seed = System.currentTimeMillis();

        distribution.reseedRandomGenerator(seed);

        RandomGenerator rng = distribution.random;
        rng.setSeed(seed);

        Integer actualSample = distribution.sample();
        Integer expectedSample = rng.nextInt(3);
        assertEquals(expectedSample, actualSample);

        /* Test that reseeding with the same seed yields the same samples */
        distribution.reseedRandomGenerator(seed);
        actualSample = distribution.sample();
        assertEquals(expectedSample, actualSample);
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.3), new Pair<>(2, 0.4),
                new Pair<>(3, 0.3));
        DiscreteDistribution distribution = new DiscreteDistribution<>(samples);

        List<Integer> samplesList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            samplesList.add(distribution.sample());
        }

        int[] expectedSamples = new int[1000];
        for (int i = 0; i < 1000; i++) {
            expectedSamples[i] = (int) (Math.random() * 3);
        }

        assertArrayEquals(expectedSamples, samplesList.stream().mapToInt(i -> i).toArray());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNegativeSampleSize() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.3), new Pair<>(2, 0.4),
                new Pair<>(3, 0.3));
        DiscreteDistribution distribution = new DiscreteDistribution<>(samples);

        distribution.sample(-1);
    }
}