import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionTest;
import org.junit.Test;

public class FDistributionTest extends FDistribution {
    @Test
    public void testFDistribution_DefaultConstructor() {
        FDistribution fDist = new FDistribution();
        FDistributionTest.assertDistribution(fDist, 1.0, 1.0);
    }

    @Test
    public void testFDistribution_Constructor_ValidParameters() {
        FDistribution fDist = new FDistribution(2.5, 3.5);
        FDistributionTest.assertDistribution(fDist, 2.5, 3.5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistribution_Constructor_InvalidParameters() {
        FDistribution fDist = new FDistribution(-1.0, 3.5);
        // The constructor should throw an exception
    }

    @Test
    public void testFDistribution_NumericalMean() {
        FDistribution fDist = new FDistribution(3.5, 2.5);
        double mean = fDist.getNumericalMean();
        FDistributionTest.assertNumericalMean(mean, 3.5, 2.5);
    }

    @Test
    public void testFDistribution_NumericalVariance() {
        FDistribution fDist = new FDistribution(4.5, 3.5);
        double variance = fDist.getNumericalVariance();
        FDistributionTest.assertNumericalVariance(variance, 4.5, 3.5);
    }

    @Test
    public void testFDistribution_Density() {
        FDistribution fDist = new FDistribution(2.5, 3.5);
        double density = fDist.density(1.5);
        FDistributionTest.assertDensity(density, 2.5, 3.5, 1.5);
    }

    @Test
    public void testFDistribution_CumulativeProbability() {
        FDistribution fDist = new FDistribution(1.5, 2.5);
        double prob = fDist.cumulativeProbability(1.25);
        FDistributionTest.assertProbability(prob, 1.5, 2.5, 1.25);
    }

    // Add more test cases for other methods and edge cases
}