import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        try {
            HypergeometricDistribution hyperDist = new HypergeometricDistribution(10, 5, 3);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            assertEquals("Expected exception", e.getMessage());
        }
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, hyperDist.cumulativeProbability(2), 0.01);
        assertEquals(1.0, hyperDist.cumulativeProbability(5), 0.01);
        assertEquals(0.236, hyperDist.cumulativeProbability(3), 0.01);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, hyperDist.probability(1), 0.01);
        assertEquals(0.031, hyperDist.probability(2), 0.01);
        assertEquals(0.236, hyperDist.probability(3), 0.01);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.0, hyperDist.upperCumulativeProbability(1), 0.01);
        assertEquals(0.382, hyperDist.upperCumulativeProbability(3), 0.01);
    }

    @Test
    public void testNumericalMean() {
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, hyperDist.getNumericalMean(), 0.01);
    }

    @Test
    public void testNumericalVariance() {
        HypergeometricDistribution hyperDist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.38, hyperDist.getNumericalVariance(), 0.01);
    }

}