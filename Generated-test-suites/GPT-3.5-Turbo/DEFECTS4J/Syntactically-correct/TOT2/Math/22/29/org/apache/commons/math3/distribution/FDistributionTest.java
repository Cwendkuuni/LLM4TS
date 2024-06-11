import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(4, 6);
        assertEquals(0.0, fDistribution.density(0.0), 0.01);
        assertEquals(0.0809, fDistribution.density(0.5), 0.01);
        // Add more test cases for density method edge cases and scenarios
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(4, 6);
        assertEquals(0.0, fDistribution.cumulativeProbability(0.0), 0.01);
        assertEquals(0.055, fDistribution.cumulativeProbability(0.5), 0.01);
        // Add more test cases for cumulativeProbability method edge cases and scenarios
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(4, 6);
        assertEquals(2.0, fDistribution.getNumericalMean(), 0.01);
        // Add more test cases for getNumericalMean method edge cases and scenarios
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(4, 6);
        assertEquals(1.6, fDistribution.getNumericalVariance(), 0.01);
        // Add more test cases for getNumericalVariance method edge cases and scenarios
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidParameters() {
        new FDistribution(-2, 6);
    }

    // Add more test cases for other public methods in FDistribution class

}