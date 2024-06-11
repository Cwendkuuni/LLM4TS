import org.junit.Assert;
import org.junit.Test;

public class FDistributionTest {

    // Test the density method
    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(4, 5);

        // Typical use case
        double density = fDistribution.density(1.5);
        Assert.assertEquals(0.156, density, 0.001);

        // Edge case
        density = fDistribution.density(0);
        Assert.assertEquals(0, density, 0);

        // Error scenario
        try {
            fDistribution.density(-1);
            Assert.fail("Expected NotStrictlyPositiveException was not thrown");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception, test passed
        }
    }

    // Test the cumulativeProbability method
    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(4, 5);

        // Typical use case
        double probability = fDistribution.cumulativeProbability(1.5);
        Assert.assertEquals(0.780, probability, 0.001);

        // Edge case
        probability = fDistribution.cumulativeProbability(0);
        Assert.assertEquals(0, probability, 0);

        // Error scenario
        try {
            fDistribution.cumulativeProbability(-1);
            Assert.fail("Expected NotStrictlyPositiveException was not thrown");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception, test passed
        }
    }

    // Test the getNumericalMean method
    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(4, 5);

        // Typical use case
        double mean = fDistribution.getNumericalMean();
        Assert.assertEquals(1.25, mean, 0);

        // Edge case
        fDistribution = new FDistribution(2, 5);
        mean = fDistribution.getNumericalMean();
        Assert.assertEquals(Double.NaN, mean, 0);

        fDistribution = new FDistribution(4, 2);
        mean = fDistribution.getNumericalMean();
        Assert.assertEquals(2, mean, 0);
    }

    // Test the getNumericalVariance method
    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(4, 5);

        // Typical use case
        double variance = fDistribution.getNumericalVariance();
        Assert.assertEquals(3.75, variance, 0);

        // Edge case
        fDistribution = new FDistribution(2, 5);
        variance = fDistribution.getNumericalVariance();
        Assert.assertEquals(Double.NaN, variance, 0);

        fDistribution = new FDistribution(4, 2);
        variance = fDistribution.getNumericalVariance();
        Assert.assertEquals(Double.NaN, variance, 0);
    }

    // Test the getSupportLowerBound method
    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(4, 5);

        // Typical use case
        double lowerBound = fDistribution.getSupportLowerBound();
        Assert.assertEquals(0, lowerBound, 0);
    }

    // Test the getSupportUpperBound method
    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(4, 5);

        // Typical use case
        double upperBound = fDistribution.getSupportUpperBound();
        Assert.assertEquals(Double.POSITIVE_INFINITY, upperBound, 0);
    }

    // Test the isSupportLowerBoundInclusive method
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(4, 5);

        // Typical use case
        boolean inclusive = fDistribution.isSupportLowerBoundInclusive();
        Assert.assertTrue(inclusive);
    }

    // Test the isSupportUpperBoundInclusive method
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(4, 5);

        // Typical use case
        boolean inclusive = fDistribution.isSupportUpperBoundInclusive();
        Assert.assertFalse(inclusive);
    }

    // Test the isSupportConnected method
    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(4, 5);

        // Typical use case
        boolean connected = fDistribution.isSupportConnected();
        Assert.assertTrue(connected);
    }

}