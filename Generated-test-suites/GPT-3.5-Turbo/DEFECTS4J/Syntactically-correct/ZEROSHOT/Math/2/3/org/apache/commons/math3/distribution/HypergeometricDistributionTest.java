import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.*;

public class HypergeometricDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testPopulationSizeNegative() {
        new HypergeometricDistribution(-1, 4, 2);
    }

    @Test
    public void testPopulationSizePositive() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 4, 2);
        assert hd.getPopulationSize() == 10;
    }

    @Test(expected = NotPositiveException.class)
    public void testNumberOfSuccessesNegative() {
        new HypergeometricDistribution(10, -1, 2);
    }

    @Test
    public void testNumberOfSuccessesPositive() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 4, 2);
        assert hd.getNumberOfSuccesses() == 4;
    }

    @Test(expected = NotPositiveException.class)
    public void testSampleSizeNegative() {
        new HypergeometricDistribution(10, 4, -1);
    }

    @Test
    public void testSampleSizePositive() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 4, 2);
        assert hd.getSampleSize() == 2;
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(4, 5, 2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(10, 4, 12);
    }

    @Test
    public void testCumulativeProbabilityInsideDomain() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 4, 2);
        assert hd.cumulativeProbability(1) == 0.3571428571428571;
    }

    @Test
    public void testCumulativeProbabilityBelowDomain() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 4, 2);
        assert hd.cumulativeProbability(0) == 0.0;
    }

    @Test
    public void testCumulativeProbabilityAboveDomain() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 4, 2);
        assert hd.cumulativeProbability(5) == 1.0;
    }

    @Test
    public void testProbabilityInsideDomain() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 4, 2);
        assert hd.probability(1) == 0.3571428571428571;
    }

    @Test
    public void testProbabilityBelowDomain() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 4, 2);
        assert hd.probability(0) == 0.0;
    }

    @Test
    public void testProbabilityAboveDomain() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 4, 2);
        assert hd.probability(5) == 0.0;
    }

    @Test
    public void testUpperCumulativeProbabilityInsideDomain() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 4, 2);
        assert hd.upperCumulativeProbability(1) == 0.6428571428571429;
    }

    @Test
    public void testUpperCumulativeProbabilityBelowDomain() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 4, 2);
        assert hd.upperCumulativeProbability(0) == 1.0;
    }

    @Test
    public void testUpperCumulativeProbabilityAboveDomain() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 4, 2);
        assert hd.upperCumulativeProbability(5) == 0.0;
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 4, 2);
        assert hd.getNumericalMean() == 0.8;
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 4, 2);
        assert hd.getNumericalVariance() == 0.09591836734693878;
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 4, 2);
        assert hd.getSupportLowerBound() == 0;
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 4, 2);
        assert hd.getSupportUpperBound() == 2;
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 4, 2);
        assert hd.isSupportConnected() == true;
    }
}