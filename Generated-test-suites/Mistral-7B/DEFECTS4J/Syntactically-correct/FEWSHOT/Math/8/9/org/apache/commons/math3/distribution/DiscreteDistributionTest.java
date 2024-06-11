package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.DiscreteUniformDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;

public class DiscreteDistributionTest {

    /**
     * Test constructor with a list of samples.
     */
    @Test
    public void testConstructorWithListOfSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);

            // Test getSamples method
            List<Pair<Integer, Double>> expectedSamples = Arrays.asList(
                    new Pair<>(Integer.valueOf(1), 0.3),
                    new Pair<>(Integer.valueOf(2), 0.4),
                    new Pair<>(Integer.valueOf(3), 0.3)
            );
            assertEquals(expectedSamples, distribution.getSamples());

            // Test probability method
            assertEquals(0.3, distribution.probability(Integer.valueOf(1)), 0.01);
            assertEquals(0.4, distribution.probability(Integer.valueOf(2)), 0.01);
            assertEquals(0.3, distribution.probability(Integer.valueOf(3)), 0.01);
            assertNull(distribution.probability(null));
            assertNull(distribution.probability(Integer.valueOf(4)));

            // Test sample method
            Integer sampled = distribution.sample();
            List<Integer> sampleList = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                sampleList.add(distribution.sample());
            }
            List<Integer> expectedSampleList = new ArrayList<>(samples.size());
            for (Pair<Integer, Double> sample : samples) {
                expectedSampleList.add(sample.getKey());
            }
            assertEquals(expectedSampleList, sampleList);

            // Test sample method with specified seed
            RandomGenerator rng = new Well19937c(1234567890L);
            DiscreteDistribution<Integer> distributionWithSeed = new DiscreteDistribution<Integer>(rng, samples);
            List<Integer> seededSampleList = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                seededSampleList.add(distributionWithSeed.sample());
            }
            for (int i = 0; i < samples.size(); i++) {
                assertTrue(seededSampleList.contains(samples.get(i).getKey()));
            }

            // Test sample method with negative size
            try {
                distribution.sample(-1);
                fail();
            } catch (NotStrictlyPositiveException e) {
                // Expected
            }

            // Test sample method with zero size
            try {
                distribution.sample(0);
                fail();
            } catch (NotStrictlyPositiveException e) {
                // Expected
            }

            // Test sample method array
            Integer[] samplesArray = new Integer[samples.size()];
            for (int i = 0; i < samples.size(); i++) {
                samplesArray[i] = samples.get(i).getKey();
            }
            Integer[] sampledArray = distribution.sample(samples.size());
            assertArrayEquals(samplesArray, sampledArray);

            // Test reseedRandomGenerator method
            distribution.reseedRandomGenerator(1234567890L);
            List<Integer> sampleListAfterSeed = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                sampleListAfterSeed.add(distribution.sample());
            }
            assertNotEquals(sampleList, sampleListAfterSeed);

        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test constructor with a random generator and a list of samples.
     */
    @Test
    public void testConstructorWithRandomGeneratorAndListOfSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        RandomGenerator rng = new Well19937c();

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(rng, samples);

            // Test getSamples method
            List<Pair<Integer, Double>> expectedSamples = Arrays.asList(
                    new Pair<>(Integer.valueOf(1), 0.3),
                    new Pair<>(Integer.valueOf(2), 0.4),
                    new Pair<>(Integer.valueOf(3), 0.3)
            );
            assertEquals(expectedSamples, distribution.getSamples());

            // Test probability method
            assertEquals(0.3, distribution.probability(Integer.valueOf(1)), 0.01);
            assertEquals(0.4, distribution.probability(Integer.valueOf(2)), 0.01);
            assertEquals(0.3, distribution.probability(Integer.valueOf(3)), 0.01);
            assertNull(distribution.probability(null));
            assertNull(distribution.probability(Integer.valueOf(4)));

            // Test sample method
            Integer sampled = distribution.sample();
            List<Integer> sampleList = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                sampleList.add(distribution.sample());
            }
            List<Integer> expectedSampleList = new ArrayList<>(samples.size());
            for (Pair<Integer, Double> sample : samples) {
                expectedSampleList.add(sample.getKey());
            }
            assertEquals(expectedSampleList, sampleList);

            // Test sample method with negative size
            try {
                distribution.sample(-1);
                fail();
            } catch (NotStrictlyPositiveException e) {
                // Expected
            }

            // Test sample method with zero size
            try {
                distribution.sample(0);
                fail();
            } catch (NotStrictlyPositiveException e) {
                // Expected
            }

            // Test sample method array
            Integer[] samplesArray = new Integer[samples.size()];
            for (int i = 0; i < samples.size(); i++) {
                samplesArray[i] = samples.get(i).getKey();
            }
            Integer[] sampledArray = distribution.sample(samples.size());
            assertArrayEquals(samplesArray, sampledArray);

            // Test reseedRandomGenerator method
            distribution.reseedRandomGenerator(1234567890L);
            List<Integer> sampleListAfterSeed = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                sampleListAfterSeed.add(distribution.sample());
            }
            assertNotEquals(sampleList, sampleListAfterSeed);

        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test constructor with an empty list of samples.
     */
    @Test
    public void testConstructorWithEmptyListOfSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
            assertNull(distribution);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test constructor with a list of samples having infinite probabilities.
     */
    @Test
    public void testConstructorWithListOfSamplesHavingInfiniteProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), Double.POSITIVE_INFINITY));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
            fail();
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test constructor with a list of samples having zero probabilities.
     */
    @Test
    public void testConstructorWithListOfSamplesHavingZeroProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.0));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
            fail();
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    /**
     * Test constructor with a list of samples having negative probabilities.
     */
    @Test
    public void testConstructorWithListOfSamplesHavingNegativeProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), -0.1));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
            fail();
        } catch (NotPositiveException e) {
            // Expected
        }
    }

    /**
     * Test constructor with a list of samples having probabilities summing to zero.
     */
    @Test
    public void testConstructorWithListOfSamplesHavingProbabilitiesSummingToZero() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), -0.1));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
            fail();
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    /**
     * Test constructor with a list of samples having duplicate values.
     */
    @Test
    public void testConstructorWithListOfSamplesHavingDuplicateValues() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(1), 0.4));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test constructor with a list of samples having null values.
     */
    @Test
    public void testConstructorWithListOfSamplesHavingNullValues() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(null, 0.3));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test constructor with a list of samples having null probabilities.
     */
    @Test
    public void testConstructorWithListOfSamplesHavingNullProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), null));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test DiscreteUniformDistribution constructor.
     */
    @Test
    public void testDiscreteUniformDistributionConstructor() {
        RandomGenerator rng = new Well19937c();
        DiscreteUniformDistribution<Integer> distribution = new DiscreteUniformDistribution<Integer>(rng, 1, 10);

        // Test getSamples method
        List<Pair<Integer, Double>> expectedSamples = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            double probability = 1.0 / 10.0;
            expectedSamples.add(new Pair<>(Integer.valueOf(i), probability));
        }
        assertEquals(expectedSamples, distribution.getSamples());

        // Test probability method
        for (int i = 1; i <= 10; i++) {
            assertEquals(1.0 / 10.0, distribution.probability(Integer.valueOf(i)), 0.01);
        }
        assertNull(distribution.probability(null));
        assertNull(distribution.probability(Integer.valueOf(0)));
        assertNull(distribution.probability(Integer.valueOf(11)));

        // Test sample method
        List<Integer> sampleList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            sampleList.add(distribution.sample());
        }
        List<Integer> expectedSampleList = new ArrayList<>(expectedSamples.size());
        for (Pair<Integer, Double> sample : expectedSamples) {
            expectedSampleList.add(sample.getKey());
        }
        assertEquals(expectedSampleList, sampleList);

        // Test sample method with negative size
        try {
            distribution.sample(-1);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        // Test sample method with zero size
        try {
            distribution.sample(0);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        // Test sample method array
        Integer[] samplesArray = new Integer[expectedSamples.size()];
        for (int i = 0; i < expectedSamples.size(); i++) {
            samplesArray[i] = expectedSamples.get(i).getKey();
        }
        Integer[] sampledArray = distribution.sample(expectedSamples.size());
        assertArrayEquals(samplesArray, sampledArray);

        // Test reseedRandomGenerator method
        distribution.reseedRandomGenerator(1234567890L);
        List<Integer> sampleListAfterSeed = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            sampleListAfterSeed.add(distribution.sample());
        }
        assertNotEquals(sampleList, sampleListAfterSeed);
    }
}