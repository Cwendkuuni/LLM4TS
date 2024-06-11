import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FDistributionTest {

    private static final double EPSILON = 1e-9;

    private FDistribution f;

    @Before
    public void setUp() {
        f = new FDistribution(3, 2);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidNumeratorDegreesOfFreedom() {
        new FDistribution(0, 2);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidDenominatorDegreesOfFreedom() {
        new FDistribution(3, 0);
    }

    @Test
    public void testDensity() {
        Assert.assertEquals(0, f.density(-1), EPSILON);
        Assert.assertEquals(0, f.density(0), EPSILON);
        Assert.assertEquals(0.325832845, f.density(0.5), EPSILON);
        Assert.assertEquals(0.590728074, f.density(1), EPSILON);
    }

    @Test
    public void testCumulativeProbability() {
        Assert.assertEquals(0, f.cumulativeProbability(-1), EPSILON);
        Assert.assertEquals(0, f.cumulativeProbability(0), EPSILON);
        Assert.assertEquals(0.292893219, f.cumulativeProbability(0.5), EPSILON);
        Assert.assertEquals(0.381966011, f.cumulativeProbability(1), EPSILON);
        Assert.assertEquals(0.600410448, f.cumulativeProbability(2), EPSILON);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        Assert.assertEquals(3, f.getNumeratorDegreesOfFreedom(), EPSILON);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        Assert.assertEquals(2, f.getDenominatorDegreesOfFreedom(), EPSILON);
    }

    @Test
    public void testGetNumericalMean() {
        Assert.assertEquals(2, f.getNumericalMean(), EPSILON);
    }

    @Test
    public void testGetNumericalVariance() {
        Assert.assertEquals(8, f.getNumericalVariance(), EPSILON);
    }

    @Test
    public void testGetSupportLowerBound() {
        Assert.assertEquals(0, f.getSupportLowerBound(), EPSILON);
    }

    @Test
    public void testGetSupportUpperBound() {
        Assert.assertEquals(Double.POSITIVE_INFINITY, f.getSupportUpperBound(), EPSILON);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        Assert.assertTrue(f.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        Assert.assertFalse(f.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        Assert.assertTrue(f.isSupportConnected());
    }
}