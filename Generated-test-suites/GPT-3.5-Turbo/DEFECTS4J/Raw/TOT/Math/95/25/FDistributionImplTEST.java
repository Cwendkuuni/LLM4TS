import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

    private FDistributionImpl fDistribution;

    @Before
    public void setUp() {
        fDistribution = new FDistributionImpl(3, 4);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        double result = fDistribution.cumulativeProbability(2.5);
        assertEquals("Incorrect cumulative probability value", 0.7793249816824972, result, 1e-15);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double result = fDistribution.inverseCumulativeProbability(0.6);
        assertEquals("Incorrect inverse cumulative probability value", 2.0943935846872187, result, 1e-15);
    }

    @Test
    public void testInverseCumulativeProbabilityLowerBound() throws MathException {
        double result = fDistribution.inverseCumulativeProbability(0);
        assertEquals("Incorrect inverse cumulative probability value for lower bound", 0.0, result, 1e-15);
    }

    @Test
    public void testInverseCumulativeProbabilityUpperBound() throws MathException {
        double result = fDistribution.inverseCumulativeProbability(1);
        assertEquals("Incorrect inverse cumulative probability value for upper bound",
                Double.POSITIVE_INFINITY, result, 1e-15);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomWithInvalidValue() {
        fDistribution.setNumeratorDegreesOfFreedom(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomWithInvalidValue() {
        fDistribution.setDenominatorDegreesOfFreedom(0);
    }
}