import org.apache.commons.math.MathException;
import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        // Arrange
        double mean = 2.5;
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(mean, 1.0);

        // Act
        double result = normalDistribution.getMean();

        // Assert
        Assert.assertEquals(mean, result, 0);
    }

    @Test
    public void testSetMean() {
        // Arrange
        double mean = 2.5;
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();

        // Act
        normalDistribution.setMean(mean);
        double result = normalDistribution.getMean();

        // Assert
        Assert.assertEquals(mean, result, 0);
    }

    @Test
    public void testGetStandardDeviation() {
        // Arrange
        double standardDeviation = 1.5;
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, standardDeviation);

        // Act
        double result = normalDistribution.getStandardDeviation();

        // Assert
        Assert.assertEquals(standardDeviation, result, 0);
    }

    @Test
    public void testSetStandardDeviation() {
        // Arrange
        double standardDeviation = 1.5;
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();

        // Act
        normalDistribution.setStandardDeviation(standardDeviation);
        double result = normalDistribution.getStandardDeviation();

        // Assert
        Assert.assertEquals(standardDeviation, result, 0);
    }

    @Test
    public void testCumulativeProbability() {
        // Arrange
        double mean = 0;
        double standardDeviation = 1;
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(mean, standardDeviation);
        double x = 1.5;
        
        // Act
        double result = normalDistribution.cumulativeProbability(x);

        // Assert
        Assert.assertEquals(0.9331927987311419, result, 0.00001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        // Arrange
        double mean = 0;
        double standardDeviation = 1;
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(mean, standardDeviation);
        double p = 0.8;

        // Act
        double result = normalDistribution.inverseCumulativeProbability(p);

        // Assert
        Assert.assertEquals(0.8416212335729143, result, 0.00001);
    }

    @Test
    public void testInverseCumulativeProbabilityWithPZero() {
        // Arrange
        double mean = 0;
        double standardDeviation = 1;
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(mean, standardDeviation);
        double p = 0;

        // Act
        double result = normalDistribution.inverseCumulativeProbability(p);

        // Assert
        Assert.assertEquals(Double.NEGATIVE_INFINITY, result, 0);
    }

    @Test
    public void testInverseCumulativeProbabilityWithPOne() {
        // Arrange
        double mean = 0;
        double standardDeviation = 1;
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(mean, standardDeviation);
        double p = 1;

        // Act
        double result = normalDistribution.inverseCumulativeProbability(p);

        // Assert
        Assert.assertEquals(Double.POSITIVE_INFINITY, result, 0);
    }

    @Test
    public void testGetDomainLowerBoundWithPLessThanHalf() {
        // Arrange
        double mean = 0;
        double standardDeviation = 1;
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(mean, standardDeviation);
        double p = 0.3;

        // Act
        double result = normalDistribution.getDomainLowerBound(p);

        // Assert
        Assert.assertEquals(-Double.MAX_VALUE, result, 0);
    }

    @Test
    public void testGetDomainLowerBoundWithPGreaterThanHalf() {
        // Arrange
        double mean = 0;
        double standardDeviation = 1;
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(mean, standardDeviation);
        double p = 0.7;

        // Act
        double result = normalDistribution.getDomainLowerBound(p);

        // Assert
        Assert.assertEquals(mean, result, 0);
    }

    @Test
    public void testGetDomainUpperBoundWithPLessThanHalf() {
        // Arrange
        double mean = 0;
        double standardDeviation = 1;
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(mean, standardDeviation);
        double p = 0.3;

        // Act
        double result = normalDistribution.getDomainUpperBound(p);

        // Assert
        Assert.assertEquals(mean, result, 0);
    }

    @Test
    public void testGetDomainUpperBoundWithPGreaterThanHalf() {
        // Arrange
        double mean = 0;
        double standardDeviation = 1;
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(mean, standardDeviation);
        double p = 0.7;

        // Act
        double result = normalDistribution.getDomainUpperBound(p);

        // Assert
        Assert.assertEquals(Double.MAX_VALUE, result, 0);
    }

    @Test
    public void testGetInitialDomainWithPLessThanHalf() {
        // Arrange
        double mean = 0;
        double standardDeviation = 1;
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(mean, standardDeviation);
        double p = 0.3;

        // Act
        double result = normalDistribution.getInitialDomain(p);

        // Assert
        Assert.assertEquals(mean - standardDeviation, result, 0);
    }

    @Test
    public void testGetInitialDomainWithPGreaterThanHalf() {
        // Arrange
        double mean = 0;
        double standardDeviation = 1;
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(mean, standardDeviation);
        double p = 0.7;

        // Act
        double result = normalDistribution.getInitialDomain(p);

        // Assert
        Assert.assertEquals(mean + standardDeviation, result, 0);
    }
}