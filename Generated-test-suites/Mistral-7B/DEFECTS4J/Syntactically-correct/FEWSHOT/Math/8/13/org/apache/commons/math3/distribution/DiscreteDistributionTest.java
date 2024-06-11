package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.random.Well19937c;

public class DiscreteDistributionTest {

    @Test
    public void testProbabilityNullSingleton() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(null, 1.0));

        try {
            new DiscreteDistribution<>(samples);
            fail();
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testProbabilityNegativeSingleton() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -1.0));

        try {
            new DiscreteDistribution<>(samples);
            fail();
        } catch (NotPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testProbabilityZeroSum() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 1.0));
        samples.add(new Pair<>(2, -1.0));

        try {
            new DiscreteDistribution<>(samples);
            fail();
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testProbabilityInfiniteSingleton() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));

        try {
            new DiscreteDistribution<>(samples);
            fail();
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSampleNullRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.5));

        try {
            new DiscreteDistribution<>(samples, null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSampleEmptyList() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();

        try {
            new DiscreteDistribution<>(samples);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        assertEquals(0.3, distribution.probability((Integer) 1));
        assertEquals(0.4, distribution.probability((Integer) 2));
        assertEquals(0.3, distribution.probability((Integer) 3));
    }

    @Test
    public void testProbabilityWrongValue() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        assertNull(distribution.probability((Integer) 4));
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        List<Pair<Integer, Double>> result = distribution.getSamples();

        assertArrayEquals(samples.toArray(), result.toArray());
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Integer result = distribution.sample();

        assertEquals(1, result.intValue());
    }

    @Test
    public void testSampleSameValueMultipleTimes() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(1, 0.4));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Integer result1 = distribution.sample();
        Integer result2 = distribution.sample();

        assertEquals(1, result1.intValue());
        assertEquals(1, result2.intValue());
    }

    @Test
    public void testSampleDifferentValues() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Integer result1 = distribution.sample();
        Integer result2 = distribution.sample();

        assertTrue(result1.intValue() != result2.intValue());
    }

    @Test
    public void testSampleSameValuesMultipleTimesWithLargeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.95));
        samples.add(new Pair<>(2, 0.05));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Integer result1 = distribution.sample();
        Integer result2 = distribution.sample();
        Integer result3 = distribution.sample();

        assertEquals(1, result1.intValue());
        assertEquals(1, result2.intValue());
        assertEquals(1, result3.intValue());
    }

    @Test
    public void testSampleWithCustomRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Integer result = distribution.sample();

        assertEquals(1, result.intValue());
    }

    @Test
    public void testSampleSizeZero() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();

        try {
            new DiscreteDistribution<>(samples).sample(0);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testSampleSizeOne() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));

        T[] result = new DiscreteDistribution<>(samples).sample(1);

        assertEquals(1, result.length);
        assertEquals(1, result[0].intValue());
    }

    @Test
    public void testSampleSizeMultiple() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        T[] result = new DiscreteDistribution<>(samples).sample(3);

        assertEquals(3, result.length);

        for (int i = 0; i < result.length; i++) {
            assertTrue(singletonList(result[i]).containsAll(singletonList(singletons.get(i))));
        }
    }

    @Test
    public void testSampleSizeMultipleWithSameValue() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(1, 0.5));

        T[] result = new DiscreteDistribution<>(samples).sample(3);

        assertEquals(3, result.length);

        List<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(1);
        expected.add(1);

        assertArrayEquals(expected.toArray(new Integer[expected.size()]), asIntArray(result));
    }

    @Test
    public void testReseedRandomGenerator() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        long seed = 1234567890L;
        distribution.reseedRandomGenerator(seed);

        Integer result1 = distribution.sample();
        Integer result2 = distribution.sample();

        Integer result1Prime = distribution.sample();
        Integer result2Prime = distribution.sample();

        assertNotSame(result1, result1Prime);
        assertNotSame(result2, result2Prime);
    }

    private List<Integer> singletonList(Object obj) {
        List<Integer> list = new ArrayList<>();
        list.add(obj instanceof Integer ? ((Integer) obj).intValue() : obj.hashCode());
        return list;
    }

    private int[] asIntArray(Object[] array) {
        int[] intArray = new int[array.length];

        for (int i = 0; i < array.length; i++) {
            intArray[i] = ((Integer) array[i]).intValue();
        }

        return intArray;
    }

    private static List<Integer> singletons = new ArrayList<>();
    static {
        singletons.add(1);
        singletons.add(2);
        singletons.add(3);
    }
}