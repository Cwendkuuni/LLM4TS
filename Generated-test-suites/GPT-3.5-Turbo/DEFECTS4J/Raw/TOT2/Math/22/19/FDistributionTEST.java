import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(3, 5);
        double density = fDistribution.density(2.0);
        
        // Add assertions here to check the density value
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(3, 5);
        double cumulativeProbability = fDistribution.cumulativeProbability(1.5);
        
        // Add assertions here to check the cumulative probability value
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(3, 5);
        double numericalMean = fDistribution.getNumericalMean();
        
        // Add assertions here to check the numerical mean value
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(3, 5);
        double numericalVariance = fDistribution.getNumericalVariance();
        
        // Add assertions here to check the numerical variance value
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidDegreesOfFreedom() {
        new FDistribution(0, 5);
        // If the exception is not thrown, the test will fail
    }
}