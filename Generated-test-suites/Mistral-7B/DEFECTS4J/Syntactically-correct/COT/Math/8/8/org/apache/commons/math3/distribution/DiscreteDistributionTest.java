import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testConstructorWithValidSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        assertEquals(3, distribution.singletons.size());
        assertEquals(0.3, distribution.probability(1), 0.001);
        assertEquals(0.4, distribution.probability(2), 0.001);
        assertEquals(0.3, distribution.probability(3), 0.001);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeSampleProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithZeroProbabilitiesSum() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, -0.1));

        new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithInfiniteProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        new DiscreteDistribution<>(samples);
    }

    @Test
    public void testProbabilityMethod() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples());

        assertEquals(0.3, distribution.probability(1), 0.001);
        assertEquals(0.4, distribution.probability(2), 0.001);
        assertEquals(0.3, distribution.probability(3), 0.001);
        assertEquals(0.0, distribution.probability(4), 0.001);
    }

    @Test
    public void testGetSamplesMethod() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples());

        List<Pair<Integer, Double>> samples = distribution.getSamples();

        assertEquals(3, samples.size());
        assertEquals(0.3, samples.get(0).getValue(), 0.001);
        assertEquals(0.4, samples.get(1).getValue(), 0.001);
        assertEquals(0.3, samples.get(2).getValue(), 0.001);
    }

    @Test
    public void testSampleMethod() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples());

        RandomGenerator rng = new Well19937c();
        rng.setSeed(System.currentTimeMillis());

        distribution.reseedRandomGenerator(rng.nextLong());

        int sample = distribution.sample().intValue();

        assertEquals(1, sample);
        assertEquals(0.3, distribution.probability(1), 0.001);
    }

    @Test
    public void testSampleArrayMethod() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples());

        RandomGenerator rng = new Well19937c();
        rng.setSeed(System.currentTimeMillis());

        distribution.reseedRandomGenerator(rng.nextLong());

        int[] samples = distribution.sample(5);

        int[] expected = {1, 1, 2, 3, 3};

        assertArrayEquals(expected, samples);
    }

    private List<Pair<Integer, Double>> samples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));
        return samples;
    }
}