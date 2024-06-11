import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    @Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double density = dist.density(0.5);
        assertEquals(0.1273205390732781, density, 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double cp = dist.cumulativeProbability(0.5);
        assertEquals(0.682689492137, cp, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityLessThanZero() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 0.0;
        dist.inverseCumulativeProbability(p);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityGreaterThanOne() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 1.0;
        dist.inverseCumulativeProbability(p);
    }

    @Test
    public void testSample() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double sample = dist.sample();
        assertTrue(sample >= -3.0 && sample <= 3.0);
    }
}