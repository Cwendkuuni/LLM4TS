import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        assertEquals(0.0, fDistribution.density(0.0), 1e-15);
        assertEquals(0.06770099689544638, fDistribution.density(2.0), 1e-15);
        assertEquals(0.003066594748056853, fDistribution.density(10.0), 1e-15);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        assertEquals(0.0, fDistribution.cumulativeProbability(0.0), 1e-15);
        assertEquals(0.42165289577587033, fDistribution.cumulativeProbability(2.0), 1e-15);
        assertEquals(0.9999999923205563, fDistribution.cumulativeProbability(10.0), 1e-15);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        assertEquals(5.0, fDistribution.getNumericalMean(), 1e-15);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        assertEquals(30.0, fDistribution.getNumericalVariance(), 1e-15);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(-3.0, 5.0);
    }

}