import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Before;
import org.junit.Test;

public class FDistributionTest {

    private static final double EPSILON = 1e-9;
    private FDistribution fDistribution;

    @Before
    public void setUp() {
        fDistribution = new FDistribution(4, 8);
    }

    @Test
    public void testConstructorWithValidArguments() {
        assertEquals(4, fDistribution.getNumeratorDegreesOfFreedom(), EPSILON);
        assertEquals(8, fDistribution.getDenominatorDegreesOfFreedom(), EPSILON);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidNumeratorDegreesOfFreedom() {
        new FDistribution(0, 8);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidDenominatorDegreesOfFreedom() {
        new FDistribution(4, 0);
    }

    @Test
    public void testDensity() {
        double density = fDistribution.density(0.5);
        assertEquals(0.21875, density, EPSILON);

        density = fDistribution.density(1);
        assertEquals(0.18359375, density, EPSILON);
    }

    @Test
    public void testCumulativeProbability() {
        double cumulativeProbability = fDistribution.cumulativeProbability(0.5);
        assertEquals(0.11821086262732848, cumulativeProbability, EPSILON);

        cumulativeProbability = fDistribution.cumulativeProbability(1);
        assertEquals(0.20937716358359817, cumulativeProbability, EPSILON);
    }

    @Test
    public void testGetNumericalMean() {
        double numericalMean = fDistribution.getNumericalMean();
        assertEquals(1.3333333333333333, numericalMean, EPSILON);
    }

    @Test
    public void testGetNumericalVariance() {
        double numericalVariance = fDistribution.getNumericalVariance();
        assertEquals(1.7777777777777777, numericalVariance, EPSILON);
    }

    @Test
    public void testGetSupportLowerBound() {
        double supportLowerBound = fDistribution.getSupportLowerBound();
        assertEquals(0, supportLowerBound, EPSILON);
    }

    @Test
    public void testGetSupportUpperBound() {
        double supportUpperBound = fDistribution.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, supportUpperBound, EPSILON);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertFalse(fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(fDistribution.isSupportConnected());
    }

}