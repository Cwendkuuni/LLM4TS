import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Well19937c;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionConstruction() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        assertEquals(3, distribution.singletons.size());
        assertEquals(1, distribution.singletons.get(0).intValue());
        assertEquals(2, distribution.singletons.get(1).intValue());
        assertEquals(3, distribution.singletons.get(2).intValue());
        assertArrayEquals(new double[]{0.3, 0.4, 0.3}, distribution.probabilities, 0.001);
    }

    @Test(expected = NotPositiveException.class)
    public void testDiscreteDistributionNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }
    }

    @Test(expected = MathArithmeticException.class)
    public void testDiscreteDistributionZeroProbabilitiesSum() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.0));
        samples.add(new Pair<>(3, 0.4));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testDiscreteDistributionInfiniteProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        try {
            new DiscreteDistribution<>(new Well19937c(), samples);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testProbability() {
        DiscreteDistribution<Integer> distribution = getTestDistribution();

        assertEquals(0.3, distribution.probability(1), 0.001);
        assertEquals(0.4, distribution.probability(2), 0.001);
        assertEquals(0.3, distribution.probability(3), 0.001);
        assertEquals(0, distribution.probability(4), 0.001);
    }

    @Test
    public void testSample() {
        DiscreteDistribution<Integer> distribution = getTestDistribution();

        RandomDataGenerator rng = new RandomDataGenerator();

        for (int i = 0; i < 1000; i++) {
            assertEquals(distribution.singletons.get(rng.nextInt(distribution.singletons.size())).intValue(), distribution.sample().intValue());
        }
    }

    @Test
    public void testSampleArray() {
        DiscreteDistribution<Integer> distribution = getTestDistribution();

        int sampleSize = 10;

        int[] samples = distribution.sample(sampleSize);

        for (int i = 0; i < sampleSize; i++) {
            assertEquals(distribution.singletons.get(i).intValue(), samples[i]);
        }
    }

    private DiscreteDistribution<Integer> getTestDistribution() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        return new DiscreteDistribution<>(new Well19937c(), samples);
    }
}