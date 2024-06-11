import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(0.22404180765538789, distribution.probability(2), 0.0001);
        assertEquals(0.049787068367863944, distribution.probability(5), 0.0001);
        assertEquals(0.36787944117144233, distribution.probability(1), 0.0001);
        assertEquals(0.049787068367863944, distribution.probability(0), 0.0001);
        assertEquals(0.0, distribution.probability(-1), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        try {
            assertEquals(0.42319008112684364, distribution.cumulativeProbability(2), 0.0001);
            assertEquals(0.9522823581380932, distribution.cumulativeProbability(5), 0.0001);
            assertEquals(0.9084218055563291, distribution.cumulativeProbability(1), 0.0001);
            assertEquals(0.950212931632136, distribution.cumulativeProbability(0), 0.0001);
        } catch (MathException e) {
            fail("Exception occurred during cumulative probability calculation.");
        }
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        try {
            assertEquals(0.9048374180359595, distribution.normalApproximateProbability(2), 0.0001);
            assertEquals(0.9971762154235702, distribution.normalApproximateProbability(5), 0.0001);
        } catch (MathException e) {
            fail("Exception occurred during normal approximate probability calculation.");
        }
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        try {
            int sample = distribution.sample();
            assertTrue(0 <= sample && sample <= Integer.MAX_VALUE);
        } catch (MathException e) {
            fail("Exception occurred during sample generation.");
        }
    }

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(3.0, distribution.getMean(), 0.0001);
    }

}