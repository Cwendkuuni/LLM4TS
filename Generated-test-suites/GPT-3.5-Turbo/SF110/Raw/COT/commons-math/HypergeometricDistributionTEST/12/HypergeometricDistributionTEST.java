import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertNotNull(distribution);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorInvalidPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(0, 5, 3);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorInvalidNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, -1, 3);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorInvalidSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, -1);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorNumberOfSuccessesGreaterThanPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 15, 3);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorSampleSizeGreaterThanPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 15);
    }
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double cumProb = distribution.cumulativeProbability(2);
        assertEquals(0.3, cumProb, 0.001);
    }
    
    @Test
    public void testGetDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        int[] domain = distribution.getDomain(10, 5, 3);
        assertArrayEquals(new int[]{2, 3}, domain);
    }
    
    @Test
    public void testGetLowerDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        int lowerDomain = distribution.getLowerDomain(10, 5, 3);
        assertEquals(2, lowerDomain);
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        int numSuccesses = distribution.getNumberOfSuccesses();
        assertEquals(5, numSuccesses);
    }
    
    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        int populationSize = distribution.getPopulationSize();
        assertEquals(10, populationSize);
    }
    
    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        int sampleSize = distribution.getSampleSize();
        assertEquals(3, sampleSize);
    }
    
    @Test
    public void testGetUpperDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        int upperDomain = distribution.getUpperDomain(5, 3);
        assertEquals(3, upperDomain);
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double prob = distribution.probability(2);
        assertEquals(0.5, prob, 0.001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double upperCumProb = distribution.upperCumulativeProbability(2);
        assertEquals(0.7, upperCumProb, 0.001);
    }
    
    @Test
    public void testInnerCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double innerCumProb = distribution.innerCumulativeProbability(2, 3, 1);
        assertEquals(0.2, innerCumProb, 0.001);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double mean = distribution.getNumericalMean();
        assertEquals(1.5, mean, 0.001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double variance = distribution.getNumericalVariance();
        assertEquals(0.42857142857142855, variance, 0.001);
    }
    
    @Test
    public void testCalculateNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double variance = distribution.calculateNumericalVariance();
        assertEquals(0.42857142857142855, variance, 0.001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        int lowerBound = distribution.getSupportLowerBound();
        assertEquals(0, lowerBound);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        int upperBound = distribution.getSupportUpperBound();
        assertEquals(3, upperBound);
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        boolean isConnected = distribution.isSupportConnected();
        assertTrue(isConnected);
    }

}