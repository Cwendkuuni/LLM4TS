import org.junit.Test;
import org.apache.commons.math3.distribution.*;

public class HypergeometricDistributionTest {
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        // Test values inside the domain
        double cumulativeProbability1 = distribution.cumulativeProbability(2);
        assert (cumulativeProbability1 == 0.5);
        
        // Test values outside the domain
        double cumulativeProbability2 = distribution.cumulativeProbability(8);
        assert (cumulativeProbability2 == 1.0);
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        int numberOfSuccesses = distribution.getNumberOfSuccesses();
        assert (numberOfSuccesses == 5);
    }
    
    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        int populationSize = distribution.getPopulationSize();
        assert (populationSize == 10);
    }
    
    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        int sampleSize = distribution.getSampleSize();
        assert (sampleSize == 3);
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        // Test values inside the domain
        double probability1 = distribution.probability(2);
        assert (probability1 > 0.0 && probability1 < 1.0);
        
        // Test values outside the domain
        double probability2 = distribution.probability(8);
        assert (probability2 == 0.0);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        // Test values inside the domain
        double upperCumulativeProbability1 = distribution.upperCumulativeProbability(2);
        assert (upperCumulativeProbability1 == 0.5);
        
        // Test values outside the domain
        double upperCumulativeProbability2 = distribution.upperCumulativeProbability(8);
        assert (upperCumulativeProbability2 == 0.0);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        double numericalMean = distribution.getNumericalMean();
        assert (numericalMean > 0.0);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        double numericalVariance = distribution.getNumericalVariance();
        assert (numericalVariance > 0.0);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        int supportLowerBound = distribution.getSupportLowerBound();
        assert (supportLowerBound >= 0);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        int supportUpperBound = distribution.getSupportUpperBound();
        assert (supportUpperBound <= 5);
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        
        boolean isSupportConnected = distribution.isSupportConnected();
        assert (isSupportConnected);
    }
}