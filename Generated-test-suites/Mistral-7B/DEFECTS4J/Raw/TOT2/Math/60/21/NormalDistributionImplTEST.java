import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_MeanAndStandardDeviation() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(1.0, 2.0);
        NormalDistribution expected = new NormalDistribution();
        assertEquals(expected, normalDistribution);
        assertEquals(1.0, normalDistribution.getMean(), 0.001);
        assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testNormalDistributionImpl_Density() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double density = distribution.density(0.5);
        assertEquals(0.12566370614359172, density, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImpl_NegativeMeanAndStandardDeviation() {
        new NormalDistributionImpl(-1, -2);
    }

    @Test
    public void testNormalDistributionImpl_CumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double p = 0.5;
        double actual = distribution.cumulativeProbability(0.5);
        assertEquals(p, actual, 0.0001);

        p = 0;
        actual = distribution.cumulativeProbability(-5);
        assertEquals(p, actual, 0.0001);

        p = 1;
        actual = distribution.cumulativeProbability(5);
        assertEquals(p, actual, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImpl_InverseCumulativeProbability_OutOfRange() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double p = 1.5;
        distribution.inverseCumulativeProbability(p);
    }

    @Test
    public void testNormalDistributionImpl_Sample() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double sample = distribution.sample();
        assertTrue(sample >= -3 && sample <= 3);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImpl_Sample_Error() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        distribution.sample();
    }
}