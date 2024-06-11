import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setUp() {
        List<Pair<Integer, Double>> samples = new ArrayList<Pair<Integer, Double>>();
        samples.add(new Pair<Integer, Double>(1, 0.4));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.2));
        samples.add(new Pair<Integer, Double>(4, 0.1));

        try {
            distribution = new DiscreteDistribution<Integer>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            // handle exception
        }
    }

    @Test
    public void testProbability() {
        assertEquals(0.4, distribution.probability(1), 0.0001);
        assertEquals(0.3, distribution.probability(2), 0.0001);
        assertEquals(0.2, distribution.probability(3), 0.0001);
        assertEquals(0.1, distribution.probability(4), 0.0001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = distribution.getSamples();

        assertEquals(4, samples.size());
        assertEquals(new Pair<Integer, Double>(1, 0.4), samples.get(0));
        assertEquals(new Pair<Integer, Double>(2, 0.3), samples.get(1));
        assertEquals(new Pair<Integer, Double>(3, 0.2), samples.get(2));
        assertEquals(new Pair<Integer, Double>(4, 0.1), samples.get(3));
    }
    
    @Test
    public void testSample() {
        Integer sample1 = distribution.sample();
        Integer sample2 = distribution.sample();
        Integer sample3 = distribution.sample();

        // Ensure that the generated samples are within the valid range
        assertEquals(true, sample1 >= 1 && sample1 <= 4);
        assertEquals(true, sample2 >= 1 && sample2 <= 4);
        assertEquals(true, sample3 >= 1 && sample3 <= 4);
    }

    @Test
    public void testSampleSize() {
        int sampleSize = 10;
        Integer[] samples = distribution.sample(sampleSize);

        // Ensure that the generated samples array has the correct length
        assertEquals(sampleSize, samples.length);

        // Ensure that all generated samples are within the valid range
        for (int i = 0; i < sampleSize; i++) {
            assertEquals(true, samples[i] >= 1 && samples[i] <= 4);
        }
    }
}