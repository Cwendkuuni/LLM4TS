import org.junit.Test;
import org.apache.commons.math3.distribution.HypergeometricDistribution;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double result = distribution.cumulativeProbability(2);
        assert(result >= 0 && result <= 1);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double result = distribution.probability(2);
        assert(result >= 0 && result <= 1);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double result = distribution.upperCumulativeProbability(2);
        assert(result >= 0 && result <= 1);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double result = distribution.getNumericalMean();
        assert(result > 0);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double result = distribution.getNumericalVariance();
        assert(result > 0);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        int result = distribution.getSupportLowerBound();
        assert(result >= 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        int result = distribution.getSupportUpperBound();
        assert(result >= 0);
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        boolean result = distribution.isSupportConnected();
        assert(result);
    }
}