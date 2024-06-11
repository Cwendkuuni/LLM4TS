import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertNotNull(distribution);
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(5, distribution.getNumberOfSuccesses());
        assertEquals(3, distribution.getSampleSize());
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.126, distribution.probability(1), 0.001);
        assertEquals(0.258, distribution.probability(2), 0.001);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.252, distribution.cumulativeProbability(2), 0.001);
        assertEquals(0.705, distribution.cumulativeProbability(3), 0.001);
    }

    @Test
    public void testSupportBounds() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0, distribution.getSupportLowerBound());
        assertEquals(3, distribution.getSupportUpperBound());
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(10, -5, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroPopulationSize() {
        new HypergeometricDistribution(0, 5, 3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 5, 15);
    }

}