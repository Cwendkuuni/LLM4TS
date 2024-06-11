import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;
    private List<Pair<Integer, Double>> samples;

    @Before
    public void setUp() {
        samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testProbability_ValidInput() {
        assertEquals(0.2, distribution.probability(1), 0.0001);
    }

    @Test
    public void testProbability_NullInput() {
        assertEquals(0.2, distribution.probability(null), 0.0001);
    }

    @Test
    public void testProbability_InvalidInput() {
        assertEquals(0.0, distribution.probability(4), 0.0001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> expectedSamples = new ArrayList<>();
        expectedSamples.add(new Pair<>(1, 0.2));
        expectedSamples.add(new Pair<>(2, 0.3));
        expectedSamples.add(new Pair<>(3, 0.5));

        List<Pair<Integer, Double>> actualSamples = distribution.getSamples();

        assertEquals(expectedSamples.get(0).getKey(), actualSamples.get(0).getKey());
        assertEquals(expectedSamples.get(0).getValue(), actualSamples.get(0).getValue(), 0.0001);
        assertEquals(expectedSamples.get(1).getKey(), actualSamples.get(1).getKey());
        assertEquals(expectedSamples.get(1).getValue(), actualSamples.get(1).getValue(), 0.0001);
        assertEquals(expectedSamples.get(2).getKey(), actualSamples.get(2).getKey());
        assertEquals(expectedSamples.get(2).getValue(), actualSamples.get(2).getValue(), 0.0001);
    }

    @Test
    public void testSample() {
        int[] counts = new int[3];

        for (int i = 0; i < 10000; i++) {
            int sample = distribution.sample();
            counts[sample - 1]++;
        }

        assertEquals(2000, counts[0], 200);
        assertEquals(3000, counts[1], 200);
        assertEquals(5000, counts[2], 200);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSample_InvalidInput() throws NotStrictlyPositiveException {
        distribution.sample(-1);
    }

    @Test
    public void testSample_ValidInput() {
        int sampleSize = 1000;
        Integer[] samples = distribution.sample(sampleSize);

        assertEquals(sampleSize, samples.length);

        for (int i = 0; i < sampleSize; i++) {
            assertNotNull(samples[i]);
        }
    }

}