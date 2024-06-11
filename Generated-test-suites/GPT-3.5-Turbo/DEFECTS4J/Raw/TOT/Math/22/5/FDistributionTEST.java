import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class FDistributionTest {

    private FDistribution fDist;

    @Before
    public void setUp() {
        try {
            fDist = new FDistribution(3, 5);
        } catch (NotStrictlyPositiveException e) {
            e.printStackTrace();
        }
    }

    // density test cases
    @Test
    public void testDensity_typicalCase() {
        double density = fDist.density(1.0);
        assertEquals(0.337619, density, 0.000001);
    }

    @Test
    public void testDensity_edgeCase() {
        double density = fDist.density(0.0);
        assertEquals(0.0, density, 0.000001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testDensity_errorCase() throws NotStrictlyPositiveException {
        FDistribution fDistWithError = new FDistribution(-3, 5);
        double density = fDistWithError.density(1.0);
    }

    // cumulativeProbability test cases
    @Test
    public void testCumulativeProbability_typicalCase() {
        double cumulativeProbability = fDist.cumulativeProbability(2.0);
        assertEquals(0.854103, cumulativeProbability, 0.000001);
    }

    @Test
    public void testCumulativeProbability_edgeCase() {
        double cumulativeProbability = fDist.cumulativeProbability(0.0);
        assertEquals(0.0, cumulativeProbability, 0.000001);
    }

    // getNumeratorDegreesOfFreedom test case
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = fDist.getNumeratorDegreesOfFreedom();
        assertEquals(3.0, numeratorDegreesOfFreedom, 0.000001);
    }

    // getDenominatorDegreesOfFreedom test case
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        double denominatorDegreesOfFreedom = fDist.getDenominatorDegreesOfFreedom();
        assertEquals(5.0, denominatorDegreesOfFreedom, 0.000001);
    }

    // getNumericalMean test case
    @Test
    public void testGetNumericalMean_typicalCase() {
        double numericalMean = fDist.getNumericalMean();
        assertEquals(1.25, numericalMean, 0.000001);
    }

    @Test
    public void testGetNumericalMean_edgeCase() {
        try {
            FDistribution fDistEdge = new FDistribution(2, 5);
            double numericalMean = fDistEdge.getNumericalMean();
            assertEquals(Double.NaN, numericalMean, 0.000001);
        } catch (NotStrictlyPositiveException e) {
            e.printStackTrace();
        }
    }

    // getNumericalVariance test case
    @Test
    public void testGetNumericalVariance_typicalCase() {
        double numericalVariance = fDist.getNumericalVariance();
        assertEquals(4.166666, numericalVariance, 0.000001);
    }

    @Test
    public void testGetNumericalVariance_edgeCase() {
        try {
            FDistribution fDistEdge = new FDistribution(2, 4);
            double numericalVariance = fDistEdge.getNumericalVariance();
            assertEquals(Double.NaN, numericalVariance, 0.000001);
        } catch (NotStrictlyPositiveException e) {
            e.printStackTrace();
        }
    }

    // getSupportLowerBound test case
    @Test
    public void testGetSupportLowerBound() {
        double supportLowerBound = fDist.getSupportLowerBound();
        assertEquals(0.0, supportLowerBound, 0.000001);
    }

    // getSupportUpperBound test case
    @Test
    public void testGetSupportUpperBound() {
        double supportUpperBound = fDist.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, supportUpperBound, 0.000001);
    }

    // isSupportLowerBoundInclusive test case
    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(fDist.isSupportLowerBoundInclusive());
    }

    // isSupportUpperBoundInclusive test case
    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertFalse(fDist.isSupportUpperBoundInclusive());
    }

    // isSupportConnected test case
    @Test
    public void testIsSupportConnected() {
        assertTrue(fDist.isSupportConnected());
    }

}