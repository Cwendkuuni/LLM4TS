import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionCreation() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.getMean(), 0.001);
        assertEquals(1, distribution.getStandardDeviation(), 0.001);

        distribution = new NormalDistributionImpl(1, 2);
        assertEquals(1, distribution.getMean(), 0.001);
        assertEquals(2, distribution.getStandardDeviation(), 0.001);

        try {
            new NormalDistributionImpl(-1, 2);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        try {
            new NormalDistributionImpl(0.5, 0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testDensity() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);

        double density = distribution.density(0.5);
        assertEquals(0.154308018963, density, 0.0001);

        density = distribution.density(3.5);
        assertEquals(0.00012548489828, density, 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);

        double cp = distribution.cumulativeProbability(0.25);
        assertEquals(0.615475637425, cp, 0.0001);

        cp = distribution.cumulativeProbability(2.5);
        assertEquals(0.997310587093, cp, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws Exception {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);

        double p = 0.25;
        double icp = distribution.inverseCumulativeProbability(p);
        assertEquals(-0.679489768573, icp, 0.0001);

        p = 0.99;
        icp = distribution.inverseCumulativeProbability(p);
        assertEquals(3.06138520695, icp, 0.0001);
    }

    @Test
    public void testSample() throws Exception {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);

        double sample = distribution.sample();
        assertEquals(0.123330583433, sample, 0.001);

        sample = distribution.sample();
        assertEquals(-0.012135853399, sample, 0.001);
    }

    @Test
    public void testCumulativeProbabilityWithLargeValues() throws Exception {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);

        double cp = distribution.cumulativeProbability(50 * distribution.getStandardDeviation());
        assertEquals(1, cp, 0.0001);

        cp = distribution.cumulativeProbability(-50 * distribution.getStandardDeviation());
        assertEquals(0, cp, 0.0001);
    }

    @Test
    public void testErf() {
        double arg = 1.5;
        double expected = 0.999335874894;
        assertEquals(expected, Erf.erf(arg), 0.000001);

        arg = -1.5;
        expected = -0.999335874894;
        assertEquals(expected, Erf.erf(arg), 0.000001);
    }
}