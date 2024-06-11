import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionConstructorWithValidArguments() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        assertEquals(3, distribution.singletons.size());
        assertEquals(1, distribution.singletons.get(0));
        assertEquals(2, distribution.singletons.get(1));
        assertEquals(3, distribution.singletons.get(2));

        assertArrayEquals(new double[]{0.3, 0.4, 0.3}, distribution.probabilities, 0.0001);
    }

    @Test(expected = NotPositiveException.class)
    public void testDiscreteDistributionConstructorWithNegativeProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, -0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDiscreteDistributionConstructorWithZeroProbabilitiesSum() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, -0.1));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testDiscreteDistributionConstructorWithInfProbability() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, Double.POSITIVE_INFINITY));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);
    }

    @Test
    public void testProbabilityMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        assertEquals(0.3, distribution.probability((Integer)1), 0.0001);
        assertEquals(0.4, distribution.probability((Integer)2), 0.0001);
        assertEquals(0.3, distribution.probability((Integer)3), 0.0001);
        assertNull(distribution.probability((Integer)4));
    }

    @Test
    public void testGetSamplesMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        List<Pair<Integer, Double>> result = distribution.getSamples();

        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getKey());
        assertEquals(0.3, result.get(0).getValue(), 0.0001);
        assertEquals(2, result.get(1).getKey());
        assertEquals(0.4, result.get(1).getValue(), 0.0001);
        assertEquals(3, result.get(2).getKey());
        assertEquals(0.3, result.get(2).getValue(), 0.0001);
    }

    @Test
    public void testSampleMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Integer result = distribution.sample();

        assertTrue(samples.contains(new Pair<>(result, 0.3)) || samples.contains(new Pair<>(result, 0.4)) || samples.contains(new Pair<>(result, 0.3)));
    }

    @Test
    public void testSampleMethodWithMultipleCalls() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Set<Integer> resultSet = new HashSet<>();

        for (int i = 0; i < 100; i++) {
            resultSet.add(distribution.sample());
        }

        assertEquals(samples.size(), resultSet.size());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleMethodWithNegativeSampleSize() throws NotStrictlyPositiveException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        distribution.sample(-1);
    }

    @Test
    public void testSampleMethodWithValidSampleSize() throws NotStrictlyPositiveException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.3));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(new Well19937c(), samples);

        Integer[] result = distribution.sample(5);

        assertArrayEquals(new Integer[]{1, 2, 1, 3, 3}, result);
    }
}