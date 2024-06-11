import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setUp() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        distribution = new DiscreteDistribution<>(samples);
    }

    @Test
    public void testProbability() {
        double delta = 1e-15;

        assertEquals(0.5, distribution.probability(1), delta);
        assertEquals(0.3, distribution.probability(2), delta);
        assertEquals(0.2, distribution.probability(3), delta);

        assertEquals(0, distribution.probability(0), delta);
        assertEquals(0, distribution.probability(4), delta);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> expectedSamples = new ArrayList<>();
        expectedSamples.add(new Pair<>(1, 0.5));
        expectedSamples.add(new Pair<>(2, 0.3));
        expectedSamples.add(new Pair<>(3, 0.2));

        List<Pair<Integer, Double>> samples = distribution.getSamples();

        assertEquals(expectedSamples.size(), samples.size());

        for(int i = 0; i < samples.size(); i++) {
            Pair<Integer, Double> expected = expectedSamples.get(i);
            Pair<Integer, Double> actual = samples.get(i);

            assertEquals(expected.getKey(), actual.getKey());
            assertEquals(expected.getValue(), actual.getValue(), 1e-15);
        }
    }

    @Test
    public void testSample() {
        int sampleSize = 100000;
        int[] expectedCounts = new int[] {50000, 30000, 20000};
        int[] actualCounts = new int[3];

        for (int i = 0; i < sampleSize; i++) {
            int sample = distribution.sample();
            actualCounts[sample - 1]++;
        }

        double delta = sampleSize * 0.01;
        for (int i = 0; i < actualCounts.length; i++) {
            assertEquals(expectedCounts[i], actualCounts[i], delta);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNegativeSize() throws NotStrictlyPositiveException {
        int sampleSize = -1;
        distribution.sample(sampleSize);
    }

    @Test
    public void testSampleZeroSize() throws NotStrictlyPositiveException {
        int sampleSize = 0;
        Integer[] samples = distribution.sample(sampleSize);
        assertEquals(0, samples.length);
    }

    @Test
    public void testSamplePositiveSize() throws NotStrictlyPositiveException {
        int sampleSize = 10;
        Integer[] samples = distribution.sample(sampleSize);
        assertEquals(sampleSize, samples.length);
    }

}