import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionTest {

    @Test
    public void testFDistributionConstruction() {
        // Test construction with valid parameters
        FDistribution fDist = new FDistribution(2.0, 3.0);
        assertEquals(2.0, fDist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, fDist.getDenominatorDegreesOfFreedom(), 0.0);

        // Test construction with inverse cumulative probability accuracy
        FDistribution fDistInvAcc = new FDistribution(2.0, 3.0, 0.001);
        assertEquals(2.0, fDistInvAcc.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, fDistInvAcc.getDenominatorDegreesOfFreedom(), 0.0);
        assertEquals(0.001, fDistInvAcc.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        double densityValue = fDist.density(1.5);
        assertEquals(0.0209037323820938, densityValue, 0.0001);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        double cumulativeProbabilityValue = fDist.cumulativeProbability(1.5);
        assertEquals(0.914359472421254, cumulativeProbabilityValue, 0.0001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeParametersConstruction() {
        FDistribution fDist = new FDistribution(-2.0, -3.0);
    }

    @Test
    public void testFDistributionCalculateNumericalVariance() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        double numericalVariance = fDist.getNumericalVariance();
        assertEquals(1.64205224196446, numericalVariance, 0.01);
    }

    @Test
    public void testFDistributionGetSupportLowerBound() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        double lowerBound = fDist.getSupportLowerBound();
        assertEquals(0.0, lowerBound, 0.0);
    }

    @Test
    public void testFDistributionGetSupportUpperBound() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        double upperBound = fDist.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, upperBound, 0.0);
    }
}