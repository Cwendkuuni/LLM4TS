import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    //Test cumulativeProbability method
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.33333333333333337, dist.cumulativeProbability(1), 0.00001);
        assertEquals(0.4000000000000001, dist.cumulativeProbability(2), 0.00001);
        assertEquals(0.6000000000000001, dist.cumulativeProbability(3), 0.00001);
    }

    //Test getDomain method
    @Test
    public void testGetDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        int[] domain = dist.getDomain(10, 5, 2);
        assertEquals(2, domain[0]);
        assertEquals(5, domain[1]);
    }

    //Test getLowerDomain method
    @Test
    public void testGetLowerDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        int lowerDomain = dist.getLowerDomain(10, 5, 2);
        assertEquals(3, lowerDomain);
    }

    //Test getNumberOfSuccesses method
    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        int numberOfSuccesses = dist.getNumberOfSuccesses();
        assertEquals(5, numberOfSuccesses);
    }

    //Test getPopulationSize method
    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        int populationSize = dist.getPopulationSize();
        assertEquals(10, populationSize);
    }

    //Test getSampleSize method
    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        int sampleSize = dist.getSampleSize();
        assertEquals(2, sampleSize);
    }

    //Test probability method
    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.33333333333333326, dist.probability(1), 0.00001);
        assertEquals(0.2666666666666667, dist.probability(2), 0.00001);
        assertEquals(0.2, dist.probability(3), 0.00001);
    }

    //Test upperCumulativeProbability method
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.6, dist.upperCumulativeProbability(1), 0.00001);
        assertEquals(0.4666666666666667, dist.upperCumulativeProbability(2), 0.00001);
        assertEquals(0.33333333333333337, dist.upperCumulativeProbability(3), 0.00001);
    }

    //Test getNumericalMean method
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        double numericalMean = dist.getNumericalMean();
        assertEquals(1.0, numericalMean, 0.00001);
    }

    //Test getNumericalVariance method
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        double numericalVariance = dist.getNumericalVariance();
        assertEquals(0.7142857142857143, numericalVariance, 0.00001);
    }

    //Test getSupportLowerBound method
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        int supportLowerBound = dist.getSupportLowerBound();
        assertEquals(2, supportLowerBound);
    }

    //Test getSupportUpperBound method
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        int supportUpperBound = dist.getSupportUpperBound();
        assertEquals(5, supportUpperBound);
    }

    //Test isSupportConnected method
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        boolean isSupportConnected = dist.isSupportConnected();
        assertTrue(isSupportConnected);
    }
}