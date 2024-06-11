import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<String, Double>("A", 0.2));
        samples.add(new Pair<String, Double>("B", 0.4));
        samples.add(new Pair<String, Double>("C", 0.3));
        samples.add(new Pair<String, Double>("D", 0.1));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(samples);

        assertEquals(0.2, distribution.probability("A"), 0.0001);
        assertEquals(0.4, distribution.probability("B"), 0.0001);
        assertEquals(0.3, distribution.probability("C"), 0.0001);
        assertEquals(0.1, distribution.probability("D"), 0.0001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<String, Double>("A", 0.2));
        samples.add(new Pair<String, Double>("B", 0.4));
        samples.add(new Pair<String, Double>("C", 0.3));
        samples.add(new Pair<String, Double>("D", 0.1));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(samples);

        List<Pair<String, Double>> actualSamples = distribution.getSamples();

        assertEquals(samples, actualSamples);
    }

    @Test
    public void testSample() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<String, Double>("A", 0.2));
        samples.add(new Pair<String, Double>("B", 0.4));
        samples.add(new Pair<String, Double>("C", 0.3));
        samples.add(new Pair<String, Double>("D", 0.1));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(samples);

        String sample = distribution.sample();

        assertTrue("A".equals(sample) || "B".equals(sample) || "C".equals(sample) || "D".equals(sample));
    }

    @Test
    public void testSampleWithSampleSize() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<String, Double>("A", 0.2));
        samples.add(new Pair<String, Double>("B", 0.4));
        samples.add(new Pair<String, Double>("C", 0.3));
        samples.add(new Pair<String, Double>("D", 0.1));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(samples);

        String[] sample = distribution.sample(3);

        assertEquals(3, sample.length);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeProbability() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<String, Double>("A", -0.2));
        samples.add(new Pair<String, Double>("B", 0.4));
        samples.add(new Pair<String, Double>("C", 0.3));
        samples.add(new Pair<String, Double>("D", 0.1));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(samples);
    }

    @Test(expected = MathArithmeticException.class)
    public void testZeroSumProbability() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<String, Double>("A", 0.5));
        samples.add(new Pair<String, Double>("B", 0.5));
        samples.add(new Pair<String, Double>("C", -0.5));
        samples.add(new Pair<String, Double>("D", 0.5));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(samples);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInfiniteProbability() {
        List<Pair<String, Double>> samples = new ArrayList<Pair<String, Double>>();
        samples.add(new Pair<String, Double>("A", 0.5));
        samples.add(new Pair<String, Double>("B", 0.5));
        samples.add(new Pair<String, Double>("C", 0.3));
        samples.add(new Pair<String, Double>("D", Double.POSITIVE_INFINITY));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<String>(samples);
    }
}