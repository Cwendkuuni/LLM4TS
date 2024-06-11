import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testDiscreteDistributionConstructorNegativeProbabilities() {
        List<Pair<Object, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1.0, -0.5));
        try {
            new DiscreteDistribution<Object>(samples);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testDiscreteDistributionConstructorZeroProbabilitiesSum() {
        List<Pair<Object, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1.0, 0.5));
        samples.add(new Pair<>(2.0, 0.5));
        try {
            new DiscreteDistribution<Object>(samples);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testDiscreteDistributionConstructorInfiniteProbabilities() {
        List<Pair<Object, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1.0, Double.POSITIVE_INFINITY));
        try {
            new DiscreteDistribution<Object>(samples);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);

        assertEquals(0.2, distribution.probability((Integer) 1), 0.001);
        assertEquals(0.3, distribution.probability((Integer) 2), 0.001);
        assertEquals(0.5, distribution.probability((Integer) 3), 0.001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);
        List<Pair<Integer, Double>> result = distribution.getSamples();

        assertEquals(samples.size(), result.size());
        for (int i = 0; i < samples.size(); i++) {
            assertEquals(samples.get(i).getKey(), result.get(i).getKey());
            assertEquals(samples.get(i).getValue(), result.get(i).getValue(), 0.001);
        }
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);

        Object sample = distribution.sample();
        assertTrue(samples.contains(new Pair<>((Integer) sample, distribution.probability((Integer) sample))));
    }

    @Test
    public void testSampleArray() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<Integer>(new Well19937c(), samples);

        try {
            distribution.sample(0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }

        Integer[] sampleArray = distribution.sample(5);
        for (Integer sample : sampleArray) {
            assertTrue(samples.contains(new Pair<>((Integer) sample, distribution.probability((Integer) sample))));
        }
    }

}