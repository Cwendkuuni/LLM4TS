import org.junit.Test;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        // Test probability of each value in the distribution
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.3));
        samples.add(new Pair<>("B", 0.5));
        samples.add(new Pair<>("C", 0.2));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
        
        assertEquals(0.3, distribution.probability("A"), 0);
        assertEquals(0.5, distribution.probability("B"), 0);
        assertEquals(0.2, distribution.probability("C"), 0);
    }

    @Test
    public void testGetSamples() {
        // Test getting the samples from the distribution
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.3));
        samples.add(new Pair<>("B", 0.5));
        samples.add(new Pair<>("C", 0.2));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);

        List<Pair<String, Double>> result = distribution.getSamples();

        assertEquals(samples, result);
    }

    @Test
    public void testSample() {
        // Test generating a random sample from the distribution
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.3));
        samples.add(new Pair<>("B", 0.5));
        samples.add(new Pair<>("C", 0.2));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
        
        String result = distribution.sample();
        
        assertTrue(result.equals("A") || result.equals("B") || result.equals("C"));
    }

    @Test(expected=NotStrictlyPositiveException.class)
    public void testSampleInvalidSize() {
        // Test generating a random sample with an invalid sample size
        List<Pair<String, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.3));
        samples.add(new Pair<>("B", 0.5));
        samples.add(new Pair<>("C", 0.2));
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
        
        distribution.sample(-1);
    }
}