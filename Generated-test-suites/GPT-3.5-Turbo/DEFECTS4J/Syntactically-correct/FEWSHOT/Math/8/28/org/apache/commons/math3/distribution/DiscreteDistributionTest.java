import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {
    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setUp() {
        distribution = createDistribution();
    }

    @Test
    public void testProbability() {
        assertEquals(0.2, distribution.probability(1), Double.MIN_VALUE);
        assertEquals(0.3, distribution.probability(2), Double.MIN_VALUE);
        assertEquals(0.5, distribution.probability(3), Double.MIN_VALUE);
        assertEquals(0.0, distribution.probability(4), Double.MIN_VALUE);
        assertEquals(0.0, distribution.probability(null), Double.MIN_VALUE);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = distribution.getSamples();
        assertNotNull(samples);
        assertEquals(4, samples.size());

        List<Pair<Integer, Double>> expectedSamples = new ArrayList<Pair<Integer, Double>>();
        expectedSamples.add(new Pair<Integer, Double>(1, 0.2));
        expectedSamples.add(new Pair<Integer, Double>(2, 0.3));
        expectedSamples.add(new Pair<Integer, Double>(3, 0.5));
        expectedSamples.add(new Pair<Integer, Double>(4, 0.0));

        for (int i = 0; i < samples.size(); i++) {
            Pair<Integer, Double> expectedPair = expectedSamples.get(i);
            Pair<Integer, Double> actualPair = samples.get(i);

            assertEquals(expectedPair.getKey(), actualPair.getKey());
            assertEquals(expectedPair.getValue(), actualPair.getValue(), Double.MIN_VALUE);
        }
    }

    @Test
    public void testReseedRandomGenerator() {
        distribution.reseedRandomGenerator(123);
        // No assertion as this is just testing the setter
    }

    @Test
    public void testSample() {
        Integer sample = distribution.sample();
        assertNotNull(sample);
        // No assertion as this is a random value
    }

    @Test
    public void testSampleWithSampleSize() {
        int sampleSize = 3;
        Integer[] sample = null;
        try {
            sample = distribution.sample(sampleSize);
        } catch (NotStrictlyPositiveException e) {
            fail("Should not throw NotStrictlyPositiveException");
        }

        assertNotNull(sample);
        assertEquals(sampleSize, sample.length);
        // No assertion as this is a random value
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithNegativeSampleSize() throws NotStrictlyPositiveException {
        int sampleSize = -1;
        distribution.sample(sampleSize);
    }

    private DiscreteDistribution<Integer> createDistribution() {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.5));
        samples.add(new Pair<Integer, Double>(4, 0.0));

        try {
            return new DiscreteDistribution<Integer>(new Well19937c(), samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Should not throw exception");
        }

        return null;
    }
}