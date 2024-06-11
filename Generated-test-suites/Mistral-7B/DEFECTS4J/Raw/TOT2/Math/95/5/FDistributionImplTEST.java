import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl_constructor() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testCumulativeProbability_validInput() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        double result = distribution.cumulativeProbability(2.5);
        double expected = Beta.regularizedBeta((2.0 * 2.5) / (3.0 + 2.0 * 2.5),
                0.5 * 2.0, 0.5 * 3.0);
        assertEquals(expected, result, 0.001);
    }

    @Test
    public void testInverseCumulativeProbability_validInput() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        double p = 0.9;
        double result = distribution.inverseCumulativeProbability(p);
        // Manually calculate the inverse for comparison
        double x = 1.0 / (Beta.regularizedIncompleteBeta(p, 0.5 * 2.0, 0.5 * 3.0) /
                Beta.beta(0.5 * 2.0, 0.5 * 3.0));
        assertEquals(x, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom_negativeValue() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        distribution.setNumeratorDegreesOfFreedom(-2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom_negativeValue() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        distribution.setDenominatorDegreesOfFreedom(-2.0);
    }

    // Add more test cases for other methods and edge cases here
}