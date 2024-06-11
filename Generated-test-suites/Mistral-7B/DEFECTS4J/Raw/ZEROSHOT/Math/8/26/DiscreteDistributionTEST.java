import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionConstruction() throws Exception {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.2));
        samples.add(new Pair<>(Integer.valueOf(2), 0.3));
        samples.add(new Pair<>(Integer.valueOf(3), 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        assertEquals(3, distribution.singletons.size());
        assertEquals(0.2, distribution.probability(Integer.valueOf(1)), 0.01);
        assertEquals(0.3, distribution.probability(Integer.valueOf(2)), 0.01);
        assertEquals(0.5, distribution.probability(Integer.valueOf(3)), 0.01);

        List<Pair<Integer, Double>> samplesList = distribution.getSamples();
        assertEquals(3, samplesList.size());
        assertEquals(Integer.valueOf(1), samplesList.get(0).getKey());
        assertEquals(0.2, samplesList.get(0).getValue(), 0.01);
        assertEquals(Integer.valueOf(2), samplesList.get(1).getKey());
        assertEquals(0.3, samplesList.get(1).getValue(), 0.01);
        assertEquals(Integer.valueOf(3), samplesList.get(2).getKey());
        assertEquals(0.5, samplesList.get(2).getValue(), 0.01);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeProbability() throws Exception {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), -0.2));

        new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInfiniteProbability() throws Exception {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), Double.POSITIVE_INFINITY));

        try {
            new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathArithmeticException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test(expected = MathArithmeticException.class)
    public void testZeroProbabilitySum() throws Exception {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.2));
        samples.add(new Pair<>(Integer.valueOf(2), -0.1));

        try {
            new DiscreteDistribution<>(samples);
        } catch (NotPositiveException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test
    public void testSample() throws Exception {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.2));
        samples.add(new Pair<>(Integer.valueOf(2), 0.3));
        samples.add(new Pair<>(Integer.valueOf(3), 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        int sample = 0;
        for (int i = 0; i < 100; i++) {
            sample = distribution.sample().intValue();
        }

        int expectedCount = 0;
        for (int i = 0; i < distribution.singletons.size(); i++) {
            expectedCount += Math.round(100 * distribution.probability(distribution.singletons.get(i)));
        }

        assertEquals(expectedCount, sample);
    }

    @Test
    public void testSampleSize() throws Exception {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.2));
        samples.add(new Pair<>(Integer.valueOf(2), 0.3));
        samples.add(new Pair<>(Integer.valueOf(3), 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        Integer[] samplesArray = distribution.sample(10);

        assertEquals(10, samplesArray.length);
        for (int i = 0; i < 10; i++) {
            int sample = samplesArray[i].intValue();
            int expectedCount = 0;
            for (int j = 0; j < distribution.singletons.size(); j++) {
                expectedCount += Math.round(100 * distribution.probability(distribution.singletons.get(j)));
            }

            assertEquals(expectedCount, sample.intValue());
        }
    }
}