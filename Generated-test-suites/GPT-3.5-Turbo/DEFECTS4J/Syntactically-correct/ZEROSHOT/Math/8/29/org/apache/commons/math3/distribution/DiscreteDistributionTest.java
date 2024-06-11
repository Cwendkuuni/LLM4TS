import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    private DiscreteDistribution<String> distribution;

    @Before
    public void setUp() {
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.5));

        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
    }

    @Test
    public void testProbability() {
        assertEquals(0.2, distribution.probability("A"), 0.0001);
        assertEquals(0.3, distribution.probability("B"), 0.0001);
        assertEquals(0.5, distribution.probability("C"), 0.0001);
        assertEquals(0.0, distribution.probability("D"), 0.0001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<String, Double>> samples = distribution.getSamples();
        assertEquals(3, samples.size());
        assertEquals(new Pair<>("A", 0.2), samples.get(0));
        assertEquals(new Pair<>("B", 0.3), samples.get(1));
        assertEquals(new Pair<>("C", 0.5), samples.get(2));
    }

    @Test
    public void testSample() {
        for (int i = 0; i < 1000; i++) {
            String sample = distribution.sample();
            assertTrue(sample.equals("A") || sample.equals("B") || sample.equals("C"));
        }
    }

    @Test
    public void testSampleSize() {
        int sampleSize = 10;

        try {
            String[] samples = distribution.sample(sampleSize);
            assertEquals(sampleSize, samples.length);
            for (String sample : samples) {
                assertTrue(sample.equals("A") || sample.equals("B") || sample.equals("C"));
            }
        } catch (Exception e) {
            fail("Exception thrown during sample size test: " + e.getMessage());
        }
    }
}