package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.math.MathArrays;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistribution() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.1));
        samples.add(new Pair<>(Integer.valueOf(2), 0.2));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));
        samples.add(new Pair<>(Integer.valueOf(4), 0.4));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            assertEquals(4, distribution.singletons.size());
            assertEquals(3, distribution.probabilities.length);
            assertArrayEquals(new Double[]{0.1, 0.2, 0.3, 0.4}, distribution.probabilities, 0.001);
        } catch (MathIllegalArgumentException | MathArithmeticException | NotPositiveException e) {
            //expected
        }
    }

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.1));
        samples.add(new Pair<>(Integer.valueOf(2), 0.2));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));
        samples.add(new Pair<>(Integer.valueOf(4), 0.4));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            assertEquals(0.1, distribution.probability(Integer.valueOf(1)), 0.001);
            assertEquals(0.2, distribution.probability(Integer.valueOf(2)), 0.001);
            assertEquals(0.3, distribution.probability(Integer.valueOf(3)), 0.001);
            assertEquals(0.4, distribution.probability(Integer.valueOf(4)), 0.001);
            assertNull(distribution.probability(Integer.valueOf(5)));
        } catch (MathIllegalArgumentException | NotPositiveException | MathArithmeticException e) {
            //expected
        }
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.1));
        samples.add(new Pair<>(Integer.valueOf(2), 0.2));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));
        samples.add(new Pair<>(Integer.valueOf(4), 0.4));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            RandomGenerator rng = new Well19937c();
            distribution.reseedRandomGenerator(1234);

            for (int i = 0; i < 10; i++) {
                Integer value = distribution.sample();
                System.out.println(value);
            }
        } catch (MathIllegalArgumentException | MathArithmeticException | NotPositiveException e) {
            //expected
        }
    }

    @Test
    public void testSampleArray() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.1));
        samples.add(new Pair<>(Integer.valueOf(2), 0.2));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));
        samples.add(new Pair<>(Integer.valueOf(4), 0.4));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

            int[] expected = {1, 1, 2, 3, 3, 3, 4, 4, 4, 4};

            try {
                distribution.sample(11);
                assertTrue(MathArrays.isSubset(distribution.singletons.toArray(), expected));
            } catch (NotStrictlyPositiveException e) {
                //expected
            }
        } catch (MathIllegalArgumentException | MathArithmeticException | NotPositiveException e) {
            //expected
        }
    }
}