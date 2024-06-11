import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(20, 5, 10);
        
        assertEquals(0.0, dist.cumulativeProbability(0), 0.001);
        assertEquals(0.439, dist.cumulativeProbability(3), 0.001);
        assertEquals(1.0, dist.cumulativeProbability(5), 0.001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(20, 5, 10);
        
        assertEquals(0.0, dist.probability(-1), 0.001);
        assertEquals(0.076, dist.probability(3), 0.001);
        assertEquals(0.0, dist.probability(6), 0.001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(20, 5, 10);
        
        assertEquals(1.0, dist.upperCumulativeProbability(0), 0.001);
        assertEquals(0.561, dist.upperCumulativeProbability(3), 0.001);
        assertEquals(0.0, dist.upperCumulativeProbability(6), 0.001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(20, 5, 10);
        
        assertEquals(2.5, dist.getNumericalMean(), 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(20, 5, 10);
        
        assertEquals(0.986, dist.getNumericalVariance(), 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(20, 5, 10);
        
        assertEquals(0, dist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(20, 5, 10);
        
        assertEquals(5, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(20, 5, 10);
        
        assertEquals(true, dist.isSupportConnected());
    }
}