import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    private static final double DELTA = 1e-15;
    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl(0.0, 1.0);
    }

    @Test
    public void testGetMean() {
        assertEquals(0.0, normalDistribution.getMean(), DELTA);
    }

    @Test
    public void testSetMean() {
        normalDistribution.setMean(1.5);
        assertEquals(1.5, normalDistribution.getMean(), DELTA);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, normalDistribution.getStandardDeviation(), DELTA);
    }

    @Test
    public void testSetStandardDeviation() {
        normalDistribution.setStandardDeviation(2.5);
        assertEquals(2.5, normalDistribution.getStandardDeviation(), DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegative() {
        normalDistribution.setStandardDeviation(-2.5);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        double result = normalDistribution.cumulativeProbability(0.0);
        assertEquals(0.5, result, DELTA);
    }

    @Test
    public void testInverseCumulativeProbabilityLowerBound() throws MathException {
        double result = normalDistribution.inverseCumulativeProbability(0.0);
        assertEquals(Double.NEGATIVE_INFINITY, result, DELTA);
    }

    @Test
    public void testInverseCumulativeProbabilityUpperBound() throws MathException {
        double result = normalDistribution.inverseCumulativeProbability(1.0);
        assertEquals(Double.POSITIVE_INFINITY, result, DELTA);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double result = normalDistribution.inverseCumulativeProbability(0.5);
        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void testGetDomainLowerBoundForLowerProbability() {
        double result = normalDistribution.getDomainLowerBound(0.25);
        assertEquals(-Double.MAX_VALUE, result, DELTA);
    }

    @Test
    public void testGetDomainLowerBoundForHigherProbability() {
        double result = normalDistribution.getDomainLowerBound(0.75);
        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void testGetDomainUpperBoundForLowerProbability() {
        double result = normalDistribution.getDomainUpperBound(0.25);
        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void testGetDomainUpperBoundForHigherProbability() {
        double result = normalDistribution.getDomainUpperBound(0.75);
        assertEquals(Double.MAX_VALUE, result, DELTA);
    }

    @Test
    public void testGetInitialDomainForLowerProbability() {
        double result = normalDistribution.getInitialDomain(0.25);
        assertEquals(-1.0, result, DELTA);
    }

    @Test
    public void testGetInitialDomainForHigherProbability() {
        double result = normalDistribution.getInitialDomain(0.75);
        assertEquals(1.0, result, DELTA);
    }
}