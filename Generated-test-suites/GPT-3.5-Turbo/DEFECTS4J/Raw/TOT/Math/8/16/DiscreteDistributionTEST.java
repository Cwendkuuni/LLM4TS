import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;
    
    @Before
    public void setUp() {
        try {
            distribution = new DiscreteDistribution<Integer>(new Well19937c(), generateSampleData());
        } catch (NotPositiveException e) {
            fail("Error creating the distribution: " + e.getMessage());
        } catch (MathArithmeticException e) {
            fail("Error creating the distribution: " + e.getMessage());
        } catch (MathIllegalArgumentException e) {
            fail("Error creating the distribution: " + e.getMessage());
        }
    }
    
    @Test
    public void testProbability() {
        double prob1 = distribution.probability(1);
        assertEquals(0.1, prob1, 0);
        
        double prob2 = distribution.probability(3);
        assertEquals(0.3, prob2, 0);
        
        double prob3 = distribution.probability(5);
        assertEquals(0.5, prob3, 0);
        
        double prob4 = distribution.probability(7);
        assertEquals(0.0, prob4, 0);
    }
    
    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = distribution.getSamples();
        
        assertEquals(4, samples.size());
        
        Pair<Integer, Double> sample1 = samples.get(0);
        assertEquals((Integer)1, sample1.getKey());
        assertEquals(0.1, sample1.getValue(), 0);
        
        Pair<Integer, Double> sample2 = samples.get(1);
        assertEquals((Integer)3, sample2.getKey());
        assertEquals(0.3, sample2.getValue(), 0);
        
        Pair<Integer, Double> sample3 = samples.get(2);
        assertEquals((Integer)5, sample3.getKey());
        assertEquals(0.5, sample3.getValue(), 0);
        
        Pair<Integer, Double> sample4 = samples.get(3);
        assertEquals((Integer)7, sample4.getKey());
        assertEquals(0.0, sample4.getValue(), 0);
    }
    
    @Test
    public void testSample() {
        Integer sample1 = distribution.sample();
        assertTrue(sample1 >= 1 && sample1 <= 5);
        
        Integer sample2 = distribution.sample();
        assertTrue(sample2 >= 1 && sample2 <= 5);
        
        Integer sample3 = distribution.sample();
        assertTrue(sample3 >= 1 && sample3 <= 5);
    }
    
    @Test
    public void testSampleWithSize() {
        int sampleSize = 5;
        Integer[] samples = distribution.sample(sampleSize);
        assertEquals(sampleSize, samples.length);
        
        for (Integer sample : samples) {
            assertTrue(sample >= 1 && sample <= 5);
        }
    }
    
    // Helper method to generate sample data for the DiscreteDistribution
    private List<Pair<Integer, Double>> generateSampleData() {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<Integer, Double>(1, 0.1));
        samples.add(new Pair<Integer, Double>(3, 0.3));
        samples.add(new Pair<Integer, Double>(5, 0.5));
        samples.add(new Pair<Integer, Double>(7, 0.0));
        return samples;
    }
    
}