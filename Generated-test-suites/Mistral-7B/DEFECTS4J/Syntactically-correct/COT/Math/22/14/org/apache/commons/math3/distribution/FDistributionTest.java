import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testFDistributionConstructor() throws NotStrictlyPositiveException {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;

        FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionConstructorNegativeParameters() throws NotStrictlyPositiveException {
        double numeratorDegreesOfFreedom = -1.0;
        double denominatorDegreesOfFreedom = -2.0;

        new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution fDistribution = new FDistribution(3.0, 4.0);

        double x = 1.5;
        double expectedDensity = 0.281125944945983;
        double actualDensity = fDistribution.density(x);

        assertEquals(expectedDensity, actualDensity, 0.000001);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(3.0, 4.0);

        double x = 1.5;
        double expectedCumulativeProbability = 0.931644232655688;
        double actualCumulativeProbability = fDistribution.cumulativeProbability(x);

        assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.000001);
    }

    @Test
    public void testFDistributionGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(3.0, 4.0);
        double expectedMean = 1.5;
        double actualMean = fDistribution.getNumericalMean();

        assertEquals(expectedMean, actualMean, 0.0);
    }

    @Test
    public void testFDistributionGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(3.0, 4.0);
        double expectedVariance = 0.378183238527935;
        double actualVariance = fDistribution.getNumericalVariance();

        assertEquals(expectedVariance, actualVariance, 0.0);
    }

    @Test
    public void testFDistributionGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(3.0, 4.0);
        double expectedLowerBound = 0.0;
        double actualLowerBound = fDistribution.getSupportLowerBound();

        assertEquals(expectedLowerBound, actualLowerBound, 0.0);
    }

    @Test
    public void testFDistributionGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(3.0, 4.0);
        double expectedUpperBound = Double.POSITIVE_INFINITY;
        double actualUpperBound = fDistribution.getSupportUpperBound();

        assertEquals(expectedUpperBound, actualUpperBound, 0.0);
    }

    @Test
    public void testFDistributionIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(3.0, 4.0);
        boolean expectedIsSupportLowerBoundInclusive = true;
        boolean actualIsSupportLowerBoundInclusive = fDistribution.isSupportLowerBoundInclusive();

        assertEquals(expectedIsSupportLowerBoundInclusive, actualIsSupportLowerBoundInclusive);
    }

    @Test
    public void testFDistributionIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(3.0, 4.0);
        boolean expectedIsSupportUpperBoundInclusive = false;
        boolean actualIsSupportUpperBoundInclusive = fDistribution.isSupportUpperBoundInclusive();

        assertEquals(expectedIsSupportUpperBoundInclusive, actualIsSupportUpperBoundInclusive);
    }

    @Test
    public void testFDistributionIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(3.0, 4.0);
        boolean expectedIsSupportConnected = true;
        boolean actualIsSupportConnected = fDistribution.isSupportConnected();

        assertEquals(expectedIsSupportConnected, actualIsSupportConnected);
    }
}