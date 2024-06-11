import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(2.5);
        assertEquals(0.100744391885618, pd.probability(0), 0.0001);
        assertEquals(0.327170656286527, pd.probability(1), 0.0001);
        assertEquals(0.231146143234336, pd.probability(2), 0.0001);
        assertEquals(0.179966662281373, pd.probability(3), 0.0001);
        assertEquals(0.131551918498297, pd.probability(4), 0.0001);
        assertEquals(0.100744391885618, pd.probability(5), 0.0001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testNegativeMean() {
        new PoissonDistributionImpl(-1);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(2.5);
        assertEquals(0.0, pd.cumulativeProbability(-1), 0.0);
        assertEquals(0.100744391885618, pd.cumulativeProbability(0), 0.0001);
        assertEquals(0.231146143234336, pd.cumulativeProbability(1), 0.0001);
        assertEquals(0.361892314215955, pd.cumulativeProbability(2), 0.0001);
        assertEquals(0.532833623456921, pd.cumulativeProbability(3), 0.0001);
        assertEquals(0.664579883331336, pd.cumulativeProbability(4), 0.0001);
        assertEquals(0.837122133748701, pd.cumulativeProbability(5), 0.0001);
        assertEquals(1.0, pd.cumulativeProbability(Integer.MAX_VALUE), 0.0);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityTooLarge() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(2.5);
        pd.cumulativeProbability(Integer.MAX_VALUE + 1);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(2.5);
        assertEquals(0.100744391885618, pd.normalApproximateProbability(0), 0.0001);
        assertEquals(0.327170656286527, pd.normalApproximateProbability(1), 0.0001);
        assertEquals(0.231146143234336, pd.normalApproximateProbability(2), 0.0001);
        assertEquals(0.179966662281373, pd.normalApproximateProbability(3), 0.0001);
        assertEquals(0.131551918498297, pd.normalApproximateProbability(4), 0.0001);
        assertEquals(0.100744391885618, pd.normalApproximateProbability(5), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(2.5);
        int sample = pd.sample();
        assertTrue(sample >= 0 && sample <= 1000 * pd.getMean());
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(2.5);
        assertEquals(0, pd.inverseCumulativeProbability(0), 0);
        assertEquals(1, pd.inverseCumulativeProbability(1), 0);
        assertEquals(2, pd.inverseCumulativeProbability(pd.cumulativeProbability(2)), 0);
        assertEquals(3, pd.inverseCumulativeProbability(pd.cumulativeProbability(3)), 0);
        assertEquals(4, pd.inverseCumulativeProbability(pd.cumulativeProbability(4)), 0);
        assertEquals(5, pd.inverseCumulativeProbability(pd.cumulativeProbability(5)), 0);
        assertEquals(6, pd.inverseCumulativeProbability(pd.cumulativeProbability(6)), 0);
    }
}