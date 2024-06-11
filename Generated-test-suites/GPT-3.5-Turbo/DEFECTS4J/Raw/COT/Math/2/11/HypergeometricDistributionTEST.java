import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import java.util.Arrays;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(100, -1, 50);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroPopulationSize() {
        new HypergeometricDistribution(0, 5, 2);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativePopulationSize() {
        new HypergeometricDistribution(-100, 5, 2);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeSampleSize() {
        new HypergeometricDistribution(100, 50, -1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(100, 200, 50);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(100, 50, 200);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 25);
        assert distribution.getNumericalMean() == 12.5;
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 25);
        assert distribution.getNumericalVariance() == 6.191919191919192;
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 25);
        assert distribution.getSupportLowerBound() == 25;
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 25);
        assert distribution.getSupportUpperBound() == 50;
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 25);
        assert distribution.isSupportConnected() == true;
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 25);
        assert distribution.cumulativeProbability(0) == 0.0;
        assert distribution.cumulativeProbability(12) == 0.1093488960481016;
        assert distribution.cumulativeProbability(25) == 0.4018899322976534;
        assert distribution.cumulativeProbability(50) == 1.0;
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 25);
        assert distribution.probability(-1) == 0.0;
        assert distribution.probability(12) == 0.06265664160401003;
        assert distribution.probability(25) == 0.1093488960481016;
        assert distribution.probability(50) == 0.0;
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 25);
        assert distribution.upperCumulativeProbability(0) == 1.0;
        assert distribution.upperCumulativeProbability(12) == 0.8906511039518984;
        assert distribution.upperCumulativeProbability(25) == 0.5981100677023466;
        assert distribution.upperCumulativeProbability(50) == 0.0;
    }
}