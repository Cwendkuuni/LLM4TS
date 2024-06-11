import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    // Test cases for constructor

    @Test
    public void testConstructorValidValues() {
        try {
            HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
            assertNotNull(hyperGeomDist);
        } catch (Exception e) {
            fail("Exception thrown for valid constructor parameters");
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativePopulationSize() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(-100, 50, 25);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroPopulationSize() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(0, 50, 25);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeNumberOfSuccesses() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, -50, 25);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeSampleSize() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, -25);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorNumberOfSuccessesGreaterThanPopulationSize() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 150, 25);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorSampleSizeGreaterThanPopulationSize() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 125);
    }


    // Test cases for cumulativeProbability()

    @Test
    public void testCumulativeProbabilityInsideDomain() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        double cumulativeProb = hyperGeomDist.cumulativeProbability(15);
        assertEquals(0.8092, cumulativeProb, 0.0001);
    }

    @Test
    public void testCumulativeProbabilityLowerBound() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        double cumulativeProb = hyperGeomDist.cumulativeProbability(0);
        assertEquals(0.0, cumulativeProb, 0.0001);
    }

    @Test
    public void testCumulativeProbabilityUpperBound() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        double cumulativeProb = hyperGeomDist.cumulativeProbability(25);
        assertEquals(1.0, cumulativeProb, 0.0001);
    }

    // Test cases for getDomain()

    @Test
    public void testGetDomain() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        int[] domain = hyperGeomDist.getDomain(100, 50, 25);
        assertEquals(25, domain[0]);
        assertEquals(50, domain[1]);
    }

    // Test cases for getLowerDomain()

    @Test
    public void testGetLowerDomainInsideDomain() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        int lowerDomain = hyperGeomDist.getLowerDomain(100, 50, 25);
        assertEquals(25, lowerDomain);
    }

    @Test
    public void testGetLowerDomainOutsideDomain() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        int lowerDomain = hyperGeomDist.getLowerDomain(100, 75, 25);
        assertEquals(50, lowerDomain);
    }

    // Test cases for getNumberOfSuccesses()

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        int numOfSuccesses = hyperGeomDist.getNumberOfSuccesses();
        assertEquals(50, numOfSuccesses);
    }

    // Test cases for getPopulationSize()

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        int populationSize = hyperGeomDist.getPopulationSize();
        assertEquals(100, populationSize);
    }

    // Test cases for getSampleSize()

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        int sampleSize = hyperGeomDist.getSampleSize();
        assertEquals(25, sampleSize);
    }

    // Test cases for getUpperDomain()

    @Test
    public void testGetUpperDomainInsideDomain() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        int upperDomain = hyperGeomDist.getUpperDomain(50, 25);
        assertEquals(25, upperDomain);
    }

    @Test
    public void testGetUpperDomainOutsideDomain() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        int upperDomain = hyperGeomDist.getUpperDomain(75, 25);
        assertEquals(25, upperDomain);
    }

    // Test cases for probability()

    @Test
    public void testProbabilityInsideDomain() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        double probability = hyperGeomDist.probability(15);
        assertEquals(0.2077, probability, 0.0001);
    }

    @Test
    public void testProbabilityOutsideDomain() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        double probability = hyperGeomDist.probability(30);
        assertEquals(0.0, probability, 0.0001);
    }

    // Test cases for upperCumulativeProbability()

    @Test
    public void testUpperCumulativeProbabilityInsideDomain() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        double upperCumulativeProb = hyperGeomDist.upperCumulativeProbability(15);
        assertEquals(0.1908, upperCumulativeProb, 0.0001);
    }

    @Test
    public void testUpperCumulativeProbabilityLowerBound() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        double upperCumulativeProb = hyperGeomDist.upperCumulativeProbability(0);
        assertEquals(1.0, upperCumulativeProb, 0.0001);
    }

    @Test
    public void testUpperCumulativeProbabilityUpperBound() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        double upperCumulativeProb = hyperGeomDist.upperCumulativeProbability(25);
        assertEquals(0.0, upperCumulativeProb, 0.0001);
    }

    // Test cases for innerCumulativeProbability()

    @Test
    public void testInnerCumulativeProbabilityForward() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        double innerCumulativeProb = hyperGeomDist.innerCumulativeProbability(20, 22, 1);
        assertEquals(0.1988, innerCumulativeProb, 0.0001);
    }

    @Test
    public void testInnerCumulativeProbabilityBackward() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        double innerCumulativeProb = hyperGeomDist.innerCumulativeProbability(22, 20, -1);
        assertEquals(0.2342, innerCumulativeProb, 0.0001);
    }

    // Test cases for getNumericalMean()

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        double numericalMean = hyperGeomDist.getNumericalMean();
        assertEquals(12.5, numericalMean, 0.0001);
    }

    // Test cases for getNumericalVariance()

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        double numericalVariance = hyperGeomDist.getNumericalVariance();
        assertEquals(2.0408163265306123, numericalVariance, 0.0001);
    }

    // Test cases for calculateNumericalVariance()

    @Test
    public void testCalculateNumericalVariance() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        double calculatedNumericalVariance = hyperGeomDist.calculateNumericalVariance();
        assertEquals(2.0408163265306123, calculatedNumericalVariance, 0.0001);
    }

    // Test cases for getSupportLowerBound()

    @Test
    public void testGetSupportLowerBoundInsideDomain() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        int supportLowerBound = hyperGeomDist.getSupportLowerBound();
        assertEquals(0, supportLowerBound);
    }

    @Test
    public void testGetSupportLowerBoundOutsideDomain() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        int supportLowerBound = hyperGeomDist.getSupportLowerBound();
        assertEquals(0, supportLowerBound);
    }

    // Test cases for getSupportUpperBound()

    @Test
    public void testGetSupportUpperBoundInsideDomain() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        int supportUpperBound = hyperGeomDist.getSupportUpperBound();
        assertEquals(25, supportUpperBound);
    }

    @Test
    public void testGetSupportUpperBoundOutsideDomain() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        int supportUpperBound = hyperGeomDist.getSupportUpperBound();
        assertEquals(25, supportUpperBound);
    }

    // Test cases for isSupportConnected()

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution hyperGeomDist = new HypergeometricDistribution(100, 50, 25);
        assertTrue(hyperGeomDist.isSupportConnected());
    }

}