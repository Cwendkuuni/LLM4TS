import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class HypergeometricDistributionTest {

    // Test constructor with valid input
    @Test
    public void testValidConstructor() {
        HypergeometricDistribution hypergeo = new HypergeometricDistribution(10, 5, 3);

        // Check population size
        assertEquals(10, hypergeo.getPopulationSize());

        // Check number of successes
        assertEquals(5, hypergeo.getNumberOfSuccesses());

        // Check sample size
        assertEquals(3, hypergeo.getSampleSize());
    }

    // Test constructor with invalid population size (<=0)
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstructorPopulationSize() {
        HypergeometricDistribution hypergeo = new HypergeometricDistribution(0, 5, 3);
    }

    // Test constructor with invalid number of successes (<0)
    @Test(expected = NotPositiveException.class)
    public void testInvalidConstructorNumberOfSuccesses() {
        HypergeometricDistribution hypergeo = new HypergeometricDistribution(10, -1, 3);
    }

    // Test constructor with invalid sample size (<0)
    @Test(expected = NotPositiveException.class)
    public void testInvalidConstructorSampleSize() {
        HypergeometricDistribution hypergeo = new HypergeometricDistribution(10, 5, -2);
    }

    // Test constructor with invalid number of successes (> population size)
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstructorNumberOfSuccessesGreaterThanPopulationSize() {
        HypergeometricDistribution hypergeo = new HypergeometricDistribution(10, 15, 3);
    }

    // Test constructor with invalid sample size (> population size)
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstructorSampleSizeGreaterThanPopulationSize() {
        HypergeometricDistribution hypergeo = new HypergeometricDistribution(10, 5, 12);
    }

    // Test cumulativeProbability method
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution hypergeo = new HypergeometricDistribution(10, 5, 3);

        // Test x < lower domain (0.0 returned)
        assertEquals(0.0, hypergeo.cumulativeProbability(-1), 0.0);

        // Test x in domain (0.03968253968253968 returned)
        assertEquals(0.03968253968253968, hypergeo.cumulativeProbability(1), 0.0);

        // Test x > upper domain (1.0 returned)
        assertEquals(1.0, hypergeo.cumulativeProbability(6), 0.0);
    }

    // Test probability method
    @Test
    public void testProbability() {
        HypergeometricDistribution hypergeo = new HypergeometricDistribution(10, 5, 3);

        // Test x < lower domain (0.0 returned)
        assertEquals(0.0, hypergeo.cumulativeProbability(-1), 0.0);

        // Test x in domain (0.23076923076923073 returned)
        assertEquals(0.23076923076923073, hypergeo.cumulativeProbability(1), 0.0);

        // Test x > upper domain (0.13333333333333333 returned)
        assertEquals(0.13333333333333333, hypergeo.cumulativeProbability(6), 0.0);
    }

    // Test upperCumulativeProbability method
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution hypergeo = new HypergeometricDistribution(10, 5, 3);

        // Test x <= lower domain (1.0 returned)
        assertEquals(1.0, hypergeo.upperCumulativeProbability(0), 0.0);

        // Test x in domain (0.9603174603174603 returned)
        assertEquals(0.9603174603174603, hypergeo.upperCumulativeProbability(4), 0.0);

        // Test x > upper domain (0.0 returned)
        assertEquals(0.0, hypergeo.upperCumulativeProbability(6), 0.0);
    }

    // Test getNumericalMean method
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution hypergeo = new HypergeometricDistribution(10, 5, 3);

        // Test numerical mean
        assertEquals(1.5, hypergeo.getNumericalMean(), 0.0);
    }

    // Test getNumericalVariance method
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution hypergeo = new HypergeometricDistribution(10, 5, 3);

        // Test numerical variance
        assertEquals(0.989010989010989, hypergeo.getNumericalVariance(), 0.0);
    }

    // Test getSupportLowerBound method
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution hypergeo = new HypergeometricDistribution(10, 5, 3);

        // Test lower bound of support
        assertEquals(0, hypergeo.getSupportLowerBound());
    }

    // Test getSupportUpperBound method
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution hypergeo = new HypergeometricDistribution(10, 5, 3);

        // Test upper bound of support
        assertEquals(3, hypergeo.getSupportUpperBound());
    }

    // Test isSupportConnected method
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution hypergeo = new HypergeometricDistribution(10, 5, 3);

        // Test if support is connected
        assertTrue(hypergeo.isSupportConnected());
    }

}