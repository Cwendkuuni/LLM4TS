import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.test.AbstractRealDistributionTest;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PoissonDistributionImplTest extends AbstractRealDistributionTest {

    @Test
    public void testProbability() {
        PoissonDistribution impl = new PoissonDistributionImpl(1.0);
        assertEquals(0.367978162526738, impl.probability(0), 1e-7);
        assertEquals(0.367924347433395, impl.probability(1), 1e-7);
        assertEquals(0.183992173722697, impl.probability(2), 1e-7);
        assertEquals(0.100259186157561, impl.probability(3), 1e-7);
        assertEquals(0.049779226351293, impl.probability(4), 1e-7);
        assertEquals(0.027445429570734, impl.probability(5), 1e-7);
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        PoissonDistribution impl = new PoissonDistributionImpl(1.0);
        assertEquals(0.0, impl.cumulativeProbability(Integer.MIN_VALUE), 1e-7);
        assertEquals(0.367924347433395, impl.cumulativeProbability(0), 1e-7);
        assertEquals(0.632045652566605, impl.cumulativeProbability(1), 1e-7);
        assertEquals(0.901986541894613, impl.cumulativeProbability(2), 1e-7);
        assertEquals(0.993327236638371, impl.cumulativeProbability(3), 1e-7);
        assertEquals(0.999737813437593, impl.cumulativeProbability(4), 1e-7);
        assertEquals(1.0, impl.cumulativeProbability(Integer.MAX_VALUE), 1e-7);
    }

    @Test
    public void testNormalApproximateProbability() throws Exception {
        PoissonDistributionImpl impl = new PoissonDistributionImpl(1.0);
        NormalDistribution normal = new NormalDistributionImpl(1.0, 1.0);
        for (int x = 0; x <= 10; x++) {
            double p = impl.probability(x);
            double pApprox = normal.cumulativeProbability(x + 0.5);
            assertEquals(p, pApprox, 1e-4);
        }
    }

    @Test
    public void testSample() throws Exception {
        PoissonDistributionImpl impl = new PoissonDistributionImpl(1.0);
        for (int test = 0; test < 10000; test++) {
            int s = impl.sample();
            assertTrue(s >= 0);
            assertEquals(impl.mean(), (double) impl.getQuantiles()[0], 1e-4);
        }
    }

    @Test(expected = MathRuntimeException.class)
    public void testSampleNegativeMean() {
        PoissonDistributionImpl impl = new PoissonDistributionImpl(-1.0);
    }
}