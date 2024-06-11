import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(1.0, 2.0);
        double x = 1.5;
        double expected = Beta.regularizedBeta(1.5, 0.5, 0.5);
        double result = distribution.cumulativeProbability(x);
        assertEquals(expected, result, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegativeX() throws MathException {
        FDistribution distribution = new FDistributionImpl(1.0, 2.0);
        double x = -1.0;
        distribution.cumulativeProbability(x);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNonPositiveDegreesOfFreedom() throws MathException {
        FDistribution distribution = new FDistributionImpl(-1.0, 2.0);
    }

    // Add other test cases for inverseCumulativeProbability, setNumeratorDegreesOfFreedom and setDenominatorDegreesOfFreedom here
}