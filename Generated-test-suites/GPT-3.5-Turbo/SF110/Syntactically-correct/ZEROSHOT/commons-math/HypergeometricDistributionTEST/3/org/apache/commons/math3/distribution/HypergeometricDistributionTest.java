import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativePopulation() {
        new HypergeometricDistribution(-1, 2, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroPopulation() {
        new HypergeometricDistribution(0, 2, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeSuccesses() {
        new HypergeometricDistribution(10, -2, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeSamples() {
        new HypergeometricDistribution(10, 2, -3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorSuccessesLargerThanPopulation() {
        new HypergeometricDistribution(10, 20, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorSamplesLargerThanPopulation() {
        new HypergeometricDistribution(10, 2, 15);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        assertEquals(0.001, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.171, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.1, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.592, distribution.cumulativeProbability(3), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(4), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(5), 0.0001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(0.0, distribution.probability(-1), 0.0001);
        assertEquals(0.0, distribution.probability(6), 0.0001);
        assertEquals(0.1, distribution.probability(2), 0.0001);
        assertEquals(0.126, distribution.probability(3), 0.0001);
        assertEquals(0.252, distribution.probability(4), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(1.0, distribution.upperCumulativeProbability(-1), 0.0001);
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.999, distribution.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.827, distribution.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.7, distribution.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.408, distribution.upperCumulativeProbability(4), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(5), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(6), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(1.5, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(0.3913, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        assertEquals(3, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 3, 5);
        assertTrue(distribution.isSupportConnected());
    }

}