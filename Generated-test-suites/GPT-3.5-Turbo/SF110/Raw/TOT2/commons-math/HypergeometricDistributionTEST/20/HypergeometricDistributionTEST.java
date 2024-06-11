
import static org.junit.Assert.*;

import org.junit.Test;

public class HypergeometricDistributionTest {
    @Test
    public void testHypergeometricDistributionConstructor() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(10, hypergeometricDistribution.getPopulationSize());
        assertEquals(3, hypergeometricDistribution.getNumberOfSuccesses());
        assertEquals(5, hypergeometricDistribution.getSampleSize());
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(0.0, hypergeometricDistribution.cumulativeProbability(-1), 0.0001);
        assertEquals(1.0, hypergeometricDistribution.cumulativeProbability(100), 0.0001);
        assertEquals(0.224, hypergeometricDistribution.cumulativeProbability(2), 0.001);
    }

    // Add more test cases for other methods
}
