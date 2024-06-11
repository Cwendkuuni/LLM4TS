import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstructorWithMeanAndStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl(0.5, 2.0);
        assertEquals(0.5, distribution.getMean(), 0.001);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplConstructorWithNegativeStandardDeviation() {
        new NormalDistributionImpl(0.5, -2.0);
    }

    @Test
    public void testDensity() {
        NormalDistribution distribution = new NormalDistributionImpl(0.5, 2.0);
        assertEquals(0.03275845464354923, distribution.density(1.5), 0.001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.5, 2.0);
        assertEquals(0.1544306691908434, distribution.cumulativeProbability(1.5), 0.001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.5, 2.0);
        distribution.cumulativeProbability(100000.0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.5, 2.0);
        assertEquals(1.964193913682277, distribution.inverseCumulativeProbability(0.95), 0.001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.5, 2.0);
        distribution.inverseCumulativeProbability(0.1);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.5, 2.0);
        double sample = distribution.sample();
        assertEquals(0.5, distribution.cumulativeProbability(sample), 0.01);
    }
}