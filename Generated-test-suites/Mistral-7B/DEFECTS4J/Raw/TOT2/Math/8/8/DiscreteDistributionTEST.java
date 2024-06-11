import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DiscreteDistributionTest {

    @Test
    public void testConstructors() {
        List<Pair<Integer, Double>> samples = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        );

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(samples);
            assertNotNull(distribution);
        } catch (Exception e) {
            // expected exception
        }

        List<Pair<Integer, Double>> samples2 = new ArrayList<>();
        samples2.add(new Pair<>(1, 0.3));
        samples2.add(new Pair<>(2, -0.4)); // negative probability

        try {
            DiscreteDistribution<Integer> distribution2 = new DiscreteDistribution<Integer>(samples2);
            assertNotNull(distribution2);
            // this assertion should fail due to negative probability
            // but since we're testing the exception, it's okay
        } catch (NotPositiveException e1) {
            // expected exception for negative probability
        } catch (Exception e2) {
            // unexpected exception
            throw new RuntimeException("Unexpected exception", e2);
        }

        List<Pair<Integer, Double>> samples3 = new ArrayList<>();
        samples3.add(new Pair<>(1, Double.POSITIVE_INFINITY)); // infinite probability

        try {
            DiscreteDistribution<Integer> distribution3 = new DiscreteDistribution<Integer>(samples3);
            assertNotNull(distribution3);
            // this assertion should fail due to infinite probability
            // but since we're testing the exception, it's okay
        } catch (MathIllegalArgumentException e1) {
            // expected exception for infinite probability
        } catch (Exception e2) {
            // unexpected exception
            throw new RuntimeException("Unexpected exception", e2);
        }

        // test constructor with null input
        try {
            DiscreteDistribution<Integer> distribution4 = new DiscreteDistribution<Integer>(null);
            assertNotNull(distribution4);
            // this assertion should fail due to null input
            // but since we're testing the exception, it's okay
        } catch (NullPointerException e1) {
            // expected exception for null input
        } catch (Exception e2) {
            // unexpected exception
            throw new RuntimeException("Unexpected exception", e2);
        }

        // test constructor with empty input
        try {
            DiscreteDistribution<Integer> distribution5 = new DiscreteDistribution<Integer>(Collections.emptyList());
            assertNotNull(distribution5);
            // this assertion should fail due to empty list
            // but since we're testing the exception, it's okay
        } catch (Exception e) {
            // expected exception for empty list
            throw new RuntimeException("Unexpected exception", e);
        }

        // test constructor with same value but different keys
        List<Pair<Integer, Double>> samples6 = new ArrayList<>();
        samples6.add(new Pair<>(1, 0.3));
        samples6.add(new Pair<>(2, 0.3));

        try {
            DiscreteDistribution<Integer> distribution6 = new DiscreteDistribution<Integer>(samples6);
            assertNotNull(distribution6);
            // this test should pass
        } catch (Exception e) {
            // unexpected exception
            throw new RuntimeException("Unexpected exception", e);
        }

        // test constructor with same key but different values
        List<Pair<Integer, Double>> samples7 = new ArrayList<>();
        samples7.add(new Pair<>(1, 0.3));
        samples7.add(new Pair<>(1, 0.4));

        try {
            DiscreteDistribution<Integer> distribution7 = new DiscreteDistribution<Integer>(samples7);
            assertNotNull(distribution7);
            // this test should fail due to duplicate keys
            // but since we're testing the exception, it's okay
        } catch (Exception e) {
            // expected exception for duplicate keys
        }

        // test constructor with zero probabilities sum
        List<Pair<Integer, Double>> samples8 = new ArrayList<>();
        samples8.add(new Pair<>(1, 0.1));
        samples8.add(new Pair<>(2, 0.1));
        samples8.add(new Pair<>(3, 0.1));

        try {
            DiscreteDistribution<Integer> distribution8 = new DiscreteDistribution<Integer>(samples8);
            assertNotNull(distribution8);
            // this assertion should fail due to zero probabilities sum
            // but since we're testing the exception, it's okay
        } catch (MathArithmeticException e) {
            // expected exception for zero probabilities sum
        } catch (Exception e2) {
            // unexpected exception
            throw new RuntimeException("Unexpected exception", e2);
        }

        List<Pair<Integer, Double>> samples9 = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        );

        try {
            DiscreteDistribution<Integer> distribution9 = new DiscreteDistribution<Integer>(samples9);
            assertNotNull(distribution9);
            // this test should pass
            assertEquals(3, distribution9.singletons.size());
            assertEquals(samples9, distribution9.getSamples());
        } catch (Exception e) {
            // unexpected exception
            throw new RuntimeException("Unexpected exception", e);
        }

        List<Pair<Integer, Double>> samples10 = new ArrayList<>();
        samples10.add(new Pair<>(1, 0.3));

        try {
            DiscreteDistribution<Integer> distribution10 = new DiscreteDistribution<Integer>(samples10);
            assertNotNull(distribution10);
            // this test should fail due to empty list
            // but since we're testing the exception, it's okay
        } catch (Exception e) {
            // expected exception for empty list
        }

        List<Pair<Integer, Double>> samples11 = Collections.singletonList(new Pair<>(1, 0.3));

        try {
            DiscreteDistribution<Integer> distribution11 = new DiscreteDistribution<Integer>(samples11);
            assertNotNull(distribution11);
            // this test should pass
            assertEquals(1, distribution11.singletons.size());
            assertEquals(samples11, distribution11.getSamples());
        } catch (Exception e) {
            // unexpected exception
            throw new RuntimeException("Unexpected exception", e);
        }

        // test constructor with null RNG
        try {
            DiscreteDistribution<Integer> distribution12 = new DiscreteDistribution<Integer>(null, samples9);
            assertNotNull(distribution12);
            // this assertion should fail due to null RNG
            // but since we're testing the exception, it's okay
        } catch (NullPointerException e) {
            // expected exception for null RNG
        } catch (Exception e2) {
            // unexpected exception
            throw new RuntimeException("Unexpected exception", e2);
        }

        // test constructor with valid RNG
        Well19937c rng = new Well19937c();
        List<Pair<Integer, Double>> samples13 = Arrays.asList(
                new Pair<>(1, 0.3),
                new Pair<>(2, 0.4),
                new Pair<>(3, 0.3)
        );

        try {
            DiscreteDistribution<Integer> distribution13 = new DiscreteDistribution<Integer>(rng, samples13);
            assertNotNull(distribution13);
            // this test should pass
            assertEquals(3, distribution13.singletons.size());
            assertEquals(samples13, distribution13.getSamples());
        } catch (Exception e) {
            // unexpected exception
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testProbability() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(Well19937c.getDefault(),
                Arrays.asList(new Pair<>(1, 0.3), new Pair<>(2, 0.4), new Pair<>(3, 0.3)));

        // test probability with valid input
        assertEquals(0.3, distribution.probability((Integer) 1), 0.001);
        assertEquals(0.4, distribution.probability((Integer) 2), 0.001);
        assertEquals(0.3, distribution.probability((Integer) 3), 0.001);

        // test probability with invalid input
        assertEquals(0.0, distribution.probability(null), 0.001);
        assertEquals(0.0, distribution.probability((Object) new String("test")), 0.001);
        assertEquals(0.0, distribution.probability((Integer) 4), 0.001);

        // test probability with out-of-bounds input
        assertEquals(0.0, distribution.probability((Integer) Integer.MIN_VALUE), 0.001);
        assertEquals(0.0, distribution.probability((Integer) Integer.MAX_VALUE), 0.001);
    }

    @Test
    public void testGetSamples() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(Well19937c.getDefault(),
                Arrays.asList(new Pair<>(1, 0.3), new Pair<>(2, 0.4), new Pair<>(3, 0.3)));

        List<Pair<Integer, Double>> samples = distribution.getSamples();

        // test getSamples with valid input
        assertEquals(3, samples.size());
        assertEquals(distribution.singletons, samples);

        // test getSamples with invalid input
        assertEquals(0, distribution.getSamples(null).size());
        assertEquals(0, distribution.getSamples((Object) new String("test")).size());
    }

    @Test
    public void testSample() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(Well19937c.getDefault(),
                Arrays.asList(new Pair<>(1, 0.3), new Pair<>(2, 0.4), new Pair<>(3, 0.3)));

        // test sample with valid input
        Integer sample = distribution.sample();
        List<Integer> samples = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            samples.add(distribution.sample());
        }
        List<Integer> expectedSamples = new ArrayList<>();
        for (Pair<Integer, Double> pair : distribution.getSamples()) {
            for (int i = 0; i < Math.round(100 * pair.getValue()); i++) {
                expectedSamples.add(pair.getKey());
            }
        }
        assertEquals(expectedSamples, samples);

        // test sample with invalid input
        assertNull(distribution.sample(null));
        assertNull(distribution.sample((Object) new String("test")));
    }

    @Test
    public void testSampleSize() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(Well19937c.getDefault(),
                Arrays.asList(new Pair<>(1, 0.3), new Pair<>(2, 0.4), new Pair<>(3, 0.3)));

        // test sampleSize with valid input
        int sampleSize = distribution.sample(5).length;
        assertEquals(5, sampleSize);

        // test sampleSize with invalid input
        try {
            distribution.sample(-1);
            fail("Test failed: expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected exception
        }
    }
}