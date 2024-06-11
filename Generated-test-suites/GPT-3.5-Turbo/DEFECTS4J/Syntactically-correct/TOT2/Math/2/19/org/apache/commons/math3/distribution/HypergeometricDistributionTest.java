import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HypergeometricDistributionTest {
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(0.15, distribution.cumulativeProbability(2), 0.001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(15, 5, 8);
        assertEquals(0.63, distribution.upperCumulativeProbability(3), 0.001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(20, 4, 7);
        assertEquals(0.275, distribution.probability(2), 0.001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(25, 6, 9);
        assertEquals(2.16, distribution.getNumericalMean(), 0.01);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(30, 7, 10);
        assertEquals(1.59, distribution.getNumericalVariance(), 0.01);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(35, 8, 12);
        assertEquals(1, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(40, 10, 15);
        assertEquals(10, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(45, 11, 18);
        assertEquals(true, distribution.isSupportConnected());
    }
}