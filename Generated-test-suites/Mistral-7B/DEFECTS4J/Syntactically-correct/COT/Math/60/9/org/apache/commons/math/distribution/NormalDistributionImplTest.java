import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstructorWithMeanAndStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals("Mean should be set correctly", 1.0, distribution.getMean(), 0.01);
        assertEquals("Standard deviation should be set correctly", 2.0, distribution.getStandardDeviation(), 0.01);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplConstructorWithInvalidStandardDeviation() {
        new NormalDistributionImpl(1.0, 0.0);
    }

    @Test
    public void testNormalDistributionImplDensity() {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        double result = distribution.density(2.5);
        assertEquals("Density should be correct", 0.0451253313282811, result, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImplCumulativeProbabilityWithValueOutOfRange() {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        distribution.cumulativeProbability(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testNormalDistributionImplCumulativeProbability() {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        double result = distribution.cumulativeProbability(1.5);
        assertEquals("Cumulative probability should be correct", 0.433192576351346, result, 0.001);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImplInverseCumulativeProbabilityWithInvalidProbability() {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        distribution.inverseCumulativeProbability(-1.0);
    }

    @Test
    public void testNormalDistributionImplInverseCumulativeProbability() throws Exception {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        double result = distribution.inverseCumulativeProbability(0.5);
        assertEquals("Inverse cumulative probability should be correct", 0.0, result, 0.01);
    }

    @Test
    public void testNormalDistributionImplSample() throws Exception {
        NormalDistribution distribution = new NormalDistributionImpl();
        double result = distribution.sample();
        assertTrue("Sample should be a valid normal distribution value", result >= -3.4 && result <= 3.4);
    }
}