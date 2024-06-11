import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
    }

    @Test
    public void testProbability() {
        assertEquals(0.2, distribution.probability(1), 0.00001);
        assertEquals(0.3, distribution.probability(2), 0.00001);
        assertEquals(0.5, distribution.probability(3), 0.00001);
        assertEquals(0.0, distribution.probability(0), 0.00001);
        assertEquals(0.0, distribution.probability(4), 0.00001);
        assertEquals(0.0, distribution.probability(null), 0.00001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = distribution.getSamples();
        assertNotNull(samples);
        assertEquals(3, samples.size());

        assertEquals(1, (int) samples.get(0).getKey());
        assertEquals(0.2, samples.get(0).getValue(), 0.00001);

        assertEquals(2, (int) samples.get(1).getKey());
        assertEquals(0.3, samples.get(1).getValue(), 0.00001);

        assertEquals(3, (int) samples.get(2).getKey());
        assertEquals(0.5, samples.get(2).getValue(), 0.00001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNegativeSize() throws NotStrictlyPositiveException {
        distribution.sample(-1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleZeroSize() throws NotStrictlyPositiveException {
        distribution.sample(0);
    }

    @Test
    public void testSample() {
        int sampleSize = 10000;
        Integer[] samples = null;
        try {
            samples = distribution.sample(sampleSize);
        } catch (NotStrictlyPositiveException e) {
            e.printStackTrace();
        }

        assertNotNull(samples);
        assertEquals(sampleSize, samples.length);

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
            } else {
                fail("Unexpected value in sample: " + samples[i]);
            }
        }

        // Check relative frequencies
        assertEquals(0.2, (double) count1 / sampleSize, 0.01);
        assertEquals(0.3, (double) count2 / sampleSize, 0.01);
        assertEquals(0.5, (double) count3 / sampleSize, 0.01);
    }
}