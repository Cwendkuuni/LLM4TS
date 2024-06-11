import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(1.5, distribution.getMean(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbabilityOfZero() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.2231415926415793, distribution.probability(0), 0.001);
    }

    @Test
    public void testProbabilityOfNonZero() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.14384283877805396, distribution.probability(1), 0.001);
    }

    @Test
    public void testProbabilityOfLargeNumber() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(10.0);
        assertEquals(0.0000027777781839223663, distribution.probability(1000), 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityOfNegativeNumber() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        distribution.probability(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityOfIntegerGreaterThanIntMaxValue() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        distribution.probability(Integer.MAX_VALUE + 1);
    }

    @Test
    public void testCumulativeProbabilityOfZero() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.04986554228226013, distribution.cumulativeProbability(0), 0.001);
    }

    @Test
    public void testCumulativeProbabilityOfNonZero() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.19811782637795625, distribution.cumulativeProbability(1), 0.001);
    }

    @Test
    public void testCumulativeProbabilityOfLargeNumber() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(10.0);
        assertEquals(0.9999999999999999, distribution.cumulativeProbability(1000), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCumulativeProbabilityOfNegativeNumber() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        distribution.cumulativeProbability(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCumulativeProbabilityOfIntegerGreaterThanIntMaxValue() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        distribution.cumulativeProbability(Integer.MAX_VALUE + 1);
    }

    @Test
    public void testNormalApproximateProbabilityOfZero() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.04986554228226013, distribution.normalApproximateProbability(0), 0.001);
    }

    @Test
    public void testNormalApproximateProbabilityOfNonZero() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(0.19811782637795625, distribution.normalApproximateProbability(1), 0.001);
    }

    @Test
    public void testNormalApproximateProbabilityOfLargeNumber() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(10.0);
        assertEquals(0.9999999999999999, distribution.normalApproximateProbability(1000), 0.001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithNegativeMean() throws MathException {
        new PoissonDistributionImpl(-1.5).cumulativeProbability(0);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithMaxIterations() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1000000, 1.0, 1);
        distribution.cumulativeProbability(1000000);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithLowEpsilon() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.0, 1.0, 10000000);
        distribution.cumulativeProbability(1);
    }
}