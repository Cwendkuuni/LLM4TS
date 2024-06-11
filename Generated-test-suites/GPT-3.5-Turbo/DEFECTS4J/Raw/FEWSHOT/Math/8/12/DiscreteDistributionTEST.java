import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscreteDistributionTest {
    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setup() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.5));
        samples.add(new Pair<>(3, 0.2));
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testProbability() {
        assertEquals(0.3, distribution.probability(1), 0.0001);
        assertEquals(0.5, distribution.probability(2), 0.0001);
        assertEquals(0.2, distribution.probability(3), 0.0001);
        assertEquals(0.0, distribution.probability(0), 0.0001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> expectedSamples = new ArrayList<>();
        expectedSamples.add(new Pair<>(1, 0.3));
        expectedSamples.add(new Pair<>(2, 0.5));
        expectedSamples.add(new Pair<>(3, 0.2));
        assertEquals(expectedSamples, distribution.getSamples());
    }

    @Test
    public void testSample() {
        Integer sample = distribution.sample();
        assertTrue(Arrays.asList(1, 2, 3).contains(sample));
    }

    @Test
    public void testSampleWithSampleSize() {
        Integer[] samples = distribution.sample(5);
        assertEquals(5, samples.length);
        for (Integer sample : samples) {
            assertTrue(Arrays.asList(1, 2, 3).contains(sample));
        }
    }
}