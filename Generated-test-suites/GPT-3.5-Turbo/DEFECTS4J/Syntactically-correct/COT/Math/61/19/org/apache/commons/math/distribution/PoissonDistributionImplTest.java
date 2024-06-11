
import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.special.Gamma;
import org.apache.commons.math.util.FastMath;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PoissonDistributionImplTest {

    private PoissonDistributionImpl poisson;

    @Before
    public void setUp() {
        poisson = new PoissonDistributionImpl(5);
    }

    /**
     * Test the probability method with valid inputs.
     */
    @Test
    public void testProbability() {
        assertEquals(0.0336897349954273, poisson.probability(3), 0.0000000001);
        assertEquals(0.0820849986238988, poisson.probability(5), 0.0000000001);
        assertEquals(0.00673794699908547, poisson.probability(0), 0.0000000001);
    }

    /**
     * Test the probability method with an invalid input.
     */
    @Test
    public void testProbabilityInvalidInput() {
        try {
            poisson.probability(-1);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains(LocalizedFormats.NOT_POSITIVE_POISSON_MEAN.toString()));
        }
    }

    /**
     * Test the cumulativeProbability method with valid inputs.
     */
    @Test
    public void testCumulativeProbability() {
        try {
            assertEquals(0.265025915297361, poisson.cumulativeProbability(3), 0.0000000001);
            assertEquals(0.616114971052316, poisson.cumulativeProbability(5), 0.0000000001);
            assertEquals(0.606530659712633, poisson.cumulativeProbability(4), 0.0000000001);
            assertEquals(0.024600586130814, poisson.cumulativeProbability(1), 0.0000000001);
        } catch (MathException e) {
            fail("Should not have thrown MathException");
        }
    }

    /**
     * Test the cumulativeProbability method with an out of range input.
     */
    @Test
    public void testCumulativeProbabilityOutOfRange() {
        try {
            assertEquals(0, poisson.cumulativeProbability(-1), 0.0000000001);
            assertEquals(1, poisson.cumulativeProbability(Integer.MAX_VALUE), 0.0000000001);
        } catch (MathException e) {
            fail("Should not have thrown MathException");
        }
    }

    /**
     * Test the cumulativeProbability method with a large mean value.
     */
    @Test
    public void testCumulativeProbabilityLargeMean() {
        PoissonDistributionImpl poissonLargeMean = new PoissonDistributionImpl(1000);
        try {
            assertEquals(0.999999999941296, poisson.cumulativeProbability(3000), 0.0000000001);
            assertEquals(0.0292529837756841, poisson.cumulativeProbability(950), 0.0000000001);
            assertEquals(0.999999968314684, poisson.cumulativeProbability(999), 0.0000000001);
        } catch (MathException e) {
            fail("Should not have thrown MathException");
        }
    }

    /**
     * Test the normalApproximateProbability method with valid inputs.
     */
    @Test
    public void testNormalApproximateProbability() {
        try {
            assertEquals(0.639230558268467, poisson.normalApproximateProbability(3), 0.0000000001);
            assertEquals(0.712260919094445, poisson.normalApproximateProbability(5), 0.0000000001);
            assertEquals(0.888392247663876, poisson.normalApproximateProbability(10), 0.0000000001);
        } catch (MathException e) {
            fail("Should not have thrown MathException");
        }
    }

    /**
     * Test the sample method.
     */
    @Test
    public void testSample() {
        try {
            int sample = poisson.sample();
            assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
        } catch (MathException e) {
            fail("Should not have thrown MathException");
        }
    }

}
