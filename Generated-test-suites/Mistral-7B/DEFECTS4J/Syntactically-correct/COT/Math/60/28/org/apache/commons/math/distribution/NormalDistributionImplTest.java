import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstructor() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, normalDistribution.getMean(), 0.001);
        assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplNegativeStandardDeviation() {
        new NormalDistributionImpl(1.0, -1.0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double density = normalDistribution.density(0.5);
        assertEquals(0.15430806348152795, density, 0.00001);
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.75;
        double cumulativeProbability = normalDistribution.cumulativeProbability(p);
        assertEquals(0.6724275375825001, cumulativeProbability, 0.00001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() throws Exception {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double p = 2.0;
        normalDistribution.cumulativeProbability(p);
    }

    @Test
    public void testInverseCumulativeProbability() throws Exception {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.75;
        double inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(p);
        assertEquals(0.6744897544693669, inverseCumulativeProbability, 0.00001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testInverseCumulativeProbabilityInvalidProbability() throws Exception {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double p = 2.0;
        normalDistribution.inverseCumulativeProbability(p);
    }

    @Test
    public void testSample() throws Exception {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double sample = normalDistribution.sample();
        assertTrue(sample >= -3.0 && sample <= 3.0);
    }

    @Test(expected = MathException.class)
    public void testSampleException() throws Exception {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        normalDistribution.sample();
    }
}