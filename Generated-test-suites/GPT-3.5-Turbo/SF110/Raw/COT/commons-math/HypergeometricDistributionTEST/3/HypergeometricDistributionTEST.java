import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.junit.*;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
            Assert.assertNotNull(distribution);
            Assert.assertEquals(10, distribution.getPopulationSize());
            Assert.assertEquals(5, distribution.getNumberOfSuccesses());
            Assert.assertEquals(2, distribution.getSampleSize());
        } catch (Exception e) {
            Assert.fail("Constructor should not throw an exception");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidPopulationSize() throws Exception {
        new HypergeometricDistribution(-1, 5, 2);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidNumberOfSuccesses() throws Exception {
        new HypergeometricDistribution(10, -1, 2);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidSampleSize() throws Exception {
        new HypergeometricDistribution(10, 5, -1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() throws Exception {
        new HypergeometricDistribution(10, 15, 2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeGreaterThanPopulationSize() throws Exception {
        new HypergeometricDistribution(10, 5, 12);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        Assert.assertEquals(0.2727272727272727, distribution.probability(0), 0.0001);
        Assert.assertEquals(0.45454545454545453, distribution.probability(1), 0.0001);
        Assert.assertEquals(0.2727272727272727, distribution.probability(2), 0.0001);
        Assert.assertEquals(0, distribution.probability(3), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        Assert.assertEquals(0.2727272727272727, distribution.cumulativeProbability(0), 0.0001);
        Assert.assertEquals(0.7272727272727272, distribution.cumulativeProbability(1), 0.0001);
        Assert.assertEquals(1, distribution.cumulativeProbability(2), 0.0001);
        Assert.assertEquals(1, distribution.cumulativeProbability(3), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        Assert.assertEquals(1, distribution.upperCumulativeProbability(0), 0.0001);
        Assert.assertEquals(0.7272727272727272, distribution.upperCumulativeProbability(1), 0.0001);
        Assert.assertEquals(0.2727272727272727, distribution.upperCumulativeProbability(2), 0.0001);
        Assert.assertEquals(0, distribution.upperCumulativeProbability(3), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        Assert.assertEquals(1, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        Assert.assertEquals(0.2222222222222222, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        Assert.assertEquals(3, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        Assert.assertEquals(5, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        Assert.assertTrue(distribution.isSupportConnected());
    }
    
    // Additional test cases for edge cases and exception handling scenarios can be added here

}