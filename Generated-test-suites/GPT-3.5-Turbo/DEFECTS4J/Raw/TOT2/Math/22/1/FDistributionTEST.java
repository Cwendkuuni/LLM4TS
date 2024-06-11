import static org.junit.Assert.*;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(3.0, 4.0);
        assertEquals(0.0, fDistribution.density(-1.0), 0.0001);
        // Add more test cases for density method here
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(3.0, 4.0);
        assertEquals(0.0, fDistribution.cumulativeProbability(-1.0), 0.0001);
        // Add more test cases for cumulativeProbability method here
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(3.0, 4.0);
        assertEquals(Double.NaN, fDistribution.getNumericalMean(), 0.0001);
        // Add more test cases for getNumericalMean method here
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(3.0, 4.0);
        assertEquals(Double.NaN, fDistribution.getNumericalVariance(), 0.0001);
        // Add more test cases for getNumericalVariance method here
    }

    // Add more test cases for other methods in FDistribution class here

}