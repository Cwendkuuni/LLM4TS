package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;

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
    public void testConstructor() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.5));

        DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);

        assertEquals(samples, dist.getSamples());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_NotPositiveException() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, -0.2));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.5));

        DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructor_MathArithmeticException() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.4));

        DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructor_MathIllegalArgumentException() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(null, Double.POSITIVE_INFINITY));

        DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);
    }

    @Test
    public void testProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.5));

        DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);

        assertEquals(0.2, dist.probability(1), 0.0001);
        assertEquals(0.3, dist.probability(2), 0.0001);
        assertEquals(0.5, dist.probability(3), 0.0001);
        assertEquals(0, dist.probability(4), 0.0001);
    }

    @Test
    public void testSample() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.5));

        DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);

        RandomGenerator rng = new Well19937c();

        int numSamples = 1000;
        int numOne = 0, numTwo = 0, numThree = 0;

        for (int i = 0; i < numSamples; i++) {
            int sample = dist.sample(rng);
            switch (sample) {
                case 1:
                    numOne++;
                    break;
                case 2:
                    numTwo++;
                    break;
                case 3:
                    numThree++;
                    break;
            }
        }

        assertEquals(200, numOne, 20);
        assertEquals(300, numTwo, 20);
        assertEquals(500, numThree, 20);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSample_NotStrictlyPositiveException() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException, NotStrictlyPositiveException {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.5));

        DiscreteDistribution<Integer> dist = new DiscreteDistribution<Integer>(samples);

        dist.sample(0);
    }
}