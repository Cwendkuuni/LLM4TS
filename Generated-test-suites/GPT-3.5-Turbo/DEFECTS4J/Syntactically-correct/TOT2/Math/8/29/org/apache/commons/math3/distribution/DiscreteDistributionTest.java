import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.random.*;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.5));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.2));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        
        assertEquals(0.5, distribution.probability(1), 0.001);
        assertEquals(0.3, distribution.probability(2), 0.001);
        assertEquals(0.2, distribution.probability(3), 0.001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.5));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.2));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        
        List<Pair<Integer, Double>> retrievedSamples = distribution.getSamples();
        
        assertEquals(samples, retrievedSamples);
    }

    @Test
    public void testSample() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.5));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.2));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        
        Integer sample = distribution.sample();
        
        assertTrue(sample == 1 || sample == 2 || sample == 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleException() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.5));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.2));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);
        
        Integer[] sampleArray = distribution.sample(0);
    }

    // Additional test cases for edge cases and error scenarios can be added here

}