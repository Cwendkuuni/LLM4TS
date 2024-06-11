import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    //1. Test the constructor with valid input values
    @Test
    public void testConstructorValidInput() {
        try {
            HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
            assertNotNull(hypergeometricDistribution);
        } catch (Exception e) {
            fail("An exception occurred when creating the HypergeometricDistribution object");
        }
    }

    //2. Test the constructor with a populationSize that is not positive
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(-10, 5, 2);
    }

    //3. Test the constructor with a numberOfSuccesses that is negative
    @Test(expected = NotPositiveException.class)
    public void testConstructorInvalidNumberOfSuccesses() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, -5, 2);
    }

    //4. Test the constructor with a sampleSize that is negative
    @Test(expected = NotPositiveException.class)
    public void testConstructorInvalidSampleSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, -2);
    }

    //5. Test the constructor with a numberOfSuccesses larger than the populationSize
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorNumberOfSuccessesLargerThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 15, 2);
    }

    //6. Test the constructor with a sampleSize larger than the populationSize
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorSampleSizeLargerThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 20);
    }

    //7. Test the cumulativeProbability method with a value less than the lower bound of the domain
    @Test
    public void testCumulativeProbabilityLowerBound() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.0, hypergeometricDistribution.cumulativeProbability(-1), 0);
    }

    //8. Test the cumulativeProbability method with a value greater than the upper bound of the domain
    @Test
    public void testCumulativeProbabilityUpperBound() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(1.0, hypergeometricDistribution.cumulativeProbability(10), 0);
    }

    //9. Test the getNumberOfSuccesses method
    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(5, hypergeometricDistribution.getNumberOfSuccesses());
    }

    //10. Test the getPopulationSize method
    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(10, hypergeometricDistribution.getPopulationSize());
    }

    //11. Test the getSampleSize method
    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(2, hypergeometricDistribution.getSampleSize());
    }

    //12. Test the probability method with a value less than the lower bound of the domain
    @Test
    public void testProbabilityLowerBound() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.0, hypergeometricDistribution.probability(-1), 0);
    }

    //13. Test the probability method with a value greater than the upper bound of the domain
    @Test
    public void testProbabilityUpperBound() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.0, hypergeometricDistribution.probability(12), 0);
    }

    //14. Test the upperCumulativeProbability method with a value less than or equal to the lower bound of the domain
    @Test
    public void testUpperCumulativeProbabilityLowerBound() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(1.0, hypergeometricDistribution.upperCumulativeProbability(0), 0);
    }

    //15. Test the upperCumulativeProbability method with a value greater than the upper bound of the domain
    @Test
    public void testUpperCumulativeProbabilityUpperBound() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.0, hypergeometricDistribution.upperCumulativeProbability(15), 0);
    }

    //16. Test the getNumericalMean method
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(1.0, hypergeometricDistribution.getNumericalMean(), 0);
    }

    //17. Test the getNumericalVariance method
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.11764705882352941, hypergeometricDistribution.getNumericalVariance(), 1e-10);
    }

    //18. Test the getSupportLowerBound method
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0, hypergeometricDistribution.getSupportLowerBound());
    }

    //19. Test the getSupportUpperBound method
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(2, hypergeometricDistribution.getSupportUpperBound());
    }

    //20. Test the isSupportConnected method
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 2);
        assertTrue(hypergeometricDistribution.isSupportConnected());
    }

}