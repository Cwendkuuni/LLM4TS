import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class FDistributionTest {

    private FDistribution distribution;

    @Before
    public void setUp() throws NotStrictlyPositiveException {
        distribution = new FDistribution(2.0, 3.0);
    }

    @Test
    public void testConstructorWithValidArguments() {
        assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidNumeratorDegreesOfFreedom() {
        new FDistribution(-1.0, 3.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidDenominatorDegreesOfFreedom() {
        new FDistribution(2.0, -1.0);
    }

    @Test
    public void testDensity() {
        double expected = 0.1371317390258151;
        double actual = distribution.density(1.5);
        assertEquals(expected, actual, 0.00000001);
    }

    @Test
    public void testCumulativeProbability() {
        double expected = 0.3183486834777452;
        double actual = distribution.cumulativeProbability(1.5);
        assertEquals(expected, actual, 0.00000001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        double expected = 2.0;
        double actual = distribution.getNumeratorDegreesOfFreedom();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        double expected = 3.0;
        double actual = distribution.getDenominatorDegreesOfFreedom();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        double expected = 1e-9;
        double actual = distribution.getSolverAbsoluteAccuracy();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        double expected = 1.5;
        double actual = distribution.getNumericalMean();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        double expected = 0.6611875837618093;
        double actual = distribution.getNumericalVariance();
        assertEquals(expected, actual, 0.00000001);
    }

    @Test
    public void testGetSupportLowerBound() {
        double expected = 0.0;
        double actual = distribution.getSupportLowerBound();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        double expected = Double.POSITIVE_INFINITY;
        double actual = distribution.getSupportUpperBound();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        boolean expected = true;
        boolean actual = distribution.isSupportLowerBoundInclusive();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        boolean expected = false;
        boolean actual = distribution.isSupportUpperBoundInclusive();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsSupportConnected() {
        boolean expected = true;
        boolean actual = distribution.isSupportConnected();
        assertEquals(expected, actual);
    }

    @Test
    public void testCalculateNumericalVariance() {
        FDistribution distributionWithDifferentParameters = new FDistribution(3.0, 5.0);
        assertEquals(1.439297143245361, distributionWithDifferentParameters.calculateNumericalVariance(), 0.00000001);
    }
}