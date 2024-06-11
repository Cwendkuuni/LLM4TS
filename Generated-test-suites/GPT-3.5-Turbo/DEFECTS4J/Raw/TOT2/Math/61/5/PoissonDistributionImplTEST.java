import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        
        assertEquals(0.1353352832366127, distribution.probability(0), 0.0001);
        assertEquals(0.20521249629516169, distribution.probability(1), 0.0001);
        assertEquals(0.21330935857337894, distribution.probability(2), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        
        try {
            assertEquals(0.080301397071394, distribution.cumulativeProbability(0), 0.0001);
            assertEquals(0.3532045260258423, distribution.cumulativeProbability(1), 0.0001);
            assertEquals(0.6472317030342747, distribution.cumulativeProbability(2), 0.0001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        
        try {
            assertEquals(0.06404855548634036, distribution.normalApproximateProbability(0), 0.0001);
            assertEquals(0.26598552904870077, distribution.normalApproximateProbability(1), 0.0001);
            assertEquals(0.6845757259132758, distribution.normalApproximateProbability(2), 0.0001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        
        try {
            int sample = distribution.sample();
            assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }
}