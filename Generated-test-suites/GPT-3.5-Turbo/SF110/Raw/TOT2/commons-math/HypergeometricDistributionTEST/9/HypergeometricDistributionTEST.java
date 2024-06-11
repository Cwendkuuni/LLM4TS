import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, hyperDist.getPopulationSize());
        assertEquals(5, hyperDist.getNumberOfSuccesses());
        assertEquals(3, hyperDist.getSampleSize());
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.3636, hyperDist.cumulativeProbability(2), 0.0001);
    }
    
    // Other test cases for the remaining methods...

}