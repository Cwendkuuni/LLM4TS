import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertEquals(10, dist.getPopulationSize());
            assertEquals(5, dist.getNumberOfSuccesses());
            assertEquals(3, dist.getSampleSize());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCumulativeProbability() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertEquals(0.24242424242424243, dist.cumulativeProbability(2), 0.0001);
            assertEquals(1.0, dist.cumulativeProbability(5), 0.0001);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProbability() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertEquals(0.5151515151515151, dist.probability(2), 0.0001);
            assertEquals(0.15151515151515152, dist.probability(5), 0.0001);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpperCumulativeProbability() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertEquals(0.7575757575757576, dist.upperCumulativeProbability(2), 0.0001);
            assertEquals(0.0, dist.upperCumulativeProbability(5), 0.0001);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetNumericalMean() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertEquals(1.5, dist.getNumericalMean(), 0.0001);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetNumericalVariance() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertEquals(0.3181818181818182, dist.getNumericalVariance(), 0.0001);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetSupportLowerBound() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertEquals(0, dist.getSupportLowerBound());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetSupportUpperBound() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertEquals(3, dist.getSupportUpperBound());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsSupportConnected() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertEquals(true, dist.isSupportConnected());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            e.printStackTrace();
        }
    }
}