import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstructorWithMeanAndStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0, dist.getMean(), 0.001);
        assertEquals(1, dist.getStandardDeviation(), 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplConstructorWithNegativeStandardDeviation() {
        new NormalDistributionImpl(0, -1);
    }

    @Test
    public void testNormalDistributionImplDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double density = dist.density(1);
        assertEquals(0.398942280401433, density, 0.0001);
    }

    @Test
    public void testNormalDistributionImplCumulativeProbability() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double cdf = dist.cumulativeProbability(0.5);
        assertEquals(0.682689462136865, cdf, 0.0001);

        try {
            dist.cumulativeProbability(5);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testNormalDistributionImplInverseCumulativeProbability() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double inverseCdf = dist.inverseCumulativeProbability(0.5);
        assertEquals(0, inverseCdf, 0.001);

        try {
            dist.inverseCumulativeProbability(-0.1);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testNormalDistributionImplSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double sample = dist.sample();
        assertEquals(0, sample, 0.01);
    }
}