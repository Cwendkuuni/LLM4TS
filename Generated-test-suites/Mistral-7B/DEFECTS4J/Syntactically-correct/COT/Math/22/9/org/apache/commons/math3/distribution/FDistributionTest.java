import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeDOFs() {
        try {
            new FDistribution(-1.0, 2.0);
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeDOFs2() {
        try {
            new FDistribution(2.0, -1.0);
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testDensity() {
        FDistribution dist = new FDistribution(5.0, 6.0);
        double[] xValues = {0.0, 0.5, 1.0, 2.0, 3.0, 10.0};
        double[] expectedDensities = {0.0, 0.11363114362922834, 0.2113273859388613, 0.3295117959586511, 0.3813733767312287, 0.0};

        for (int i = 0; i < xValues.length; i++) {
            double x = xValues[i];
            double density = dist.density(x);
            assertEquals(expectedDensities[i], density, 1e-8);
        }
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution dist = new FDistribution(5.0, 6.0);
        double[] xValues = {0.0, 0.5, 1.0, 2.0, 3.0, 4.0, 10.0};
        double[] expectedCPs = {0.0, 0.1279979665881237, 0.306988722941291, 0.501968669228146, 0.6744848565683957, 0.7821315588370297, 1.0};

        for (int i = 0; i < xValues.length; i++) {
            double x = xValues[i];
            double cp = dist.cumulativeProbability(x);
            assertEquals(expectedCPs[i], cp, 1e-8);
        }
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution dist = new FDistribution(5.0, 6.0);
        assertEquals(5.0, dist.getNumeratorDegreesOfFreedom(), 1e-8);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution dist = new FDistribution(5.0, 6.0);
        assertEquals(6.0, dist.getDenominatorDegreesOfFreedom(), 1e-8);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution dist = new FDistribution(5.0, 6.0);
        assertEquals(FDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, dist.getSolverAbsoluteAccuracy(), 1e-8);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution dist = new FDistribution(5.0, 6.0);
        assertEquals(6.111111111111111, dist.getNumericalMean(), 1e-8);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution dist = new FDistribution(5.0, 6.0);
        double numericalVariance = dist.getNumericalVariance();
        assertTrue(Precision.isClose(numericalVariance, 1.931221099885453, 1e-8));
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution dist = new FDistribution(5.0, 6.0);
        assertEquals(0.0, dist.getSupportLowerBound(), 1e-8);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution dist = new FDistribution(5.0, 6.0);
        assertEquals(Double.POSITIVE_INFINITY, dist.getSupportUpperBound(), 1e-8);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution dist = new FDistribution(5.0, 6.0);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution dist = new FDistribution(5.0, 6.0);
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution dist = new FDistribution(5.0, 6.0);
        assertTrue(dist.isSupportConnected());
    }
}