import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testNormalDistributionImplConstruction() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertTrue(dist instanceof NormalDistributionImpl);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test constructor with invalid argument (negative standard deviation).
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplConstructionNegativeStandardDeviation() {
        new NormalDistributionImpl(0.0, -1.0);
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double density = dist.density(0.5);
        assertEquals(0.154308063481527, density, 0.0001);
    }

    /**
     * Test cumulativeProbability method with valid argument.
     */
    @Test
    public void testCumulativeProbabilityValidArgument() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = dist.cumulativeProbability(1.0);
        assertEquals(0.841344746013577, p, 0.0001);
    }

    /**
     * Test cumulativeProbability method with extreme argument.
     */
    @Test
    public void testCumulativeProbabilityExtremeArgument() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = dist.cumulativeProbability(-50.0);
        assertEquals(0.0, p, 0.0001);

        p = dist.cumulativeProbability(50.0 + 10.0 * Math.sqrt(2.0 * Math.PI));
        assertEquals(1.0, p, 0.0001);
    }

    /**
     * Test inverseCumulativeProbability method with valid argument.
     */
    @Test
    public void testInverseCumulativeProbabilityValidArgument() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.95;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(1.64485362695134, x, 0.001);
    }

    /**
     * Test inverseCumulativeProbability method with edge cases.
     */
    @Test
    public void testInverseCumulativeProbabilityEdgeCases() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);

        // Zero probability
        double p = 0.0;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(Double.NEGATIVE_INFINITY, x, 0.0);

        // One probability
        p = 1.0;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(Double.POSITIVE_INFINITY, x, 0.0);

        // Negative probability
        p = -1.0;
        try {
            x = dist.inverseCumulativeProbability(p);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double sample = dist.sample();
        assertTrue(sample >= -3.0 && sample <= 3.0);
    }
}