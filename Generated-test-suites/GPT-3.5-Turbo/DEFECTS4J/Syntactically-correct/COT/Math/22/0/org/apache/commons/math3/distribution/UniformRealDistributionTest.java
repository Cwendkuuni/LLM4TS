import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        // Test1: x is lower bound
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double x1 = 0.0;
        double result1 = distribution.density(x1);
        assert(result1 == 1.0);

        // Test2: x is upper bound
        double x2 = 1.0;
        double result2 = distribution.density(x2);
        assert(result2 == 1.0);

        // Test3: x is not within the range
        double x3 = 2.0;
        double result3 = distribution.density(x3);
        assert(result3 == 0.0);
    }

    @Test
    public void testCumulativeProbability() {
        // Test1: x is lower bound
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double x1 = 0.0;
        double result1 = distribution.cumulativeProbability(x1);
        assert(result1 == 0.0);

        // Test2: x is upper bound
        double x2 = 1.0;
        double result2 = distribution.cumulativeProbability(x2);
        assert(result2 == 1.0);

        // Test3: x is within the range
        double x3 = 0.5;
        double result3 = distribution.cumulativeProbability(x3);
        assert(result3 == 0.5);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double mean = distribution.getNumericalMean();
        assert(mean == 0.5);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double variance = distribution.getNumericalVariance();
        assert(variance == 0.08333333333333333);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double lowerBound = distribution.getSupportLowerBound();
        assert(lowerBound == 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double upperBound = distribution.getSupportUpperBound();
        assert(upperBound == 1.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        boolean result = distribution.isSupportLowerBoundInclusive();
        assert(result == true);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        boolean result = distribution.isSupportUpperBoundInclusive();
        assert(result == false);
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        boolean result = distribution.isSupportConnected();
        assert(result == true);
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        double sample = distribution.sample();
        assert(sample >= 0.0 && sample <= 1.0);
    }
}