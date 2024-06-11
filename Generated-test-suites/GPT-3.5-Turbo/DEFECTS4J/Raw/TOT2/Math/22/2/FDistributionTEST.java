import static org.junit.Assert.*;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDist = new FDistribution(5.0, 10.0);
        assertEquals(0.03677483318861919, fDist.density(2.0), 1e-15);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDist = new FDistribution(5.0, 10.0);
        assertEquals(0.07324301073487404, fDist.cumulativeProbability(2.0), 1e-15);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(5.0, 10.0);
        assertEquals(5.0, fDist.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(5.0, 10.0);
        assertEquals(10.0, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDist = new FDistribution(5.0, 10.0);
        assertEquals(5.555555555555555, fDist.getNumericalMean(), 1e-15);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDist = new FDistribution(5.0, 10.0);
        assertEquals(4.166666666666666, fDist.getNumericalVariance(), 1e-15);
    }

    // Add more test cases for edge cases and exceptions if needed

}