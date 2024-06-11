import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {
    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, distribution.getMean(), 0.000001);
    }
    
    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.0820849986238988, distribution.probability(1), 0.000001);
        assertEquals(0.0668009428905426, distribution.probability(2), 0.000001);
        assertEquals(0.0426887487514558, distribution.probability(3), 0.000001);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.0803013878858166, distribution.cumulativeProbability(1), 0.000001);
        assertEquals(0.147102224652359, distribution.cumulativeProbability(2), 0.000001);
        assertEquals(0.189791973403814, distribution.cumulativeProbability(3), 0.000001);
    }
    
    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.10533529198571657, distribution.normalApproximateProbability(1), 0.000001);
        assertEquals(0.189791973403814, distribution.normalApproximateProbability(2), 0.000001);
        assertEquals(0.2660852498987548, distribution.normalApproximateProbability(3), 0.000001);
    }
    
    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        int sample = distribution.sample();
        assertTrue(sample >= 0);
        assertTrue(sample <= Integer.MAX_VALUE);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0, distribution.inverseCumulativeProbability(0), 0.000001);
        assertEquals(1, distribution.inverseCumulativeProbability(0.0803013878858166), 0.000001);
        assertEquals(2, distribution.inverseCumulativeProbability(0.147102224652359), 0.000001);
        assertEquals(3, distribution.inverseCumulativeProbability(0.189791973403814), 0.000001);
        assertEquals(4, distribution.inverseCumulativeProbability(0.2264088884940305), 0.000001);
    }
}