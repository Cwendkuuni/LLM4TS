import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        double x = 2.0;
        double expected = 0.764213055;
        double delta = 0.000000001;
        double result = fDistribution.cumulativeProbability(x);
        assertEquals(expected, result, delta);
    }

    @Test
    public void testCumulativeProbabilityNegativeX() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        double x = -2.0;
        double expected = 0.0;
        double delta = 0.0;
        double result = fDistribution.cumulativeProbability(x);
        assertEquals(expected, result, delta);
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        double p = 0.6;
        double expected = 0.704875536;
        double delta = 0.000000001;
        double result = fDistribution.inverseCumulativeProbability(p);
        assertEquals(expected, result, delta);
    }

    @Test
    public void testInverseCumulativeProbabilityZeroP() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        double p = 0.0;
        double expected = 0.0;
        double delta = 0.0;
        double result = fDistribution.inverseCumulativeProbability(p);
        assertEquals(expected, result, delta);
    }

    @Test
    public void testInverseCumulativeProbabilityOneP() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        double p = 1.0;
        double expected = Double.POSITIVE_INFINITY;
        double delta = 0.0;
        double result = fDistribution.inverseCumulativeProbability(p);
        assertEquals(expected, result, delta);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomNegativeValue() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        fDistribution.setNumeratorDegreesOfFreedom(-2.0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomNegativeValue() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        fDistribution.setDenominatorDegreesOfFreedom(-3.0);
    }
}