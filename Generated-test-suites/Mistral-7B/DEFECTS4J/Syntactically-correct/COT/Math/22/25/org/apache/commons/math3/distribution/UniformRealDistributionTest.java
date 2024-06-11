import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.0, distribution.getLowerBound(), 0.0);
        assertEquals(1.0, distribution.getUpperBound(), 0.0);
        assertInstanceOf(Well19937c.class, distribution.getRandomGenerator());
    }

    @Test
    public void testUniformRealDistributionWithBoundsConstructor() {
        double lower = 2.5;
        double upper = 5.5;
        UniformRealDistribution distribution;

        try {
            distribution = new UniformRealDistribution(lower, upper);
            fail("Should have thrown NumberIsTooLargeException as lower is larger than upper.");
        } catch (NumberIsTooLargeException e) {
            // Expected exception.
        }

        try {
            distribution = new UniformRealDistribution(lower - 0.001, upper);
            assertEquals(lower - 0.001, distribution.getLowerBound(), 0.0);
            assertEquals(upper, distribution.getUpperBound(), 0.0);
        } catch (NumberIsTooLargeException e) {
            fail("Should not have thrown NumberIsTooLargeException.");
        }

        try {
            distribution = new UniformRealDistribution(lower, upper + 0.001);
            fail("Should have thrown NumberIsTooLargeException as upper is smaller than lower.");
        } catch (NumberIsTooLargeException e) {
            // Expected exception.
        }
    }

    @Test
    public void testUniformRealDistributionWithRandomGeneratorBoundsAndAccuracyConstructor() {
        double lower = 1.0;
        double upper = 3.0;
        double inverseCumulativeAccuracy = 1e-6;
        UniformRealDistribution distribution;

        try {
            distribution = new UniformRealDistribution(new Well19937c(), lower, upper, inverseCumulativeAccuracy);
            assertEquals(lower, distribution.getLowerBound(), 0.0);
            assertEquals(upper, distribution.getUpperBound(), 0.0);
            assertEquals(inverseCumulativeAccuracy, distribution.getSolverAbsoluteAccuracy(), 0.0);
        } catch (NumberIsTooLargeException e) {
            fail("Should not have thrown NumberIsTooLargeException.");
        }

        try {
            distribution = new UniformRealDistribution(new Well19937c(), lower, upper, inverseCumulativeAccuracy * 10);
            assertEquals(lower, distribution.getLowerBound(), 0.0);
            assertEquals(upper, distribution.getUpperBound(), 0.0);
            assertEquals(inverseCumulativeAccuracy * 10, distribution.getSolverAbsoluteAccuracy(), 0.0);
        } catch (NumberIsTooLargeException e) {
            fail("Should not have thrown NumberIsTooLargeException.");
        }

        try {
            distribution = new UniformRealDistribution(new Well19937c(), upper, lower, inverseCumulativeAccuracy);
            fail("Should have thrown NumberIsTooLargeException as lower is larger than upper.");
        } catch (NumberIsTooLargeException e) {
            // Expected exception.
        }
    }

    @Test
    public void testDensityMethod() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);

        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double density = distribution.density(x);
            double expectedDensity = 1.0 / (1.0 - 0.0);
            assertEquals(expectedDensity, density, 1e-6);
        }

        double xMinusLowerBound = distribution.getLowerBound() - 0.01;
        double densityAtLowerBound = distribution.density(xMinusLowerBound);
        double expectedDensityAtLowerBound = 1.0 / (1.0 - (distribution.getLowerBound() - xMinusLowerBound));
        assertEquals(expectedDensityAtLowerBound, densityAtLowerBound, 1e-6);

        double xPlusUpperBound = distribution.getUpperBound() + 0.01;
        double densityAtUpperBound = distribution.density(xPlusUpperBound);
        double expectedDensityAtUpperBound = 1.0 / (xPlusUpperBound - distribution.getLowerBound());
        assertEquals(expectedDensityAtUpperBound, densityAtUpperBound, 1e-6);
    }

    @Test
    public void testCumulativeProbabilityMethod() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);

        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double cumulativeProbability = distribution.cumulativeProbability(x);
            double expectedCumulativeProbability = x;
            assertEquals(expectedCumulativeProbability, cumulativeProbability, 1e-6);
        }

        double xMinusLowerBound = distribution.getLowerBound() - 0.01;
        double cumulativeProbabilityAtLowerBound = distribution.cumulativeProbability(xMinusLowerBound);
        double expectedCumulativeProbabilityAtLowerBound = 0.0;
        assertEquals(expectedCumulativeProbabilityAtLowerBound, cumulativeProbabilityAtLowerBound, 1e-6);

        double xPlusUpperBound = distribution.getUpperBound() + 0.01;
        double cumulativeProbabilityAtUpperBound = distribution.cumulativeProbability(xPlusUpperBound);
        double expectedCumulativeProbabilityAtUpperBound = 1.0;
        assertEquals(expectedCumulativeProbabilityAtUpperBound, cumulativeProbabilityAtUpperBound, 1e-6);
    }

    @Test
    public void testGetSolverAbsoluteAccuracyMethod() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, distribution.getSolverAbsoluteAccuracy(), 0.0);

        UniformRealDistribution distributionWithCustomAccuracy = new UniformRealDistribution(0.0, 1.0, 1e-7);
        assertEquals(1e-7, distributionWithCustomAccuracy.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testGetNumericalMeanMethod() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double numericalMean = distribution.getNumericalMean();
        double expectedNumericalMean = 0.5;
        assertEquals(expectedNumericalMean, numericalMean, 1e-6);
    }

    @Test
    public void testGetNumericalVarianceMethod() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double numericalVariance = distribution.getNumericalVariance();
        double expectedNumericalVariance = 0.0625;
        assertEquals(expectedNumericalVariance, numericalVariance, 1e-6);
    }

    @Test
    public void testGetSupportLowerBoundMethod() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double supportLowerBound = distribution.getSupportLowerBound();
        double expectedSupportLowerBound = 0.0;
        assertEquals(expectedSupportLowerBound, supportLowerBound, 0.0);
    }

    @Test
    public void testGetSupportUpperBoundMethod() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double supportUpperBound = distribution.getSupportUpperBound();
        double expectedSupportUpperBound = 1.0;
        assertEquals(expectedSupportUpperBound, supportUpperBound, 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusiveMethod() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusiveMethod() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnectedMethod() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSampleMethod() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);

        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= distribution.getLowerBound());
            assertTrue(sample < distribution.getUpperBound());
        }
    }
}