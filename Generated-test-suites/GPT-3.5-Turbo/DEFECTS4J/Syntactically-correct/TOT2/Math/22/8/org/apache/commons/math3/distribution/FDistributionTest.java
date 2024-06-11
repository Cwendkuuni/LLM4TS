import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDist = new FDistribution(3, 5);
        assertEquals(0.0, fDist.density(0.0), 1e-9);
        assertEquals(0.278233873646577, fDist.density(1.0), 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDist = new FDistribution(3, 5);
        assertEquals(0.0, fDist.cumulativeProbability(-1.0), 1e-9);
        assertEquals(0.0, fDist.cumulativeProbability(0.0), 1e-9);
        assertEquals(0.964541855466522, fDist.cumulativeProbability(4.0), 1e-9);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDist = new FDistribution(3, 5);
        assertEquals(Double.NaN, fDist.getNumericalMean(), 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDist = new FDistribution(3, 5);
        assertEquals(Double.NaN, fDist.getNumericalVariance(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException() {
        new FDistribution(0, 5);
    }

}