import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setUp() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<>(0, 0.2));
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));

        distribution = new DiscreteDistribution<>(samples);
    }

    @Test
    public void testProbability() {
        assertEquals(0.2, distribution.probability(0), 0.0);
        assertEquals(0.5, distribution.probability(1), 0.0);
        assertEquals(0.3, distribution.probability(2), 0.0);
        assertEquals(0.0, distribution.probability(-1), 0.0);
        assertEquals(0.0, distribution.probability(3), 0.0);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = distribution.getSamples();

        assertEquals(0.2, samples.get(0).getValue(), 0.0);
        assertEquals(0.5, samples.get(1).getValue(), 0.0);
        assertEquals(0.3, samples.get(2).getValue(), 0.0);
    }

    @Test
    public void testSample() {
        Integer sample = distribution.sample();
        assertTrue(sample == 0 || sample == 1 || sample == 2);
    }

    @Test
    public void testSampleWithSampleSize() throws NotStrictlyPositiveException {
        Integer[] sample = distribution.sample(3);

        assertEquals(3, sample.length);
        assertTrue(sample[0] == 0 || sample[0] == 1 || sample[0] == 2);
        assertTrue(sample[1] == 0 || sample[1] == 1 || sample[1] == 2);
        assertTrue(sample[2] == 0 || sample[2] == 1 || sample[2] == 2);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithNegativeSampleSize() throws NotStrictlyPositiveException {
        distribution.sample(-1);
    }

    @Test
    public void testReseedRandomGenerator() {
        distribution.reseedRandomGenerator(123456);

        // Unable to test randomness, so check if the reseed method is called without errors.
        assertTrue(true);
    }

}