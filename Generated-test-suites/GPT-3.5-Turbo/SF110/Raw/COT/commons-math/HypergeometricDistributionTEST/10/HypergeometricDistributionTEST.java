
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(100, 50, 20);
        double result = hypergeometricDistribution.cumulativeProbability(10);
        assertEquals(0.07900191582584705, result, 0.000001);
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(100, 50, 20);
        int result = hypergeometricDistribution.getNumberOfSuccesses();
        assertEquals(50, result);
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(100, 50, 20);
        int result = hypergeometricDistribution.getPopulationSize();
        assertEquals(100, result);
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(100, 50, 20);
        int result = hypergeometricDistribution.getSampleSize();
        assertEquals(20, result);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(100, 50, 20);
        double result = hypergeometricDistribution.probability(10);
        assertEquals(0.07900191582584705, result, 0.000001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(100, 50, 20);
        double result = hypergeometricDistribution.upperCumulativeProbability(10);
        assertEquals(0.9475908642099646, result, 0.000001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(100, 50, 20);
        double result = hypergeometricDistribution.getNumericalMean();
        assertEquals(10.0, result, 0.000001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(100, 50, 20);
        double result = hypergeometricDistribution.getNumericalVariance();
        assertEquals(15.757575757575758, result, 0.000001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(100, 50, 20);
        int result = hypergeometricDistribution.getSupportLowerBound();
        assertEquals(30, result);
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(100, 50, 20);
        int result = hypergeometricDistribution.getSupportUpperBound();
        assertEquals(20, result);
    }
}
