import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        // Create sample data
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.5));

        // Create the distribution object
        DiscreteDistribution<String> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }

        // Check probabilities
        assertEquals(0.2, distribution.probability("A"), 1e-8);
        assertEquals(0.3, distribution.probability("B"), 1e-8);
        assertEquals(0.5, distribution.probability("C"), 1e-8);
    }

    @Test
    public void testGetSamples() {
        // Create sample data
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.5));

        // Create the distribution object
        DiscreteDistribution<String> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }

        // Check samples
        List<Pair<String, Double>> result = distribution.getSamples();
        assertEquals(samples.size(), result.size());
        for (int i = 0; i < samples.size(); i++) {
            assertEquals(samples.get(i), result.get(i));
        }
    }

    @Test
    public void testSample() {
        // Create sample data
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.5));

        // Create the distribution object
        DiscreteDistribution<String> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }

        // Generate random samples
        int numSamples = 1000;
        String[] randomSamples = distribution.sample(numSamples);

        // Check sample sizes
        assertEquals(numSamples, randomSamples.length);

        // Check each sample value
        int countA = 0;
        int countB = 0;
        int countC = 0;
        for (String sample : randomSamples) {
            if (sample.equals("A")) {
                countA++;
            } else if (sample.equals("B")) {
                countB++;
            } else if (sample.equals("C")) {
                countC++;
            }
        }

        // Check proportions
        assertEquals(0.2, (double) countA/numSamples, 0.05);
        assertEquals(0.3, (double) countB/numSamples, 0.05);
        assertEquals(0.5, (double) countC/numSamples, 0.05);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNegative() throws NotStrictlyPositiveException {
        // Create sample data
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.5));

        // Create the distribution object
        DiscreteDistribution<String> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            e.printStackTrace();
        }

        // Try generating negative number of samples
        int numSamples = -1;
        distribution.sample(numSamples);
    }
}