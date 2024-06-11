import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DiscreteDistributionTest {
    
    private DiscreteDistribution<Integer> discreteDistribution;
    
    @Before
    public void setUp() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        
        try {
            discreteDistribution = new DiscreteDistribution<>(samples);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void probability_ShouldReturnCorrectProbability_ForValidInput() {
        assertEquals(0.2, discreteDistribution.probability(1), 0);
        assertEquals(0.3, discreteDistribution.probability(2), 0);
        assertEquals(0.5, discreteDistribution.probability(3), 0);
    }
    
    @Test
    public void getSamples_ShouldReturnCorrectSamples() {
        List<Pair<Integer, Double>> expectedSamples = new ArrayList<>();
        expectedSamples.add(new Pair<>(1, 0.2));
        expectedSamples.add(new Pair<>(2, 0.3));
        expectedSamples.add(new Pair<>(3, 0.5));
        
        List<Pair<Integer, Double>> actualSamples = discreteDistribution.getSamples();
        
        assertEquals(expectedSamples.size(), actualSamples.size());
        for (int i = 0; i < expectedSamples.size(); i++) {
            Pair<Integer, Double> expectedSample = expectedSamples.get(i);
            Pair<Integer, Double> actualSample = actualSamples.get(i);
            
            assertEquals(expectedSample.getKey(), actualSample.getKey());
            assertEquals(expectedSample.getValue(), actualSample.getValue(), 0);
        }
    }
    
    @Test
    public void sample_ShouldReturnNonNullSample() {
        Integer sample = discreteDistribution.sample();
        
        assertNotNull(sample);
    }
    
    @Test
    public void sample_ShouldReseedRandomGenerator_WhenCalled() {
        long seed = 12345;
        
        discreteDistribution.reseedRandomGenerator(seed);
        
        // Test reseeding by generating two samples with the same seed,
        // they should be different if the random generator was reseeded correctly
        Integer sample1 = discreteDistribution.sample();
        discreteDistribution.reseedRandomGenerator(seed);
        Integer sample2 = discreteDistribution.sample();
        
        assertTrue(!sample1.equals(sample2));
    }
    
    @Test(expected = org.apache.commons.math3.exception.NotStrictlyPositiveException.class)
    public void sample_ShouldThrowNotStrictlyPositiveException_WhenSampleSizeIsZero() {
        discreteDistribution.sample(0);
    }
    
    @Test(expected = org.apache.commons.math3.exception.NotStrictlyPositiveException.class)
    public void sample_ShouldThrowNotStrictlyPositiveException_WhenSampleSizeIsNegative() {
        discreteDistribution.sample(-5);
    }
    
}