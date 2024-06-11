import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testDefaultConstructor() {
        // Test if default constructor creates a distribution with bounds [0, 1)
        UniformRealDistribution distribution = new UniformRealDistribution();
        assert distribution.getSupportLowerBound() == 0;
        assert distribution.getSupportUpperBound() == 1;
    }

    @Test
    public void testConstructorWithBounds() {
        // Test if constructor with specified bounds creates a distribution with correct bounds
        double lower = -1;
        double upper = 2;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        assert distribution.getSupportLowerBound() == lower;
        assert distribution.getSupportUpperBound() == upper;
    }

    @Test
    public void testDensity() {
        // Test density function for values outside and inside the bounds
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assert distribution.density(-1) == 0;
        assert distribution.density(0.5) > 0;
    }

    @Test
    public void testCumulativeProbability() {
        // Test cumulative probability function for values outside and inside the bounds
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assert distribution.cumulativeProbability(-1) == 0;
        assert distribution.cumulativeProbability(0.5) > 0;
    }

    @Test
    public void testMeanAndVariance() {
        // Test numerical mean and variance calculations
        double lower = 2;
        double upper = 6;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper);
        assert distribution.getNumericalMean() == 0.5 * (lower + upper);
        assert distribution.getNumericalVariance() == (upper - lower) * (upper - lower) / 12;
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidBounds() {
        // Test if exception is thrown when lower bound is greater than or equal to upper bound
        double lower = 2;
        double upper = 2;
        new UniformRealDistribution(lower, upper);
    }

    @Test
    public void testSample() {
        // Test if sampled value falls within the specified bounds
        RandomGenerator rng = new Well19937c();
        double lower = -3;
        double upper = 3;
        UniformRealDistribution distribution = new UniformRealDistribution(rng, lower, upper, 0.1);
        double sample = distribution.sample();
        assert sample >= lower && sample < upper;
    }

}