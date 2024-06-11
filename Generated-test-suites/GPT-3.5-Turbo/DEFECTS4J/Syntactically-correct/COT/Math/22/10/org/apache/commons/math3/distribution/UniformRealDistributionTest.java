import org.junit.Test;
import org.apache.commons.math3.distribution.UniformRealDistribution;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double densityResult = dist.density(0.5);
        double expectedDensity = 1.0;
        assertTrue(Math.abs(densityResult - expectedDensity) <= 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double cumulativeProb = dist.cumulativeProbability(0.5);
        double expectedProb = 0.5;
        assertTrue(Math.abs(cumulativeProb - expectedProb) <= 1e-9);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double mean = dist.getNumericalMean();
        double expectedMean = 0.5;
        assertTrue(Math.abs(mean - expectedMean) <= 1e-9);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double variance = dist.getNumericalVariance();
        double expectedVariance = 1.0/12.0;
        assertTrue(Math.abs(variance - expectedVariance) <= 1e-9);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double lowerBound = dist.getSupportLowerBound();
        double expectedLowerBound = 0.0;
        assertTrue(Math.abs(lowerBound - expectedLowerBound) <= 1e-9);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double upperBound = dist.getSupportUpperBound();
        double expectedUpperBound = 1.0;
        assertTrue(Math.abs(upperBound - expectedUpperBound) <= 1e-9);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        boolean isLowerBoundInclusive = dist.isSupportLowerBoundInclusive();
        assertTrue(isLowerBoundInclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        boolean isUpperBoundInclusive = dist.isSupportUpperBoundInclusive();
        assertFalse(isUpperBoundInclusive);
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        boolean isSupportConnected = dist.isSupportConnected();
        assertTrue(isSupportConnected);
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double sampleResult = dist.sample();
        assertTrue(sampleResult >= 0.0 && sampleResult < 1.0);
    }

}