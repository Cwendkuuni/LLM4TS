import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {
    
    @Test
    public void testConstructorValidInput() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertEquals(10, dist.getPopulationSize());
            assertEquals(5, dist.getNumberOfSuccesses());
            assertEquals(3, dist.getSampleSize());
        } catch (Exception e) {
            fail("Exception thrown for valid input");
        }
    }
    
    @Test
    public void testConstructorNegativePopulationSize() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(-10, 5, 3);
            fail("Exception not thrown for negative population size");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Incorrect exception thrown for negative population size");
        }
    }
    
    @Test
    public void testConstructorNegativeNumberOfSuccesses() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, -5, 3);
            fail("Exception not thrown for negative number of successes");
        } catch (NotPositiveException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Incorrect exception thrown for negative number of successes");
        }
    }
    
    @Test
    public void testConstructorNegativeSampleSize() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, -3);
            fail("Exception not thrown for negative sample size");
        } catch (NotPositiveException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Incorrect exception thrown for negative sample size");
        }
    }
    
    @Test
    public void testConstructorNumberOfSuccessesLargerThanPopulationSize() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 15, 3);
            fail("Exception not thrown for number of successes larger than population size");
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Incorrect exception thrown for number of successes larger than population size");
        }
    }
    
    @Test
    public void testConstructorSampleSizeLargerThanPopulationSize() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 15);
            fail("Exception not thrown for sample size larger than population size");
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Incorrect exception thrown for sample size larger than population size");
        }
    }
    
    @Test
    public void testCumulativeProbabilityValidInput() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            double prob = dist.cumulativeProbability(2);
            assertEquals(0.26666666666666666, prob, 0.0001);
        } catch (Exception e) {
            fail("Exception thrown for valid input");
        }
    }
    
    @Test
    public void testCumulativeProbabilityOutOfBounds() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            double prob = dist.cumulativeProbability(15);
            assertEquals(1.0, prob, 0.0001);
        } catch (Exception e) {
            fail("Exception thrown for out of bounds input");
        }
    }
    
    @Test
    public void testProbabilityValidInput() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            double prob = dist.probability(2);
            assertEquals(0.4, prob, 0.0001);
        } catch (Exception e) {
            fail("Exception thrown for valid input");
        }
    }
    
    @Test
    public void testProbabilityOutOfBounds() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            double prob = dist.probability(15);
            assertEquals(0.0, prob, 0.0001);
        } catch (Exception e) {
            fail("Exception thrown for out of bounds input");
        }
    }
    
    @Test
    public void testUpperCumulativeProbabilityValidInput() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            double prob = dist.upperCumulativeProbability(6);
            assertEquals(0.06666666666666667, prob, 0.0001);
        } catch (Exception e) {
            fail("Exception thrown for valid input");
        }
    }
    
    @Test
    public void testUpperCumulativeProbabilityOutOfBounds() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            double prob = dist.upperCumulativeProbability(0);
            assertEquals(1.0, prob, 0.0001);
        } catch (Exception e) {
            fail("Exception thrown for out of bounds input");
        }
    }
    
    @Test
    public void testGetNumericalMean() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            double mean = dist.getNumericalMean();
            assertEquals(1.5, mean, 0.0001);
        } catch (Exception e) {
            fail("Exception thrown for valid input");
        }
    }
    
    @Test
    public void testGetNumericalVariance() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            double variance = dist.getNumericalVariance();
            assertEquals(0.6388888888888888, variance, 0.0001);
        } catch (Exception e) {
            fail("Exception thrown for valid input");
        }
    }
    
    @Test
    public void testGetSupportLowerBound() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            int lowerBound = dist.getSupportLowerBound();
            assertEquals(0, lowerBound);
        } catch (Exception e) {
            fail("Exception thrown for valid input");
        }
    }
    
    @Test
    public void testGetSupportUpperBound() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            int upperBound = dist.getSupportUpperBound();
            assertEquals(3, upperBound);
        } catch (Exception e) {
            fail("Exception thrown for valid input");
        }
    }
    
    @Test
    public void testIsSupportConnected() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            boolean isConnected = dist.isSupportConnected();
            assertTrue(isConnected);
        } catch (Exception e) {
            fail("Exception thrown for valid input");
        }
    }
}