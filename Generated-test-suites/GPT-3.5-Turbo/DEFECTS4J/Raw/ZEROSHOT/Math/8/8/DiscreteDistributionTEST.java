import static org.junit.Assert.*;

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
    private List<Pair<Integer, Double>> samples;

    @Before
    public void setUp() {
        samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        distribution = new DiscreteDistribution<>(samples);
    }

    @Test
    public void testProbability() {
        assertEquals(0.2, distribution.probability(1), 1e-15);
        assertEquals(0.3, distribution.probability(2), 1e-15);
        assertEquals(0.5, distribution.probability(3), 1e-15);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> expectedSamples = new ArrayList<>();
        expectedSamples.add(new Pair<>(1, 0.2));
        expectedSamples.add(new Pair<>(2, 0.3));
        expectedSamples.add(new Pair<>(3, 0.5));
        assertEquals(expectedSamples, distribution.getSamples());
    }

    @Test
    public void testSample() {
        for (int i = 0; i < 1000; i++) {
            int value = distribution.sample();
            assertTrue(value >= 1 && value <= 3);
        }
    }

    @Test
    public void testSampleSize() throws NotStrictlyPositiveException {
        int sampleSize = 5;
        Integer[] result = distribution.sample(sampleSize);
        assertEquals(sampleSize, result.length);
        for (int i = 0; i < sampleSize; i++) {
            assertTrue(result[i] >= 1 && result[i] <= 3);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleSizeNotStrictlyPositiveException() throws NotStrictlyPositiveException {
        distribution.sample(0);
    }
}