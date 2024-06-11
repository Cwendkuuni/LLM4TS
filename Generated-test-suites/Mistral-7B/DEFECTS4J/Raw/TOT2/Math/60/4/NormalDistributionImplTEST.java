import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeError;
import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_meanZero_standardDeviationOne() {
        NormalDistribution distribution = new NormalDistributionImpl();

        // Test mean
        Assert.assertEquals("Mean is not zero", 0.0, distribution.getMean(), 1e-8);

        // Test standard deviation
        Assert.assertEquals("Standard deviation is not one", 1.0, distribution.getStandardDeviation(), 1e-8);

        // Test density
        double x = 0.0;
        double density = distribution.density(x);
        Assert.assertEquals("Density is not correct", 0.39894228040143272, density, 1e-6);

        // Test cumulative probability
        double cumulativeProbability = distribution.cumulativeProbability(x);
        Assert.assertEquals("Cumulative probability is not correct", 0.5, cumulativeProbability, 1e-6);

        // Test inverse cumulative probability
        double inverseCumulativeProbability = distribution.inverseCumulativeProbability(0.5);
        Assert.assertEquals("Inverse cumulative probability is not correct", 0.0, inverseCumulativeProbability, 1e-6);

        // Test sample
        double sample = distribution.sample();
        Assert.assertEquals("Sample is not fromNormalDistribution", 0.0, sample - distribution.getMean(), 1e-6);
        Assert.assertEquals("Sample standard deviation is not correct", 1.0, Math.sqrt(Math.pow(sample - distribution.getMean(), 2) + Math.pow(distribution.getStandardDeviation(), 2)), 1e-6);
    }

    @Test
    public void testNormalDistributionImpl_meanTwo_standardDeviationThree() {
        NormalDistribution distribution = new NormalDistributionImpl(2.0, 3.0);

        // Test mean
        Assert.assertEquals("Mean is not two", 2.0, distribution.getMean(), 1e-8);

        // Test standard deviation
        Assert.assertEquals("Standard deviation is not three", 3.0, distribution.getStandardDeviation(), 1e-8);

        // Test density
        double x = 2.5;
        double density = distribution.density(x);
        Assert.assertEquals("Density is not correct", 0.1359570263304333, density, 1e-6);

        // Test cumulative probability
        double cumulativeProbability = distribution.cumulativeProbability(x);
        Assert.assertEquals("Cumulative probability is not correct", 0.9876513987726275, cumulativeProbability, 1e-6);

        // Test inverse cumulative probability
        double inverseCumulativeProbability = distribution.inverseCumulativeProbability(0.95);
        Assert.assertEquals("Inverse cumulative probability is not correct", 2.241429312353981, inverseCumulativeProbability, 1e-6);

        // Test sample
        double sample = distribution.sample();
        Assert.assertEquals("Sample is not fromNormalDistribution", 2.0, sample - distribution.getMean(), 1e-6);
        Assert.assertEquals("Sample standard deviation is not correct", 3.0, Math.sqrt(Math.pow(sample - distribution.getMean(), 2) + Math.pow(distribution.getStandardDeviation(), 2)), 1e-6);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNormalDistributionImpl_negativeStandardDeviation() {
        new NormalDistributionImpl(-1.0, 2.0);
    }

    @Test(expected = MathRuntimeError.class)
    public void testNormalDistributionImpl_maxIterationsExceededException() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, Double.MAX_VALUE);
        distribution.cumulativeProbability(Double.MAX_VALUE);
    }
}