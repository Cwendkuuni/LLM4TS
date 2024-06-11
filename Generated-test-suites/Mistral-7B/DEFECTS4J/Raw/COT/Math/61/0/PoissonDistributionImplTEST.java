import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithValidMean() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, dist.getMean(), 0.001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-2.5);
    }

    @Test
    public void testProbabilityZero() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(0.5);
        assertEquals(0.6065115237302692, dist.probability(0), 0.00001);
    }

    @Test
    public void testProbabilityPositive() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double probability = dist.probability(1);
        assertEquals(0.21622417381401375, probability, 0.00001);
    }

    @Test
    public void testProbabilityNegative() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double probability = dist.probability(-1);
        assertEquals(0.0, probability, 0.00001);
    }

    @Test
    public void testProbabilityMaxValue() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double probability = dist.probability(Integer.MAX_VALUE);
        assertEquals(0.0, probability, 0.00001);
    }

    @Test
    public void testCumulativeProbabilityZero() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals(0, dist.cumulativeProbability(0), 0.00001);
    }

    @Test
    public void testCumulativeProbabilityPositive() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double cumulativeProbability = dist.cumulativeProbability(1);
        assertEquals(0.3679781669345104, cumulativeProbability, 0.00001);
    }

    @Test
    public void testCumulativeProbabilityMaxValue() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double cumulativeProbability = dist.cumulativeProbability(Integer.MAX_VALUE);
        assertEquals(1, cumulativeProbability, 0.00001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        int sample = dist.sample();
        assertTrue(sample >= 0);
        assertTrue(sample <= Integer.MAX_VALUE);
        assertEquals(dist.probability(sample), 1.0, 0.00001);
    }

    @Test
    public void testNormalApproximateProbabilityZero() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double probability = dist.normalApproximateProbability(0);
        assertEquals(0.5959966914328558, probability, 0.00001);
    }

    @Test
    public void testNormalApproximateProbabilityMaxValue() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        double probability = dist.normalApproximateProbability(Integer.MAX_VALUE);
        assertEquals(0.5, probability, 0.00001);
    }

    @Test
    public void testNormalApproximateProbabilityPositive() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        int x = 10;
        double probability = dist.normalApproximateProbability(x);
        assertEquals(0.07958839067454165, probability, 0.00001);
    }
}