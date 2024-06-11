import org.junit.Test;
import org.apache.commons.math.distribution.PoissonDistributionImpl;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {
    
    @Test
    public void testGetMean() {
        // Create a Poisson distribution with mean 10
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(10);
        
        // Test that the mean is returned correctly
        assertEquals(10, distribution.getMean(), 0);
    }
    
    @Test
    public void testProbability() {
        // Create a Poisson distribution with mean 5
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        
        // Test the probability for various values of x
        assertEquals(0.006737946999085467, distribution.probability(0), 1e-6);
        assertEquals(0.03368973499542734, distribution.probability(1), 1e-6);
        assertEquals(0.08422433748856836, distribution.probability(2), 1e-6);
        assertEquals(0.1403735615476912, distribution.probability(3), 1e-6);
        assertEquals(0.17546700193443825, distribution.probability(4), 1e-6);
        assertEquals(0.17546700193443823, distribution.probability(5), 1e-6);
        assertEquals(0.14622250161119853, distribution.probability(6), 1e-6);
        assertEquals(0.10444464336442751, distribution.probability(7), 1e-6);
        assertEquals(0.0652779021027797, distribution.probability(8), 1e-6);
        assertEquals(0.036265501168211, distribution.probability(9), 1e-6);
        assertEquals(0.018132750584105485, distribution.probability(10), 1e-6);
        assertEquals(0.008242687540057039, distribution.probability(11), 1e-6);
        assertEquals(0.003434536475023763, distribution.probability(12), 1e-6);
        assertEquals(0.0013162472476949448, distribution.probability(13), 1e-6);
        assertEquals(0.0004714525881053418, distribution.probability(14), 1e-6);
        assertEquals(0.00015715086270178057, distribution.probability(15), 1e-6);
        assertEquals(4.913464460680643E-5, distribution.probability(16), 1e-6);
        assertEquals(1.4592785181212013E-5, distribution.probability(17), 1e-6);
        assertEquals(4.168794337774575E-6, distribution.probability(18), 1e-6);
        assertEquals(1.141295206835756E-6, distribution.probability(19), 1e-6);
    }
    
    @Test
    public void testCumulativeProbability() {
        
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        
        // Test the cumulative probability for various values of x
        assertEquals(0.006737946999085467, distribution.cumulativeProbability(0), 1e-6);
        assertEquals(0.04042768199451381, distribution.cumulativeProbability(1), 1e-6);
        assertEquals(0.12465201948308217, distribution.cumulativeProbability(2), 1e-6);
        assertEquals(0.2650255810307734, distribution.cumulativeProbability(3), 1e-6);
        assertEquals(0.44049258296521165, distribution.cumulativeProbability(4), 1e-6);
        assertEquals(0.6159595848996499, distribution.cumulativeProbability(5), 1e-6);
        assertEquals(0.7621820865108033, distribution.cumulativeProbability(6), 1e-6);
        assertEquals(0.8666267298752309, distribution.cumulativeProbability(7), 1e-6);
        assertEquals(0.9319046329780106, distribution.cumulativeProbability(8), 1e-6);
        assertEquals(0.9681701341462216, distribution.cumulativeProbability(9), 1e-6);
        assertEquals(0.9863028843143279, distribution.cumulativeProbability(10), 1e-6);
        assertEquals(0.9945455718543849, distribution.cumulativeProbability(11), 1e-6);
        assertEquals(0.9979801083294087, distribution.cumulativeProbability(12), 1e-6);
        assertEquals(0.9992943555771035, distribution.cumulativeProbability(13), 1e-6);
        assertEquals(0.9997658081652088, distribution.cumulativeProbability(14), 1e-6);
        assertEquals(0.9999239590289107, distribution.cumulativeProbability(15), 1e-6);
        assertEquals(0.9999730936735777, distribution.cumulativeProbability(16), 1e-6);
        assertEquals(0.999988686458198, distribution.cumulativeProbability(17), 1e-6);
        assertEquals(0.9999938552529762, distribution.cumulativeProbability(18), 1e-6);
        assertEquals(0.999996997548213, distribution.cumulativeProbability(19), 1e-6);
    }
    
    @Test
    public void testNormalApproximateProbability() throws MathException {
        
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        
        // Test the normal approximation probability for various values of x
        assertEquals(0.003822904536873467, distribution.normalApproximateProbability(0), 1e-6);
        assertEquals(0.028243634583064273, distribution.normalApproximateProbability(1), 1e-6);
        assertEquals(0.13178179226029283, distribution.normalApproximateProbability(2), 1e-6);
        assertEquals(0.346382992465924, distribution.normalApproximateProbability(3), 1e-6);
        assertEquals(0.628485089190451, distribution.normalApproximateProbability(4), 1e-6);
        assertEquals(0.8472746839897256, distribution.normalApproximateProbability(5), 1e-6);
        assertEquals(0.9512597574651706, distribution.normalApproximateProbability(6), 1e-6);
        assertEquals(0.9880915466037444, distribution.normalApproximateProbability(7), 1e-6);
        assertEquals(0.9969747204276041, distribution.normalApproximateProbability(8), 1e-6);
        assertEquals(0.9994528849918513, distribution.normalApproximateProbability(9), 1e-6);
        assertEquals(0.9999267908262225, distribution.normalApproximateProbability(10), 1e-6);
        assertEquals(0.9999934350207663, distribution.normalApproximateProbability(11), 1e-6);
        assertEquals(0.9999996354302954, distribution.normalApproximateProbability(12), 1e-6);
        assertEquals(0.9999999988762373, distribution.normalApproximateProbability(13), 1e-6);
        assertEquals(0.9999999999997606, distribution.normalApproximateProbability(14), 1e-6);
        assertEquals(0.9999999999999974, distribution.normalApproximateProbability(15), 1e-6);
        assertEquals(0.9999999999999999, distribution.normalApproximateProbability(16), 1e-6);
        assertEquals(0.9999999999999999, distribution.normalApproximateProbability(17), 1e-6);
        assertEquals(0.9999999999999999, distribution.normalApproximateProbability(18), 1e-6);
        assertEquals(0.9999999999999999, distribution.normalApproximateProbability(19), 1e-6);
    }
    
    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        
        // Test that the sampled value is within the expected range
        int sample = distribution.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }
}