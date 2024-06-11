import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FDistributionTest {

    private static final double DELTA = 1e-10;
    private static final double EPSILON = 1e-15;

    private FDistribution distribution;

    @Before
    public void setUp() throws NotStrictlyPositiveException {
        distribution = new FDistribution(3, 4);
    }

    @Test
    public void testConstruction() {
        Assert.assertEquals(3, distribution.getNumeratorDegreesOfFreedom(), DELTA);
        Assert.assertEquals(4, distribution.getDenominatorDegreesOfFreedom(), DELTA);
        Assert.assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), DELTA);
    }

    @Test
    public void testDensity() {
        Assert.assertEquals(0.003426224805311337, distribution.density(1.5), EPSILON);
        Assert.assertEquals(0.0, distribution.density(2.5), DELTA);
        Assert.assertEquals(Double.POSITIVE_INFINITY, distribution.density(Double.POSITIVE_INFINITY), DELTA);
    }

    @Test
    public void testCumulativeProbability() {
        Assert.assertEquals(0.1378928741282576, distribution.cumulativeProbability(1.5), EPSILON);
        Assert.assertEquals(0.5, distribution.cumulativeProbability(3), EPSILON);
        Assert.assertEquals(1.0, distribution.cumulativeProbability(Double.POSITIVE_INFINITY), DELTA);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        Assert.assertEquals(3, distribution.getNumeratorDegreesOfFreedom(), DELTA);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        Assert.assertEquals(4, distribution.getDenominatorDegreesOfFreedom(), DELTA);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        Assert.assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), DELTA);
    }

    @Test
    public void testGetNumericalMean() {
        Assert.assertEquals(3.0 / 2, distribution.getNumericalMean(), DELTA);
    }

    @Test
    public void testGetNumericalVariance() {
        Assert.assertEquals(0.3475221135825829, distribution.getNumericalVariance(), DELTA);
    }

    @Test
    public void testGetSupportLowerBound() {
        Assert.assertEquals(0.0, distribution.getSupportLowerBound(), DELTA);
    }

    @Test
    public void testGetSupportUpperBound() {
        Assert.assertEquals(Double.POSITIVE_INFINITY, distribution.getSupportUpperBound(), DELTA);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        Assert.assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        Assert.assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        Assert.assertTrue(distribution.isSupportConnected());
    }
}