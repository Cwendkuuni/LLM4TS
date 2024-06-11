import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.distribution.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testConstructorWithValidSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        DiscreteDistribution<Integer> distribution = null;
        try {
            distribution = new DiscreteDistribution<>(samples);
        } catch (NotPositiveException | MathIllegalArgumentException | MathArithmeticException e) {
            e.printStackTrace();
            fail("Exception should not be thrown.");
        }

        assertEquals(3, distribution.singletons.size());
        assertEquals(0.3, distribution.probabilities[0], 0.01);
        assertEquals(0.4, distribution.probabilities[1], 0.01);
        assertEquals(0.3, distribution.probabilities[2], 0.01);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(-1), 0.3));

        new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithZeroProbabilitiesSum() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(1), -0.3));

        new DiscreteDistribution<>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithInfProbabilities() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), Double.POSITIVE_INFINITY));

        new DiscreteDistribution<>(samples);
    }

    @Test
    public void testProbabilityMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        assertEquals(0.3, distribution.probability(Integer.valueOf(1)), 0.01);
        assertEquals(0.4, distribution.probability(Integer.valueOf(2)), 0.01);
        assertEquals(0.3, distribution.probability(Integer.valueOf(3)), 0.01);
        assertEquals(0.0, distribution.probability(Integer.valueOf(4)), 0.01);
    }

    @Test
    public void testGetSamplesMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        List<Pair<Integer, Double>> distributionSamples = distribution.getSamples();

        assertEquals(samples.size(), distributionSamples.size());
        for (int i = 0; i < samples.size(); i++) {
            assertEquals(samples.get(i).getKey(), distributionSamples.get(i).getKey());
            assertEquals(samples.get(i).getValue(), distributionSamples.get(i).getValue(), 0.01);
        }
    }

    @Test
    public void testSampleMethod() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        int sample = 0;
        for (int i = 0; i < 1000; i++) {
            sample = distribution.sample().intValue();
        }

        int expectedCount = 0;
        for (int i = 0; i < samples.size(); i++) {
            expectedCount += (int) (samples.get(i).getValue() * 1000);
        }

        assertEquals(expectedCount, sample);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleMethodWithNegativeSampleSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(Integer.valueOf(1), 0.3));
        samples.add(new Pair<>(Integer.valueOf(2), 0.4));
        samples.add(new Pair<>(Integer.valueOf(3), 0.3));

        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        distribution.sample(-1);
    }
}