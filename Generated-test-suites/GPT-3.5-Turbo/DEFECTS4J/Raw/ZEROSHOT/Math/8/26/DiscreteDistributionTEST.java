import static org.junit.Assert.*;

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

    @Before
    public void setUp() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        distribution = new DiscreteDistribution<>(samples);
    }

    @Test
    public void testProbability() {
        assertEquals(0.2, distribution.probability(1), 0.0001);
        assertEquals(0.3, distribution.probability(2), 0.0001);
        assertEquals(0.5, distribution.probability(3), 0.0001);
        assertEquals(0.0, distribution.probability(4), 0.0001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = distribution.getSamples();
        assertEquals(1, (int) samples.get(0).getKey());
        assertEquals(0.2, samples.get(0).getValue(),  0.0001);
        assertEquals(2, (int) samples.get(1).getKey());
        assertEquals(0.3, samples.get(1).getValue(), 0.0001);
        assertEquals(3, (int) samples.get(2).getKey());
        assertEquals(0.5, samples.get(2).getValue(), 0.0001);
    }

    @Test
    public void testSample() {
        RandomGenerator rng = new Well19937c();
        distribution.reseedRandomGenerator(12345L);
        rng.setSeed(12345L);
        int sample = distribution.sample();
        assertTrue(sample >= 1 && sample <= 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleInvalidSize() throws NotStrictlyPositiveException {
        distribution.sample(0);
    }

    @Test
    public void testSampleArray() throws NotStrictlyPositiveException {
        distribution.reseedRandomGenerator(12345L);
        Integer[] sampleArray = distribution.sample(5);
        assertEquals(5, sampleArray.length);
        for (Integer sample : sampleArray) {
            assertTrue(sample >= 1 && sample <= 3);
        }
    }
}