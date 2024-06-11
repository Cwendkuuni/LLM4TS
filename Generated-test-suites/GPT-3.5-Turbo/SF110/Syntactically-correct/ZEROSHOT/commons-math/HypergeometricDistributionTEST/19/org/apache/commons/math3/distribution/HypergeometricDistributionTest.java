import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

public class HypergeometricDistributionTest {
    
    @Test
    public void testConstructor() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertEquals(10, dist.getPopulationSize());
            assertEquals(5, dist.getNumberOfSuccesses());
            assertEquals(3, dist.getSampleSize());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeNumberOfSuccesses() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, -5, 3);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeSampleSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, -3);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorNumberOfSuccessesLargerThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 15, 3);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorSampleSizeLargerThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 15);
    }
    
    @Test
    public void testCumulativeProbability() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertEquals(0.00303, dist.cumulativeProbability(2), 0.00001);
            assertEquals(0.00606, dist.cumulativeProbability(3), 0.00001);
            assertEquals(0.00909, dist.cumulativeProbability(4), 0.00001);
            assertEquals(0.01212, dist.cumulativeProbability(5), 0.00001);
            assertEquals(0.06061, dist.cumulativeProbability(10), 0.00001);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetDomain() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            int[] domain = dist.getDomain(10, 5, 3);
            assertEquals(2, domain[0]);
            assertEquals(3, domain[1]);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetLowerDomain() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            int lowerDomain = dist.getLowerDomain(10, 5, 3);
            assertEquals(2, lowerDomain);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertEquals(5, dist.getNumberOfSuccesses());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetPopulationSize() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertEquals(10, dist.getPopulationSize());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetSampleSize() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertEquals(3, dist.getSampleSize());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetUpperDomain() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            int upperDomain = dist.getUpperDomain(5, 3);
            assertEquals(3, upperDomain);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testProbability() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertEquals(0.16667, dist.probability(2), 0.00001);
            assertEquals(0.20000, dist.probability(3), 0.00001);
            assertEquals(0.16667, dist.probability(4), 0.00001);
            assertEquals(0.08333, dist.probability(5), 0.00001);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertEquals(0.93939, dist.upperCumulativeProbability(2), 0.00001);
            assertEquals(0.93396, dist.upperCumulativeProbability(3), 0.00001);
            assertEquals(0.93030, dist.upperCumulativeProbability(4), 0.00001);
            assertEquals(0.92761, dist.upperCumulativeProbability(5), 0.00001);
            assertEquals(0.39394, dist.upperCumulativeProbability(10), 0.00001);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testInnerCumulativeProbability() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            double innerCumulativeProbability = dist.innerCumulativeProbability(4, 7, 1);
            assertEquals(0.24956, innerCumulativeProbability, 0.00001);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetNumericalMean() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertEquals(1.5, dist.getNumericalMean(), 0.00001);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetNumericalVariance() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertEquals(0.86667, dist.getNumericalVariance(), 0.00001);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testCalculateNumericalVariance() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            double calculateNumericalVariance = dist.calculateNumericalVariance();
            assertEquals(0.86667, calculateNumericalVariance, 0.00001);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetSupportLowerBound() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertEquals(0, dist.getSupportLowerBound());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetSupportUpperBound() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertEquals(3, dist.getSupportUpperBound());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testIsSupportConnected() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertTrue(dist.isSupportConnected());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }

}