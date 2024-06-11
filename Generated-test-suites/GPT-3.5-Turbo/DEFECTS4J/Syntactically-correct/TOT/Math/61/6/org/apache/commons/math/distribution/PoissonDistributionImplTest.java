import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.exception.MathRuntimeException;
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructor_PositiveMean() {
        // Arrange
        double mean = 5.0;

        // Act
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);

        // Assert
        Assert.assertEquals(mean, distribution.getMean(), 0.0001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructor_NegativeMean() {
        // Arrange
        double mean = -2.0;

        // Act
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
    }

    @Test
    public void testProbability_ZeroX() {
        // Arrange
        double mean = 3.0;
        int x = 0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);

        // Act
        double probability = distribution.probability(x);

        // Assert
        Assert.assertEquals(Math.exp(-mean), probability, 0.0001);
    }

    @Test
    public void testProbability_NegativeX() {
        // Arrange
        double mean = 3.0;
        int x = -1;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);

        // Act
        double probability = distribution.probability(x);

        // Assert
        Assert.assertEquals(0.0, probability, 0.0001);
    }

    @Test
    public void testProbability_PositiveX() {
        // Arrange
        double mean = 3.0;
        int x = 5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);

        // Act
        double probability = distribution.probability(x);

        // Assert
        Assert.assertEquals(0.100818813444924, probability, 0.0001);
    }

    @Test
    public void testCumulativeProbability_NegativeX() {
        // Arrange
        double mean = 3.0;
        int x = -1;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);

        // Act
        double cumulativeProbability = distribution.cumulativeProbability(x);

        // Assert
        Assert.assertEquals(0.0, cumulativeProbability, 0.0001);
    }

    @Test
    public void testCumulativeProbability_ZeroX() throws MathException {
        // Arrange
        double mean = 3.0;
        int x = 0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);

        // Act
        double cumulativeProbability = distribution.cumulativeProbability(x);

        // Assert
        Assert.assertEquals(0.049787068367863944, cumulativeProbability, 0.0001);
    }

    @Test
    public void testCumulativeProbability_PositiveX() throws MathException {
        // Arrange
        double mean = 3.0;
        int x = 5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);

        // Act
        double cumulativeProbability = distribution.cumulativeProbability(x);

        // Assert
        Assert.assertEquals(0.18473675547622792, cumulativeProbability, 0.0001);
    }

    @Test
    public void testNormalApproximateProbability_ZeroX() throws MathException {
        // Arrange
        double mean = 3.0;
        int x = 0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);

        // Act
        double normalApproximateProbability = distribution.normalApproximateProbability(x);

        // Assert
        Assert.assertEquals(0.18406012534675718, normalApproximateProbability, 0.0001);
    }

    @Test
    public void testNormalApproximateProbability_PositiveX() throws MathException {
        // Arrange
        double mean = 3.0;
        int x = 5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);

        // Act
        double normalApproximateProbability = distribution.normalApproximateProbability(x);

        // Assert
        Assert.assertEquals(0.849730870487139, normalApproximateProbability, 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        // Arrange
        double mean = 3.0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);

        // Act
        int sample = distribution.sample();

        // Assert
        Assert.assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        // Arrange
        double mean = 3.0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        double p = 0.5;

        // Act
        int domainLowerBound = distribution.getDomainLowerBound(p);

        // Assert
        Assert.assertEquals(0, domainLowerBound);
    }

    @Test
    public void testGetDomainUpperBound() {
        // Arrange
        double mean = 3.0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        double p = 0.5;

        // Act
        int domainUpperBound = distribution.getDomainUpperBound(p);

        // Assert
        Assert.assertEquals(Integer.MAX_VALUE, domainUpperBound);
    }
}