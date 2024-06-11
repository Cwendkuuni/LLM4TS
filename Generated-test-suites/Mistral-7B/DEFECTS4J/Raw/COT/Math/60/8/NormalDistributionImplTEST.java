import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstructorWithValidArguments() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.getMean(), 0.001);
        assertEquals(1, distribution.getStandardDeviation(), 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplConstructorWithInvalidStandardDeviation() {
        new NormalDistributionImpl(0, -1);
    }

    @Test
    public void testNormalDistributionImplDensity() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double density = distribution.density(0.5);
        assertEquals(0.15430806348152764, density, 0.0001);
    }

    @Test
    public void testNormalDistributionImplCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double cumulativeProbability = distribution.cumulativeProbability(1.5);
        assertEquals(0.9772454592636468, cumulativeProbability, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImplCumulativeProbabilityWithInvalidArgument() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        distribution.cumulativeProbability(Double.NaN);
    }

    @Test
    public void testNormalDistributionImplInverseCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double inverseCumulativeProbability = distribution.inverseCumulativeProbability(0.5);
        assertEquals(0, inverseCumulativeProbability, 0.001);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImplInverseCumulativeProbabilityWithInvalidArgument() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        distribution.inverseCumulativeProbability(1.5);
    }

    @Test
    public void testNormalDistributionImplSample() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double sample = distribution.sample();
        assertEquals(0, sample, 0.01); // expected value is approximately 0, but with some tolerance
    }

}