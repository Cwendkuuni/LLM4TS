import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithValidMean() {
        PoissonDistribution pd = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, pd.getMean(), 0.001);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithInvalidMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbabilityMethodZero() {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        double probabilityZero = pd.probability(0);
        assertEquals(0.2400985614542365, probabilityZero, 0.0001);
    }

    @Test
    public void testProbabilityMethodPositive() {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        double probabilityFive = pd.probability(5);
        assertEquals(0.0787868428935385, probabilityFive, 0.0001);
    }

    @Test
    public void testProbabilityMethodMaxValue() {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        double probabilityMaxValue = pd.probability(Integer.MAX_VALUE);
        assertEquals(0.0, probabilityMaxValue, 0.0001);
    }

    @Test
    public void testCumulativeProbabilityMethodZero() {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        double cdfZero = pd.cumulativeProbability(0);
        assertEquals(0.0, cdfZero, 0.001);
    }

    @Test
    public void testCumulativeProbabilityMethodMaxValue() {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        double cdfMaxValue = pd.cumulativeProbability(Integer.MAX_VALUE);
        assertEquals(1.0, cdfMaxValue, 0.001);
    }

    @Test
    public void testCumulativeProbabilityMethodInBetween() {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        double cdfFive = pd.cumulativeProbability(5);
        assertEquals(0.6826894621010862, cdfFive, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityMethodNegative() {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        pd.cumulativeProbability(-1);
    }

    @Test
    public void testNormalApproximateProbabilityMethodZero() {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        double normalApproximateProbabilityZero = pd.normalApproximateProbability(0);
        assertEquals(0.2400985614542365, normalApproximateProbabilityZero, 0.0001);
    }

    @Test
    public void testNormalApproximateProbabilityMethodMaxValue() {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        double normalApproximateProbabilityMaxValue = pd.normalApproximateProbability(Integer.MAX_VALUE);
        assertEquals(0.0, normalApproximateProbabilityMaxValue, 0.0001);
    }

    @Test
    public void testSampleMethodNormalMean() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        int sample = pd.sample();
        assertTrue(sample >= 0 && sample <= 10);
    }

    @Test
    public void testSampleMethodLargeMean() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(1000.0);
        int sample = pd.sample();
        assertTrue(sample >= 0 && sample <= 1000);
    }

    @Test(expected = MathException.class)
    public void testSampleMethodInvalidMean() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(-1.0);
        pd.sample();
    }

    @Test
    public void testInverseCumulativeProbabilityMethodZero() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        int pValue = pd.inverseCumulativeProbability(0.0);
        assertEquals(0, pValue);
    }

    @Test
    public void testInverseCumulativeProbabilityMethodMaxValue() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        int pValue = pd.inverseCumulativeProbability(1.0);
        assertEquals(Integer.MAX_VALUE, pValue);
    }

    @Test
    public void testInverseCumulativeProbabilityMethodInBetween() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        int pValue = pd.inverseCumulativeProbability(0.5);
        assertEquals(1, pValue);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityMethodOutOfRange() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        pd.inverseCumulativeProbability(1.1);
    }
}