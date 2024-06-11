
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Random;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);

        Assert.assertEquals(5, hypergeometricDistribution.getNumberOfSuccesses());
        Assert.assertEquals(10, hypergeometricDistribution.getPopulationSize());
        Assert.assertEquals(2, hypergeometricDistribution.getSampleSize());
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructor_ThrowsNotPositiveException() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(10, -5, 2);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_ThrowsNotStrictlyPositiveException() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(0, 5, 2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_ThrowsNumberIsTooLargeException() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(10, 20, 2);
    }

    @Test
    public void testCumulativeProbability() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
        double cumulativeProbability = hypergeometricDistribution.cumulativeProbability(3);
        Assert.assertEquals(0.19047619047619047, cumulativeProbability, 0.00000000001);
    }

    @Test
    public void testCumulativeProbability_LowerEdgeCase() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
        double cumulativeProbability = hypergeometricDistribution.cumulativeProbability(0);
        Assert.assertEquals(0.0, cumulativeProbability, 0.00000000001);
    }

    @Test
    public void testCumulativeProbability_UpperEdgeCase() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
        double cumulativeProbability = hypergeometricDistribution.cumulativeProbability(10);
        Assert.assertEquals(1.0, cumulativeProbability, 0.00000000001);
    }

    @Test
    public void testProbability() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
        double probability = hypergeometricDistribution.probability(3);
        Assert.assertEquals(0.22727272727272727, probability, 0.00000000001);
    }
    
    @Test
    public void testProbability_OutsideDomain() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
        double probability = hypergeometricDistribution.probability(12);
        Assert.assertEquals(0.0, probability, 0.00000000001);
    }

    @Test
    public void testUpperCumulativeProbability() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
        double upperCumulativeProbability = hypergeometricDistribution.upperCumulativeProbability(3);
        Assert.assertEquals(0.8095238095238095, upperCumulativeProbability, 0.00000000001);
    }

    @Test
    public void testUpperCumulativeProbability_LowerEdgeCase() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
        double upperCumulativeProbability = hypergeometricDistribution.upperCumulativeProbability(0);
        Assert.assertEquals(1.0, upperCumulativeProbability, 0.00000000001);
    }

    @Test
    public void testUpperCumulativeProbability_UpperEdgeCase() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
        double upperCumulativeProbability = hypergeometricDistribution.upperCumulativeProbability(10);
        Assert.assertEquals(0.0, upperCumulativeProbability, 0.00000000001);
    }
}
