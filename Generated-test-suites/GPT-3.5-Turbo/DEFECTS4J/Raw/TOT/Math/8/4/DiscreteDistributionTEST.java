
import org.apache.commons.math3.distribution.DiscreteDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DiscreteDistributionTest {
    
    private DiscreteDistribution<Integer> distribution;
    
    @Before
    public void setup() throws NotPositiveException, MathArithmeticException, MathIllegalArgumentException {
        List<Pair<Integer, Double>> samples = new ArrayList<>();
        samples.add(new Pair<>(1, 0.2));
        samples.add(new Pair<>(2, 0.3));
        samples.add(new Pair<>(3, 0.5));
        
        distribution = new DiscreteDistribution<>(samples);
    }
    
    @Test
    public void testProbability() {
        assertEquals(0.2, distribution.probability(1), 0.0001);
        assertEquals(0.3, distribution.probability(2), 0.0001);
        assertEquals(0.5, distribution.probability(3), 0.0001);
        assertEquals(0.0, distribution.probability(0), 0.0001);
        assertEquals(0.0, distribution.probability(4), 0.0001);
    }
    
    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = distribution.getSamples();
        
        assertEquals(new Pair<>(1, 0.2), samples.get(0));
        assertEquals(new Pair<>(2, 0.3), samples.get(1));
        assertEquals(new Pair<>(3, 0.5), samples.get(2));
    }
    
    @Test
    public void testSample() {
        RandomGenerator rng = new Well19937c();
        rng.setSeed(12345);
        
        DiscreteDistribution<Integer> distributionWithSeed = new DiscreteDistribution<>(rng, distribution.getSamples());
        
        int sample = distributionWithSeed.sample();
        
        assertTrue(sample >= 1 && sample <= 3);
    }
    
    @Test
    public void testSampleWithSize() {
        int sampleSize = 10;
        Integer[] samples = distribution.sample(sampleSize);
        
        assertEquals(sampleSize, samples.length);
        
        for (int i = 0; i < samples.length; i++) {
            assertTrue(samples[i] >= 1 && samples[i] <= 3);
        }
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithInvalidSize() throws NotStrictlyPositiveException {
        distribution.sample(0);
    }
    
    @Test
    public void testReseedRandomGenerator() {
        DiscreteDistribution<Integer> newDistribution = new DiscreteDistribution<>(distribution.getSamples());
        
        newDistribution.reseedRandomGenerator(12345);
        
        assertNotEquals(distribution.sample(), newDistribution.sample());
    }    
}
