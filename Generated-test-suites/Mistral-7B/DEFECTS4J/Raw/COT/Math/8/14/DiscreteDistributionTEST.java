import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionConstructorWithValidArguments() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        // Checksing the singletons and probabilities
        assertEquals(3, distribution.singletons.size());
        assertEquals(1, distribution.singletons.get(0));
        assertEquals(2, distribution.singletons.get(1));
        assertEquals(3, distribution.singletons.get(2));
        assertArrayEquals(new double[]{0.3, 0.4, 0.3}, distribution.probabilities, 0.0);
    }

    @Test(expected = NotPositiveException.class)
    public void testDiscreteDistributionConstructorWithNegativeProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDiscreteDistributionConstructorWithZeroProbabilitiesSum() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.2));
        samples.add(new Pair<>(3, 0.1));
        samples.add(new Pair<>(4, -0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testDiscreteDistributionConstructorWithInfiniteProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test
    public void testProbabilityMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        // Testing probability of valid arguments
        assertEquals(0.3, distribution.probability(1), 0.0);
        assertEquals(0.4, distribution.probability(2), 0.0);
        assertEquals(0.3, distribution.probability(3), 0.0);
        // Testing probability of invalid arguments
        assertNull(distribution.probability(null));
        assertNull(distribution.probability(new Integer(5)));
    }

    @Test
    public void testGetSamplesMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        List<Pair<Integer, Double>> result = distribution.getSamples();

        assertEquals(3, result.size());
        for (int i = 0; i < result.size(); i++) {
            assertNotNull(result.get(i));
            assertEquals(i + 1, result.get(i).getKey());
            assertEquals(distribution.probabilities[i], result.get(i).getValue(), 0.0);
        }
    }

    @Test
    public void testSampleMethodWithOneSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Integer result = distribution.sample();

        assertEquals(1, result.intValue());
    }

    @Test
    public void testSampleMethodWithMultipleSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        Integer[] samplesArray = distribution.sample(3);

        assertArrayEquals(new Integer[]{1, 2, 1}, samplesArray);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleMethodWithNegativeSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution distribution = new DiscreteDistribution<>(new Well19937c(), samples);
        distribution.sample(-1);
    }
}