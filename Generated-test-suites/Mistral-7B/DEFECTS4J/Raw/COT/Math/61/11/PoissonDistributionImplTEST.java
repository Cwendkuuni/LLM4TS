import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, pd.getMean(), 0.001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbabilityZero() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(1.0);
        double probabilityZero = pd.probability(0);
        assertEquals(0.24009855617663945, probabilityZero, 0.00000001);
    }

    @Test
    public void testProbabilityPositive() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(1.0);
        double probabilityPositive = pd.probability(1);
        assertEquals(0.27068528357894736, probabilityPositive, 0.00000001);
    }

    @Test
    public void testProbabilityNegativeAndMaxValue() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(1.0);
        double probabilityNegative = pd.probability(-1);
        assertEquals(0.0, probabilityNegative, 0.0001);
        double probabilityMaxValue = pd.probability(Integer.MAX_VALUE);
        assertEquals(0.0, probabilityMaxValue, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegative() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(1.0);
        pd.cumulativeProbability(-1);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityMaxValue() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(1.0);
        pd.cumulativeProbability(Integer.MAX_VALUE);
    }

    @Test
    public void testCumulativeProbabilityWithinRange() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(1.0);
        double cumulativeProbability = pd.cumulativeProbability(3);
        assertEquals(0.4845750393733926, cumulativeProbability, 0.00000001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(3.5);
        double result = pd.normalApproximateProbability(3);
        assertEquals(0.4821539714048664, result, 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(5.0);
        int sample = pd.sample();
        assertEquals(5, sample);
    }

    @Test
    public void testSampleNegativeMean() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(-1.5);
        int sample = pd.sample();
        assertEquals(-1, sample);
    }

    @Test
    public void testCumulativeProbabilityWithMaxIterations() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(1000000, 1.0, 100);
        double result = pd.cumulativeProbability(100000);
        assertEquals(0.99999999999999995, result, 0.0000000000000001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithSmallEpsilon() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(1.0, 0.0);
        pd.cumulativeProbability(1);
    }
}