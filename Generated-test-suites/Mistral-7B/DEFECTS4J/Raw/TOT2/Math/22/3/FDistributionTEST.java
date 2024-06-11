import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidDegreesOfFreedom() {
        try {
            new FDistribution(-1, 2);
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testDensity() {
        FDistribution distribution = new FDistribution(2, 3);
        double result = distribution.density(0.5);
        double expected = 0.10221380855541564;
        assertEquals(expected, result, 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution distribution = new FDistribution(2, 3);
        double result = distribution.cumulativeProbability(0.5);
        double expected = 0.5144784316147874;
        assertEquals(expected, result, 1e-9);
    }

    @Test
    public void testDensityWithNegativeX() {
        FDistribution distribution = new FDistribution(2, 3);
        double result = distribution.density(-1);
        double expected = 0;
        assertEquals(expected, result);
    }

    @Test
    public void testCumulativeProbabilityWithNegativeX() {
        FDistribution distribution = new FDistribution(2, 3);
        double result = distribution.cumulativeProbability(-1);
        double expected = 0;
        assertEquals(expected, result);
    }

    @Test
    public void testCalculateNumericalVariance() {
        FDistribution distribution = new FDistribution(2, 4);
        double result = distribution.getNumericalVariance();
        double expected = 2.0 / 3.0;
        assertEquals(expected, result, 1e-9);
    }
}