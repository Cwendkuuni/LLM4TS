import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

public class DiscreteDistributionTest {

    private List<Pair<String, Double>> samples;
    private DiscreteDistribution<String> distribution;

    @Before
    public void setUp() {
        samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.1));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.4));
        samples.add(new Pair<>("D", 0.2));

        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProbability() {
        assertEquals(0.1, distribution.probability("A"), 0.00001);
        assertEquals(0.3, distribution.probability("B"), 0.00001);
        assertEquals(0.4, distribution.probability("C"), 0.00001);
        assertEquals(0.2, distribution.probability("D"), 0.00001);

        // Probability of nonexistent value should be 0
        assertEquals(0.0, distribution.probability("E"), 0.00001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<String, Double>> actualSamples = distribution.getSamples();

        for (int i = 0; i < samples.size(); i++) {
            Pair<String, Double> expected = samples.get(i);
            Pair<String, Double> actual = actualSamples.get(i);

            assertEquals(expected.getKey(), actual.getKey());
            assertEquals(expected.getValue(), actual.getValue(), 0.00001);
        }
    }

    @Test
    public void testSample() {
        List<String> generatedSamples = new ArrayList<>();
        int sampleSize = 100000;

        for (int i = 0; i < sampleSize; i++) {
            String sample = distribution.sample();
            generatedSamples.add(sample);
        }

        double countA = countValueInList(generatedSamples, "A");
        double countB = countValueInList(generatedSamples, "B");
        double countC = countValueInList(generatedSamples, "C");
        double countD = countValueInList(generatedSamples, "D");

        // Check if the generated sample values are distributed according to the probabilities
        assertEquals(0.1, countA / sampleSize, 0.01);
        assertEquals(0.3, countB / sampleSize, 0.01);
        assertEquals(0.4, countC / sampleSize, 0.01);
        assertEquals(0.2, countD / sampleSize, 0.01);
    }

    @Test
    public void testSampleWithSize() {
        int sampleSize = 5;
        String[] samples = null;

        try {
            samples = distribution.sample(sampleSize);
        } catch (NotStrictlyPositiveException e) {
            e.printStackTrace();
        }

        assertEquals(sampleSize, samples.length);

        for (String sample : samples) {
            assertTrue(generatedSamples.contains(sample));
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithInvalidSize() throws NotStrictlyPositiveException {
        int sampleSize = -5;
        distribution.sample(sampleSize);
    }

    private double countValueInList(List<String> list, String value) {
        double count = 0;

        for (String item : list) {
            if (item.equals(value)) {
                count++;
            }
        }

        return count;
    }
}