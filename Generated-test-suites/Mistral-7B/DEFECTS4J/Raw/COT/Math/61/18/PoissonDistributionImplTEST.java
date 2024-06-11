import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.AbstractIntegerDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, dist.getMean(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbabilityZero() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(0.5);
        double prob = dist.probability(0);
        assertEquals(0.5960463415493162, prob, 0.0001);
    }

    @Test
    public void testProbabilityPositive() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.5);
        double prob = dist.probability(2);
        assertEquals(0.10825228382799516, prob, 0.0001);
    }

    @Test
    public void testProbabilityMaxValue() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(3.5);
        double prob = dist.probability(Integer.MAX_VALUE);
        assertEquals(0.0, prob, 0.01);
    }

    @Test
    public void testCumulativeProbabilityZero() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(0.5);
        double prob = dist.cumulativeProbability(0);
        assertEquals(0.5, prob, 0.01);
    }

    @Test
    public void testCumulativeProbabilityMaxValue() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(0.5);
        double prob = dist.cumulativeProbability(Integer.MAX_VALUE);
        assertEquals(1.0, prob, 0.01);
    }

    @Test
    public void testCumulativeProbabilityIntermediate() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.5);
        double prob = dist.cumulativeProbability(2);
        assertEquals(0.9048372988076783, prob, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityException() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(-1.5);
        dist.cumulativeProbability(0);
    }

    @Test
    public void testNormalApproximateProbabilityZero() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(0.5);
        double prob = dist.normalApproximateProbability(0);
        assertEquals(0.5960463415493162, prob, 0.0001);
    }

    @Test
    public void testNormalApproximateProbabilityMaxValue() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(0.5);
        double prob = dist.normalApproximateProbability(Integer.MAX_VALUE);
        assertEquals(0.0, prob, 0.01);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        int sample = dist.sample();
        assertTrue(sample >= 0 && sample <= 1000 * dist.getMean());
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(0.5);
        int lowerBound = dist.getDomainLowerBound(0.1);
        assertEquals(0, lowerBound);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(0.5);
        int upperBound = dist.getDomainUpperBound(0.1);
        assertEquals(Integer.MAX_VALUE, upperBound);
    }
}