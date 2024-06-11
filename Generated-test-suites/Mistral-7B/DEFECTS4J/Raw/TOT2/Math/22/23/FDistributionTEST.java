import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.test.AbstractRealDistributionTest;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionTest extends AbstractRealDistributionTest {

    @Test
    public void testFDistributionCreation() {
        try {
            FDistribution fd = new FDistribution(2.0, 3.0);
            assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
            assertEquals(3.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution fd = new FDistribution(2.0, 3.0);

        // Basic test
        double result = fd.density(1.5);
        double expected = 0.3222279465988903;
        assertEquals(expected, result, 1e-7);

        // Edge cases
        result = fd.density(0.0);
        expected = 0.0;
        assertEquals(expected, result, 1e-14);

        result = fd.density(Double.POSITIVE_INFINITY);
        expected = 0.0;
        assertEquals(expected, result, 1e-14);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution fd = new FDistribution(2.0, 3.0);

        // Basic test
        double result = fd.cumulativeProbability(1.5);
        double expected = 0.9162924793842257;
        assertEquals(expected, result, 1e-7);

        // Edge cases
        result = fd.cumulativeProbability(0.0);
        expected = 0.0;
        assertEquals(expected, result, 1e-14);

        result = fd.cumulativeProbability(Double.NEGATIVE_INFINITY);
        expected = 0.0;
        assertEquals(expected, result, 1e-14);

        result = fd.cumulativeProbability(1.0);
        expected = 0.5;
        assertEquals(expected, result, 1e-7);
    }

    @Test
    public void testFDistributionGetNumericalMean() {
        FDistribution fd = new FDistribution(2.0, 3.0);

        double result = fd.getNumericalMean();
        double expected = 1.5;
        assertEquals(expected, result, 1e-7);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFDistributionGetNumericalMeanWithWrongNumberOfArguments() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        fd.getNumericalMean(1);
    }

    @Test
    public void testFDistributionGetNumericalVariance() {
        FDistribution fd = new FDistribution(2.0, 3.0);

        double result = fd.getNumericalVariance();
        double expected = 0.787398565527443;
        assertEquals(expected, result, 1e-7);
    }

    @Test
    public void testFDistributionGetNumericalVarianceWithWrongNumberOfArguments() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        fd.getNumericalVariance(1);
    }
}