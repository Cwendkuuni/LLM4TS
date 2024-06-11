import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionConstruction() throws Exception {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        assertEquals(3, distribution.singletons.size());
        assertEquals(Integer.valueOf(1), distribution.singletons.get(0));
        assertEquals(Integer.valueOf(2), distribution.singletons.get(1));
        assertEquals(Integer.valueOf(3), distribution.singletons.get(2));

        assertEquals(3, distribution.probabilities.length);
        assertEquals(0.3, distribution.probabilities[0], 0.001);
        assertEquals(0.4, distribution.probabilities[1], 0.001);
        assertEquals(0.3, distribution.probabilities[2], 0.001);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeProbability() throws Exception {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), -0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testZeroProbabilitiesSum() throws Exception {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.1));
        samples.add(new Pair<>(Integer.valueOf(2), 0.1));
        samples.add(new Pair<>(Integer.valueOf(3), 0.1));
        samples.add(new Pair<>(Integer.valueOf(4), -0.1));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInfiniteProbability() throws Exception {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
    }

    @Test
    public void testProbability() throws Exception {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(singletonsAndProbabilities(0.3, 0.4, 0.3));

        Double probability = distribution.probability(Integer.valueOf(1));
        assertEquals(0.3, probability, 0.001);
    }

    @Test
    public void testSample() throws Exception {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(singletonsAndProbabilities(0.3, 0.4, 0.3));

        Integer sample = distribution.sample();
        List<Integer> samples = new ArrayList<>();
        samples.add(sample);
        for (int i = 0; i < 99; i++) {
            sample = distribution.sample();
            samples.add(sample);
        }

        int expectedCount = 30;
        int actualCount = 0;
        for (Integer sampleValue : samples) {
            if (sampleValue.equals(Integer.valueOf(1))) {
                actualCount++;
            }
            if (actualCount > expectedCount) {
                break;
            }
        }

        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testSampleArray() throws Exception {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(singletonsAndProbabilities(0.3, 0.4, 0.3));

        Integer[] samples = distribution.sample(1000);

        int expectedCount = 300;
        int actualCount = 0;
        for (Integer sampleValue : samples) {
            if (sampleValue.equals(Integer.valueOf(1))) {
                actualCount++;
            }
            if (actualCount > expectedCount) {
                break;
            }
        }

        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testReseedRandomGenerator() throws Exception {
        Long seed = 1L;
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(singletonsAndProbabilities(0.3, 0.4, 0.3));
        distribution.reseedRandomGenerator(seed * 2);
        Integer sample = distribution.sample();

        Integer expectedSample = null;
        for (int i = 0; i < 1000; i++) {
            Integer sampleFromOldSeed = distribution.sample();
            if (sampleFromOldSeed.equals(sample)) {
                expectedSample = sampleFromOldSeed;
                break;
            }
        }

        assertNotNull(expectedSample);
        assertEquals(sample, expectedSample);
    }

    private static List<Pair<Object, Double>> singletonsAndProbabilities(Double... probabilities) {
        List<Pair<Object, Double>> samples = new ArrayList<>();
        for (int i = 0; i < probabilities.length; i++) {
            samples.add(new Pair<>(Integer.valueOf(i + 1), probabilities[i]));
        }
        return samples;
    }

}