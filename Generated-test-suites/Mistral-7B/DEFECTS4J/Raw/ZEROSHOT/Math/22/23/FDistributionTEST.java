import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testFDistributionConstruction() throws NotStrictlyPositiveException {
        FDistribution fDistribution = new FDistribution(2.0, 4.0);
        assertEquals(2.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(-1.0, 2.0);
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution fDistribution = new FDistribution(2.0, 4.0);
        double density = fDistribution.density(1.5);
        // The actual value of the density is not important here, only that it's non-negative
        assertTrue(density >= 0);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(2.0, 4.0);
        double cumProb = fDistribution.cumulativeProbability(1.0);
        assertTrue(cumProb >= 0 && cumProb <= 1);
    }

    @Test
    public void testFDistributionMean() {
        FDistribution fDistribution = new FDistribution(2.0, 4.0);
        double mean = fDistribution.getNumericalMean();
        assertEquals(2.5, mean, 0.001);
    }

    @Test
    public void testFDistributionVariance() {
        FDistribution fDistribution = new FDistribution(2.0, 4.0);
        double variance = fDistribution.getNumericalVariance();
        assertEquals(1.3888888888888888, variance, 0.0001);
    }

    @Test
    public void testFDistributionNumericalVarianceIsCalculated() {
        FDistribution fDistribution = new FDistribution(2.0, 4.0);
        assertFalse(fDistribution.isNumericalVarianceCalculated());
        fDistribution.getNumericalVariance();
        assertTrue(fDistribution.isNumericalVarianceCalculated());
    }

    @Test
    public void testFDistributionSupport() {
        FDistribution fDistribution = new FDistribution(2.0, 4.0);
        assertTrue(fDistribution.isSupportLowerBoundInclusive());
        assertTrue(fDistribution.isSupportUpperBoundExclusive());
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 0.0);
        assertEquals(0.0, fDistribution.getSupportLowerBound(), 0.0);
    }
}