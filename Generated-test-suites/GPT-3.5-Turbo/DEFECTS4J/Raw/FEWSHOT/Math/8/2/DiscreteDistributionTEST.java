import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;
    private RandomGenerator rng;

    @Before
    public void setup() {
        rng = new Well19937c();
    }

    @Test
    public void testProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.1));
        samples.add(new Pair<Integer, Double>(2, 0.2));
        samples.add(new Pair<Integer, Double>(3, 0.3));
        samples.add(new Pair<Integer, Double>(4, 0.4));

        distribution = new DiscreteDistribution<Integer>(samples);

        assertEquals(0.1, distribution.probability(1), 0.0001);
        assertEquals(0.2, distribution.probability(2), 0.0001);
        assertEquals(0.3, distribution.probability(3), 0.0001);
        assertEquals(0.4, distribution.probability(4), 0.0001);
    }

    @Test
    public void testSample() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.1));
        samples.add(new Pair<Integer, Double>(2, 0.2));
        samples.add(new Pair<Integer, Double>(3, 0.3));
        samples.add(new Pair<Integer, Double>(4, 0.4));

        distribution = new DiscreteDistribution<Integer>(samples);

        Integer[] sample1 = distribution.sample(10);
        assertEquals(10, sample1.length);

        Integer[] sample2 = distribution.sample(100);
        assertEquals(100, sample2.length);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNotStrictlyPositiveException() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.1));
        samples.add(new Pair<Integer, Double>(2, 0.2));
        samples.add(new Pair<Integer, Double>(3, 0.3));
        samples.add(new Pair<Integer, Double>(4, 0.4));

        distribution = new DiscreteDistribution<Integer>(samples);

        distribution.sample(0);
    }

    @Test
    public void testGetSamples() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.1));
        samples.add(new Pair<Integer, Double>(2, 0.2));
        samples.add(new Pair<Integer, Double>(3, 0.3));
        samples.add(new Pair<Integer, Double>(4, 0.4));

        distribution = new DiscreteDistribution<Integer>(samples);

        List<Pair<Integer, Double>> result = distribution.getSamples();

        assertEquals(samples, result);
    }
}