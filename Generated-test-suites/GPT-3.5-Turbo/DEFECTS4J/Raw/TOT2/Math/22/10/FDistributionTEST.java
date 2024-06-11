import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionTest {

    // Test case for density method
    @Test
    public void testDensity() {
        FDistribution fDist = new FDistribution(5, 10);
        assertEquals(0.06328962911644314, fDist.density(0.5), 1e-10);
    }

    // Test case for cumulativeProbability method
    @Test
    public void testCumulativeProbability() {
        FDistribution fDist = new FDistribution(5, 10);
        assertEquals(0.48541100431943315, fDist.cumulativeProbability(0.7), 1e-10);
    }

    // Test case for getNumeratorDegreesOfFreedom method
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(5, 10);
        assertEquals(5, fDist.getNumeratorDegreesOfFreedom(), 0);
    }

    // Test case for getDenominatorDegreesOfFreedom method
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(5, 10);
        assertEquals(10, fDist.getDenominatorDegreesOfFreedom(), 0);
    }

    // Test case for getNumericalMean method
    @Test
    public void testGetNumericalMean() {
        FDistribution fDist = new FDistribution(5, 10);
        assertEquals(3.3333333333333335, fDist.getNumericalMean(), 1e-10);
    }

    // Test case for getNumericalVariance method
    @Test
    public void testGetNumericalVariance() {
        FDistribution fDist = new FDistribution(5, 10);
        assertEquals(4.761904761904762, fDist.getNumericalVariance(), 1e-10);
    }

    // Test case for getSupportLowerBound method
    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDist = new FDistribution(5, 10);
        assertEquals(0, fDist.getSupportLowerBound(), 0);
    }

    // Test case for getSupportUpperBound method
    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDist = new FDistribution(5, 10);
        assertEquals(Double.POSITIVE_INFINITY, fDist.getSupportUpperBound(), 0);
    }
}