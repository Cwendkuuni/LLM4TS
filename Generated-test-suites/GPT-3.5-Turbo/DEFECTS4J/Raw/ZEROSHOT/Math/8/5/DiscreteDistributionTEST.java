import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.random.Well19937c;

public class DiscreteDistributionTest {
    
    private DiscreteDistribution<Integer> distribution;
    private List<Pair<Integer, Double>> samples;
    
    @Before
    public void setup() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        samples = new ArrayList<Pair<Integer, Double>>();
        
        samples.add(new Pair<Integer, Double>(1, 0.2));
        samples.add(new Pair<Integer, Double>(2, 0.3));
        samples.add(new Pair<Integer, Double>(3, 0.5));
        
        distribution = new DiscreteDistribution<Integer>(samples);
    }
    
    @Test
    public void testProbability() {
        assertEquals(0.2, distribution.probability(1), 0.0001);
        assertEquals(0.3, distribution.probability(2), 0.0001);
        assertEquals(0.5, distribution.probability(3), 0.0001);
    }
    
    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> actualSamples = distribution.getSamples();
        
        assertEquals(samples.size(), actualSamples.size());
        for(int i=0; i<samples.size(); i++) {
            Pair<Integer, Double> expected = samples.get(i);
            Pair<Integer, Double> actual = actualSamples.get(i);

            assertEquals(expected.getKey(), actual.getKey());
            assertEquals(expected.getValue(), actual.getValue(), 0.0001);
        }
    }
    
    @Test
    public void testSample() {
        Integer sample = distribution.sample();
        
        assertNotNull(sample);
        assertTrue(samples.stream().anyMatch(pair -> pair.getKey().equals(sample)));
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithInvalidSampleSize() throws NotStrictlyPositiveException {
        distribution.sample(0);
    }
    
    @Test
    public void testSampleWithValidSampleSize() throws NotStrictlyPositiveException {
        Integer[] sample = distribution.sample(5);
        
        assertEquals(5, sample.length);
        for(int i=0; i<sample.length; i++) {
            assertNotNull(sample[i]);
            assertTrue(samples.stream().anyMatch(pair -> pair.getKey().equals(sample[i])));
        }
    }
    
    @Test
    public void testReseedRandomGenerator() {
        Well19937c rng = new Well19937c();
        distribution.reseedRandomGenerator(123456L);
        assertNotEquals(rng.nextLong(), distribution.random.nextLong());
    }
}