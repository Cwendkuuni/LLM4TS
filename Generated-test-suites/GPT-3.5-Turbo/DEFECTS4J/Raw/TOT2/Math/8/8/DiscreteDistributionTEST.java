import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            assertEquals(0.5, distribution.probability(1), 0.0001);
            assertEquals(0.3, distribution.probability(2), 0.0001);
            assertEquals(0.2, distribution.probability(3), 0.0001);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            List<Pair<Integer, Double>> retrievedSamples = distribution.getSamples();
            assertEquals(samples.size(), retrievedSamples.size());
            for (int i = 0; i < samples.size(); i++) {
                assertEquals(samples.get(i).getFirst(), retrievedSamples.get(i).getFirst());
                assertEquals(samples.get(i).getSecond(), retrievedSamples.get(i).getSecond());
            }
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            Integer sample = distribution.sample();
            assertNotNull(sample);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.5));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.2));

        try {
            DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
            int sampleSize = 5;
            Integer[] sampleArray = distribution.sample(sampleSize);
            assertNotNull(sampleArray);
            assertEquals(sampleSize, sampleArray.length);
        } catch (NotPositiveException | MathArithmeticException | MathIllegalArgumentException e) {
            fail("Exception thrown when not expected");
        }
    }
}