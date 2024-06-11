
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(10, 20);
        assertEquals(0.017685317442171, fDistribution.density(0.5), 1e-6);
        assertEquals(0, fDistribution.density(-1), 0);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(10, 20);
        assertEquals(0.25463955266375, fDistribution.cumulativeProbability(0.5), 1e-6);
        assertEquals(0, fDistribution.cumulativeProbability(-1), 0);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(10, 20);
        assertEquals(10, fDistribution.getNumeratorDegreesOfFreedom(), 0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(10, 20);
        assertEquals(20, fDistribution.getDenominatorDegreesOfFreedom(), 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeNumeratorDegreesOfFreedom() {
        new FDistribution(-5, 20);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeDenominatorDegreesOfFreedom() {
        new FDistribution(10, -5);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(10, 20);
        assertEquals(1.25, fDistribution.getNumericalMean(), 0);
    }
}
