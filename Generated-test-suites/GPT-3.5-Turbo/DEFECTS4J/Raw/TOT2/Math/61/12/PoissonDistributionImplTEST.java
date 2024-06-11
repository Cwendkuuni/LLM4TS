import org.apache.commons.math.MathException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        assertEquals(5.0, distribution.getMean(), 0.0001);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(0.049787, distribution.probability(1), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        try {
            assertEquals(0.2381030, distribution.cumulativeProbability(3), 0.0001);
        } catch (MathException ex) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(4.0);
        try {
            assertEquals(3, distribution.inverseCumulativeProbability(0.303167), 0.0001);
        } catch (MathException ex) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testRandomSample() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(6.0);
        try {
            int sample = distribution.sample();
            assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
        } catch (MathException ex) {
            fail("Exception thrown");
        }
    }

}