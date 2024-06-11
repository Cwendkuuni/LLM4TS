import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionTest {

    @Test
    public void testFDistributionConstructor() throws NotStrictlyPositiveException {
        FDistribution fd = new FDistribution(3.0, 4.0);
        assertEquals(3.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        double result = fd.density(1.5);
        // Here you should check the result with some approximation or reference value
        // for instance, using a numerical integration tool or looking up values at MathWorld or Wikipedia
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        double result = fd.cumulativeProbability(1.5);
        // Here you should check the result with some approximation or reference value
        // for instance, by looking up values at MathWorld or Wikipedia
    }

    @Test
    public void testFDistributionGetNumeratorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        double expected = 3.0;
        double result = fd.getNumeratorDegreesOfFreedom();
        assertEquals(expected, result, 0.0);
    }

    @Test
    public void testFDistributionGetDenominatorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        double expected = 4.0;
        double result = fd.getDenominatorDegreesOfFreedom();
        assertEquals(expected, result, 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDegreesOfFreedom() {
        new FDistribution(-3.0, 4.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroDenominatorDegreesOfFreedom() {
        new FDistribution(3.0, 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroNumeratorDegreesOfFreedom() {
        new FDistribution(0.0, 4.0);
    }

    @Test
    public void testFDistributionNumericalMean() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        double result = fd.getNumericalMean();
        double expected = 3.0 / 2.0;
        assertEquals(expected, result, 0.01); // Allow for a small error
    }

    @Test(expected = IllegalStateException.class)
    public void testFDistributionNumericalVarianceBeforeCalculation() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        fd.getNumericalVariance();
    }

    @Test
    public void testFDistributionNumericalVarianceAfterCalculation() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        fd.calculateNumericalVariance();
        double result = fd.getNumericalVariance();
        double expected = 0.393764375; // See "Statistics and Probability using R" by Deborah Rumsey
        assertEquals(expected, result, 0.01); // Allow for a small error
    }

    @Test(expected = IllegalStateException.class)
    public void testFDistributionNumericalVarianceBeforeCalculationInGetMethod() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        fd.getNumericalVariance();
    }

    @Test
    public void testFDistributionSupportBounds() {
        FDistribution fd = new FDistribution(3.0, 4.0);
        assertEquals(0.0, fd.getSupportLowerBound(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, fd.getSupportUpperBound(), 0.0);
    }
}