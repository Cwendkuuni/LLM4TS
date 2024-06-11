import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(0.05, distribution.cumulativeProbability(0), 0.0001); // minimum value
        assertEquals(0.525, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.875, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.975, distribution.cumulativeProbability(3), 0.0001); // maximum value
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(0.0, distribution.probability(-1), 0.0001); // below lower bound
        assertEquals(0.05, distribution.probability(0), 0.0001);
        assertEquals(0.35, distribution.probability(1), 0.0001);
        assertEquals(0.55, distribution.probability(2), 0.0001);
        assertEquals(0.05, distribution.probability(3), 0.0001); // above upper bound
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(0.95, distribution.upperCumulativeProbability(0), 0.0001); // minimum value
        assertEquals(0.475, distribution.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.125, distribution.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.025, distribution.upperCumulativeProbability(3), 0.0001); // maximum value
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(1.5, distribution.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(0.375, distribution.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(3, distribution.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        assertEquals(3, distribution.getSupportUpperBound());
    }
}