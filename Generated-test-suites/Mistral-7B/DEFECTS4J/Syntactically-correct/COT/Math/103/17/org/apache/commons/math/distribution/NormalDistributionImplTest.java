import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.math3.util.Precision.absTolerance;
import static org.apache.commons.math3.util.Precision.eqn;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstructorMeanStdDev() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2.5, 3.5);
        assertEquals("Mean should be set to 2.5", 2.5, normalDistribution.getMean(), absTolerance);
        assertEquals("Standard deviation should be set to 3.5", 3.5, normalDistribution.getStandardDeviation(), absTolerance);
    }

    @Test
    public void testNormalDistributionImplConstructorMeanStdDevZero() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        assertEquals("Mean should be set to 0.0", 0.0, normalDistribution.getMean(), absTolerance);
        assertEquals("Standard deviation should be set to 1.0", 1.0, normalDistribution.getStandardDeviation(), absTolerance);
    }

    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2.5, 3.5);
        assertEquals("Mean should be 2.5", 2.5, normalDistribution.getMean(), absTolerance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMeanNegative() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        normalDistribution.setMean(-1.0);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        normalDistribution.setMean(3.5);
        assertEquals("Mean should be set to 3.5", 3.5, normalDistribution.getMean(), absTolerance);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2.5, 3.5);
        assertEquals("Standard deviation should be 3.5", 3.5, normalDistribution.getStandardDeviation(), absTolerance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegative() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        normalDistribution.setStandardDeviation(-1.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2.5, 3.5);
        double result;

        result = normalDistribution.cumulativeProbability(1.5);
        assertEquals("P(X < 1.5) should be approximately 0.0641", 0.0641, result, absTolerance);

        result = normalDistribution.cumulativeProbability(2.5);
        assertEquals("P(X < 2.5) should be approximately 0.5", 0.5, result, absTolerance);

        result = normalDistribution.cumulativeProbability(3.5);
        assertEquals("P(X < 3.5) should be approximately 0.9772", 0.9772, result, absTolerance);

        result = normalDistribution.cumulativeProbability(10.0);
        assertEquals("P(X < 10.0) should be 1.0", 1.0, result, absTolerance);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2.5, 3.5);
        normalDistribution.cumulativeProbability(-5.0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2.5, 3.5);
        double result;

        result = normalDistribution.inverseCumulativeProbability(0.0);
        assertEquals("x for P(X < x) = 0.0 should be Double.NEGATIVE_INFINITY", Double.NEGATIVE_INFINITY, result, absTolerance);

        result = normalDistribution.inverseCumulativeProbability(0.5);
        assertEquals("x for P(X < x) = 0.5 should be approximately 2.5", 2.5, result, absTolerance);

        result = normalDistribution.inverseCumulativeProbability(0.95);
        assertEquals("x for P(X < x) = 0.95 should be approximately 3.565", 3.565, result, absTolerance);

        result = normalDistribution.inverseCumulativeProbability(1.0);
        assertEquals("x for P(X < x) = 1.0 should be Double.POSITIVE_INFINITY", Double.POSITIVE_INFINITY, result, absTolerance);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2.5, 3.5);
        normalDistribution.inverseCumulativeProbability(-1.0);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2.5, 3.5);
        double result;

        result = normalDistribution.getDomainLowerBound(0.0);
        assertEquals("Domain lower bound for p = 0.0 should be Double.NEGATIVE_INFINITY", Double.NEGATIVE_INFINITY, result);

        result = normalDistribution.getDomainLowerBound(0.5);
        assertEquals("Domain lower bound for p = 0.5 should be 2.5", 2.5, result, absTolerance);

        result = normalDistribution.getDomainLowerBound(0.95);
        assertEquals("Domain lower bound for p = 0.95 should be Double.POSITIVE_INFINITY", Double.POSITIVE_INFINITY, result);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2.5, 3.5);
        double result;

        result = normalDistribution.getDomainUpperBound(0.0);
        assertEquals("Domain upper bound for p = 0.0 should be Double.POSITIVE_INFINITY", Double.POSITIVE_INFINITY, result);

        result = normalDistribution.getDomainUpperBound(0.5);
        assertEquals("Domain upper bound for p = 0.5 should be 2.5", 2.5, result, absTolerance);

        result = normalDistribution.getDomainUpperBound(0.95);
        assertEquals("Domain upper bound for p = 0.95 should be Double.POSITIVE_INFINITY", Double.POSITIVE_INFINITY, result);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2.5, 3.5);
        double result;

        result = normalDistribution.getInitialDomain(0.0);
        assertEquals("Initial domain value for p = 0.0 should be approximately 1.531", 1.531, result, absTolerance);

        result = normalDistribution.getInitialDomain(0.5);
        assertEquals("Initial domain value for p = 0.5 should be approximately 2.5", 2.5, result, absTolerance);

        result = normalDistribution.getInitialDomain(0.95);
        assertEquals("Initial domain value for p = 0.95 should be approximately 4.565", 4.565, result, absTolerance);
    }
}