import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.Pair;

public class DiscreteDistributionTest {
    
    @Test
    public void testProbability() {
        List<Pair<T, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.5));
        
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
        
        assertEquals(0.2, distribution.probability("A"), 0.0001);
        assertEquals(0.3, distribution.probability("B"), 0.0001);
        assertEquals(0.5, distribution.probability("C"), 0.0001);
    }
    
    @Test
    public void testGetSamples() {
        List<Pair<T, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.5));
        
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
        
        List<Pair<String, Double>> actualSamples = distribution.getSamples();
        
        assertEquals(samples.size(), actualSamples.size());
        
        for (int i = 0; i < samples.size(); i++) {
            assertEquals(samples.get(i).getKey(), actualSamples.get(i).getKey());
            assertEquals(samples.get(i).getValue(), actualSamples.get(i).getValue(), 0.0001);
        }
    }
    
    @Test
    public void testSample() {
        List<Pair<T, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        samples.add(new Pair<>("C", 0.5));
        
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
        
        String sampledValue = distribution.sample();
        
        assertNotNull(sampledValue);
        assertTrue(sampledValue.equals("A") || sampledValue.equals("B") || sampledValue.equals("C"));
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleNegativeSize() {
        List<Pair<T, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
        
        distribution.sample(-1);
    }
    
    @Test
    public void testSampleValidSize() {
        List<Pair<T, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>("A", 0.2));
        samples.add(new Pair<>("B", 0.3));
        
        DiscreteDistribution<String> distribution = new DiscreteDistribution<>(samples);
        
        String[] sampledValues = distribution.sample(5);
        
        assertNotNull(sampledValues);
        assertEquals(5, sampledValues.length);
    }

    // Add more test cases for edge cases and error scenarios as needed
    
}