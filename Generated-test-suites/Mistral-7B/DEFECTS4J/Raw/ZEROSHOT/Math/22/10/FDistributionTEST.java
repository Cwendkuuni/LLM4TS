import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testFDistributionCreation() throws NotStrictlyPositiveException {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        RealDistribution dist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, dist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionCreationNegativeDegreesOfFreedom() throws NotStrictlyPositiveException {
        double numeratorDegreesOfFreedom = -3.0;
        double denominatorDegreesOfFreedom = 5.0;
        new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    @Test
    public void testFDistributionDensity() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        RealDistribution dist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double x = 2.4;
        double expectedValue = 0.3151352352272413;
        double actualValue = dist.density(x);
        assertEquals(expectedValue, actualValue, 0.0000000000001);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        RealDistribution dist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double x = 2.4;
        double expectedValue = 0.911624728292253;
        double actualValue = dist.cumulativeProbability(x);
        assertEquals(expectedValue, actualValue, 0.0000000000001);
    }

    @Test
    public void testFDistributionMean() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        RealDistribution dist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double expectedValue = 3.0 / 2.0;
        double actualValue = dist.getNumericalMean();
        assertEquals(expectedValue, actualValue, 0.0000000000001);
    }

    @Test
    public void testFDistributionVariance() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        RealDistribution dist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double expectedValue = (3.0 * 3.0 * 5.0 * 5.0) / (3.0 * 5.0 * 5.0 * 3.0 * 2.0 * 2.0 * 5.0 * 5.0 * 3.0);
        double actualValue = dist.getNumericalVariance();
        assertEquals(expectedValue, actualValue, 0.0000000000001);
    }

    @Test
    public void testFDistributionSupportBounds() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        RealDistribution dist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, dist.getSupportUpperBound(), 0.0);
        assertEquals(true, dist.isSupportLowerBoundInclusive());
        assertEquals(false, dist.isSupportUpperBoundInclusive());
        assertEquals(true, dist.isSupportConnected());
    }
}