import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class FDistributionTest {

    private static final double EPSILON = 1e-9;
    private FDistribution distribution;

    @Before
    public void setUp() {
        distribution = new FDistribution(3, 4);
    }

    @Test
    public void testConstructor() {
        assertEquals(3, distribution.getNumeratorDegreesOfFreedom(), EPSILON);
        assertEquals(4, distribution.getDenominatorDegreesOfFreedom(), EPSILON);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidNumeratorDegreesOfFreedom() {
        new FDistribution(0, 4);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidDenominatorDegreesOfFreedom() {
        new FDistribution(3, 0);
    }

    @Test
    public void testDensity() {
        assertEquals(0.0, distribution.density(0), EPSILON);
        assertEquals(0.004833070439944043, distribution.density(1), EPSILON);
        assertEquals(0.07257046054288957, distribution.density(2.5), EPSILON);
        assertEquals(0.19290398129424714, distribution.density(5), EPSILON);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.0, distribution.cumulativeProbability(0), EPSILON);
        assertEquals(0.36363636363636365, distribution.cumulativeProbability(1), EPSILON);
        assertEquals(0.6038961038961039, distribution.cumulativeProbability(2.5), EPSILON);
        assertEquals(0.8529411764705882, distribution.cumulativeProbability(5), EPSILON);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(1.6, distribution.getNumericalMean(), EPSILON);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(13.333333333333336, distribution.getNumericalVariance(), EPSILON);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0.0, distribution.getSupportLowerBound(), EPSILON);
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(Double.POSITIVE_INFINITY, distribution.getSupportUpperBound(), EPSILON);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }
    
    @Test
    public void testRandomSampling() {
        double x = distribution.sample();
        assertTrue(x >= distribution.getSupportLowerBound());
        assertTrue(x <= distribution.getSupportUpperBound());
    }
}