
import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {
    
    @Test
    public void testConstructor() {
        RandomGenerator rng = new Well19937c();
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 20;
        
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
        
        assertEquals(populationSize, hypergeometric.getPopulationSize());
        assertEquals(numberOfSuccesses, hypergeometric.getNumberOfSuccesses());
        assertEquals(sampleSize, hypergeometric.getSampleSize());
    }
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(100, 50, 20);
        
        assertEquals(0.0, hypergeometric.cumulativeProbability(-1), 0.0001);
        assertEquals(1.0, hypergeometric.cumulativeProbability(100), 0.0001);
        assertEquals(0.0029, hypergeometric.cumulativeProbability(18), 0.0001);
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(100, 50, 20);
        
        assertEquals(0.0, hypergeometric.probability(-1), 0.0001);
        assertEquals(0.0, hypergeometric.probability(21), 0.0001);
        assertEquals(0.0029, hypergeometric.probability(18), 0.0001);
    }
    
    @Test
    public void testNumericalMean() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(100, 50, 20);
        
        assertEquals(10.0, hypergeometric.getNumericalMean(), 0.0001);
    }
    
    // Add more tests for other methods as needed
    
}
