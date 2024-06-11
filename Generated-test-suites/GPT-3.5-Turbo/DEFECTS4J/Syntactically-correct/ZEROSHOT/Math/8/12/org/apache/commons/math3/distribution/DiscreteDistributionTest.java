import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.random.Well19937c;

public class DiscreteDistributionTest {
    
    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.4));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
        
        double prob1 = dist.probability(1);
        assertEquals(0.2, prob1, 0.0001);
        
        double prob2 = dist.probability(2);
        assertEquals(0.4, prob2, 0.0001);
        
        double prob3 = dist.probability(3);
        assertEquals(0.4, prob3, 0.0001);
        
        double prob4 = dist.probability(4);
        assertEquals(0.0, prob4, 0.0001);
    }
    
    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.4));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
        
        List<Pair<Integer, Double>> sampled = dist.getSamples();
        
        assertEquals(samples, sampled);
    }
    
    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.4));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
        
        int sample1 = dist.sample();
        assertTrue(sample1 >= 1 && sample1 <= 3);
        
        int sample2 = dist.sample();
        assertTrue(sample2 >= 1 && sample2 <= 3);
        
        int sample3 = dist.sample();
        assertTrue(sample3 >= 1 && sample3 <= 3);
    }
    
    @Test
    public void testSampleInvalidSize() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.4));
        samples.add(new Pair<>(3, 0.4));
        DiscreteDistribution<Integer> dist = new DiscreteDistribution<>(samples);
        
        try {
            dist.sample(0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }
}