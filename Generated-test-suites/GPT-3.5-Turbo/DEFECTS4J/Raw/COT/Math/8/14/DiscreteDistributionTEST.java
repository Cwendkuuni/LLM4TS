import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

public class DiscreteDistributionTest {
    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setUp() {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.5));
        try {
            distribution = new DiscreteDistribution<Integer>(samples);
        } catch (Exception e) {
            fail("Failed to create DiscreteDistribution object: " + e.getMessage());
        }
    }

    @Test
    public void testProbability() {
        assertEquals(0.2, distribution.probability(1), 0.000001);
        assertEquals(0.3, distribution.probability(2), 0.000001);
        assertEquals(0.5, distribution.probability(3), 0.000001);
        assertEquals(0.0, distribution.probability(4), 0.000001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = distribution.getSamples();
        assertEquals(1, samples.get(0).getKey().intValue());
        assertEquals(0.2, samples.get(0).getValue().doubleValue(), 0.000001);
        assertEquals(2, samples.get(1).getKey().intValue());
        assertEquals(0.3, samples.get(1).getValue().doubleValue(), 0.000001);
        assertEquals(3, samples.get(2).getKey().intValue());
        assertEquals(0.5, samples.get(2).getValue().doubleValue(), 0.000001);
    }

    @Test
    public void testSample() {
        Integer sample = distribution.sample();
        assertTrue(Arrays.asList(1, 2, 3).contains(sample));
    }

    @Test
    public void testSampleWithSampleSize() {
        int sampleSize = 10000;
        Integer[] samples = distribution.sample(sampleSize);

        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        for (int i = 0; i < sampleSize; i++) {
            if (samples[i] == 1) {
                count1++;
            } else if (samples[i] == 2) {
                count2++;
            } else if (samples[i] == 3) {
                count3++;
            }
        }

        double ratio1 = (double) count1 / sampleSize;
        double ratio2 = (double) count2 / sampleSize;
        double ratio3 = (double) count3 / sampleSize;

        assertEquals(0.2, ratio1, 0.01);
        assertEquals(0.3, ratio2, 0.01);
        assertEquals(0.5, ratio3, 0.01);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithInvalidSampleSize() {
        distribution.sample(-1);
    }
}