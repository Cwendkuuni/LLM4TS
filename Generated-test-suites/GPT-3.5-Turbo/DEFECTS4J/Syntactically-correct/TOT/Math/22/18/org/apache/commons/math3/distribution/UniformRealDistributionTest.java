import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;

    @Before
    public void setup() {
        distribution = new UniformRealDistribution(0, 1);
    }

    @Test
    public void testDensity() {
        double x1 = -0.5;
        double x2 = 0.5;
        double x3 = 1.5;
        
        double density1 = distribution.density(x1);
        double density2 = distribution.density(x2);
        double density3 = distribution.density(x3);

        assertEquals(0.0, density1, 0);
        assertEquals(1.0, density2, 0);
        assertEquals(0.0, density3, 0);
    }

    @Test
    public void testCumulativeProbability() {
        double x1 = -0.5;
        double x2 = 0.5;
        double x3 = 1.5;

        double cumulativeProbability1 = distribution.cumulativeProbability(x1);
        double cumulativeProbability2 = distribution.cumulativeProbability(x2);
        double cumulativeProbability3 = distribution.cumulativeProbability(x3);

        assertEquals(0.0, cumulativeProbability1, 0);
        assertEquals(0.5, cumulativeProbability2, 0);
        assertEquals(1.0, cumulativeProbability3, 0);
    }

    @Test
    public void testGetNumericalMean() {
        double mean = distribution.getNumericalMean();

        assertEquals(0.5, mean, 0);
    }

    @Test
    public void testGetNumericalVariance() {
        double variance = distribution.getNumericalVariance();

        assertEquals(0.08333333333333333, variance, 0);
    }

    @Test
    public void testGetSupportLowerBound() {
        double lowerBound = distribution.getSupportLowerBound();

        assertEquals(0, lowerBound, 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        double upperBound = distribution.getSupportUpperBound();

        assertEquals(1, upperBound, 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        boolean isLowerBoundInclusive = distribution.isSupportLowerBoundInclusive();

        assertEquals(true, isLowerBoundInclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        boolean isUpperBoundInclusive = distribution.isSupportUpperBoundInclusive();

        assertEquals(false, isUpperBoundInclusive);
    }

    @Test
    public void testIsSupportConnected() {
        boolean isSupportConnected = distribution.isSupportConnected();

        assertEquals(true, isSupportConnected);
    }

    @Test
    public void testSample() {
        double sample = distribution.sample();

        assertEquals(true, sample >= 0 && sample < 1);
    }

    @Test
    public void testConstructor_withNumberIsTooLargeException() {
        try {
            distribution = new UniformRealDistribution(1, 0);
        } catch (Exception e) {
            assertEquals("lower >= upper: 1 >= 0", e.getMessage());
        }
    }

}