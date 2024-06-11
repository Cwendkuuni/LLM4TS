import org.junit.Test;
import org.junit.Assert;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testNegativePopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(-1, 5, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(0, 5, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, -2, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, -1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberOfSuccessesGreaterThanPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 15, 3);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleSizeGreaterThanPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 15);
    }

    @Test
    public void testCumulativeProbabilityOutOfRange() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        Assert.assertEquals(1.0, distribution.cumulativeProbability(20), 0.0001);
    }
    
    @Test
    public void testProbabilityOutOfRange() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.0, distribution.probability(-1), 0.0001);
        Assert.assertEquals(0.0, distribution.probability(20), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbabilityOutOfRange() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(1.0, distribution.upperCumulativeProbability(-1), 0.0001);
        Assert.assertEquals(0.0, distribution.upperCumulativeProbability(20), 0.0001);
    }
    
    // Add more test cases for other methods and edge cases...

}

