import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 2);
        double result = distribution.cumulativeProbability(1);
        Assert.assertEquals(0.36666666666666664, result, 0.0001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 2);
        double result = distribution.probability(1);
        Assert.assertEquals(0.3333333333333333, result, 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 2);
        double result = distribution.upperCumulativeProbability(1);
        Assert.assertEquals(0.6333333333333333, result, 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 2);
        double result = distribution.getNumericalMean();
        Assert.assertEquals(0.8, result, 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 2);
        double result = distribution.getNumericalVariance();
        Assert.assertEquals(0.2666666666666667, result, 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 2);
        int result = distribution.getSupportLowerBound();
        Assert.assertEquals(0, result);
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 2);
        int result = distribution.getSupportUpperBound();
        Assert.assertEquals(2, result);
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 2);
        boolean result = distribution.isSupportConnected();
        Assert.assertTrue(result);
    }
}