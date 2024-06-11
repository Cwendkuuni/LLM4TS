import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DiscreteDistributionTest {

    private List<Pair<Object, Double>> samples;
    private DiscreteDistribution<Object> distribution;

    @Before
    public void setUp() {
        samples = Arrays.asList(new Pair<Object, Double>(1.0, 0.3),
                new Pair<Object, Double>(2.0, 0.4),
                new Pair<Object, Double>(3.0, 0.3));
        distribution = new DiscreteDistribution<Object>(new Well19937c(), samples);
    }

    @Test
    public void testProbability() {
        assertEquals(0.3, distribution.probability(1.0), 0.001);
        assertEquals(0.4, distribution.probability(2.0), 0.001);
        assertEquals(0.3, distribution.probability(3.0), 0.001);
        assertNull(distribution.probability(4.0));
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeProbabilityThrowsException() {
        List<Pair<Object, Double>> negativeProbabilitiesSamples = Arrays.asList(
                new Pair<Object, Double>(1.0, -0.3),
                new Pair<Object, Double>(2.0, -0.4),
                new Pair<Object, Double>(3.0, -0.3));

        new DiscreteDistribution<Object>(new Well19937c(), negativeProbabilitiesSamples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testZeroProbabilitiesThrowsException() {
        List<Pair<Object, Double>> zeroProbabilitiesSamples = Arrays.asList(
                new Pair<Object, Double>(1.0, 0.0),
                new Pair<Object, Double>(2.0, 0.0),
                new Pair<Object, Double>(3.0, 0.0));

        new DiscreteDistribution<Object>(new Well19937c(), zeroProbabilitiesSamples);
    }

    @Test
    public void testSample() {
        Object sample = distribution.sample();
        assertEquals(1.0, sample, 0);
        assertFalse(samples.contains(new Pair<Object, Double>(4.0, 0.0)));
        sample = distribution.sample();
        assertEquals(2.0, sample, 0);
        sample = distribution.sample();
        assertEquals(3.0, sample, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSampleOutOfBounds() {
        distribution.sample(samples.size() + 1);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Object, Double>> samplesList = distribution.getSamples();
        assertEquals(samples.size(), samplesList.size());
        for (int i = 0; i < samples.size(); i++) {
            assertEquals(samples.get(i).getKey(), samplesList.get(i).getKey());
            assertEquals(samples.get(i).getValue(), samplesList.get(i).getValue(), 0.001);
        }
    }

    @Test
    public void testReseedRandomGenerator() {
        Long seed = 1L;
        distribution.reseedRandomGenerator(seed);
        Long newSeed = distribution.random.getSeed();
        assertEquals(seed, newSeed);
    }
}