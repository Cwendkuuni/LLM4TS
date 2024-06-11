import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, hypergeometricDistribution.cumulativeProbability(0), 0.0001);
        assertEquals(1.0, hypergeometricDistribution.cumulativeProbability(3), 0.0001);
        assertEquals(0.5714, hypergeometricDistribution.cumulativeProbability(2), 0.0001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.3571, hypergeometricDistribution.probability(1), 0.0001);
        assertEquals(0.5714, hypergeometricDistribution.probability(2), 0.0001);
        assertEquals(0.0714, hypergeometricDistribution.probability(0), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.0, hypergeometricDistribution.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.4285, hypergeometricDistribution.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.0714, hypergeometricDistribution.upperCumulativeProbability(2), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, hypergeometricDistribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.6428, hypergeometricDistribution.getNumericalVariance(), 0.0001);
    }

}