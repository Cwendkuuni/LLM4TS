import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testFDistributionConstructor() throws NotStrictlyPositiveException {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        assertEquals(3.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        double density = distribution.density(1.5);
        assertTrue(density > 0.0);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        double cumulativeProbability = distribution.cumulativeProbability(1.5);
        assertTrue(cumulativeProbability > 0.0 && cumulativeProbability < 1.0);
    }

    @Test
    public void testFDistributionMean() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        double mean = distribution.getNumericalMean();
        assertTrue(mean > 0.0);
    }

    @Test
    public void testFDistributionVariance() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        double variance = distribution.getNumericalVariance();
        assertTrue(variance > 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(-3.0, 4.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(0.0, 4.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDenominatorDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(3.0, -4.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroDenominatorDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(3.0, 0.0);
    }

    @Test
    public void testFDistributionSupport() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        double lowerBound = distribution.getSupportLowerBound();
        assertEquals(0.0, lowerBound, 0.0);
        double upperBound = distribution.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, upperBound, 0.0);
    }

    @Test
    public void testFDistributionSupportInclusive() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        boolean lowerBoundInclusive = distribution.isSupportLowerBoundInclusive();
        assertTrue(lowerBoundInclusive);
        boolean upperBoundInclusive = distribution.isSupportUpperBoundInclusive();
        assertFalse(upperBoundInclusive);
    }

    @Test
    public void testFDistributionSupportConnected() {
        FDistribution distribution = new FDistribution(3.0, 4.0);
        boolean supportConnected = distribution.isSupportConnected();
        assertTrue(supportConnected);
    }
}