import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testPoissonDistributionConstructorWithValidArguments() {
        PoissonDistribution pd = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, pd.getMean(), 0.01);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testPoissonDistributionConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbabilityMethodZero() {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        double probability = pd.probability(0);
        assertEquals(0.2706602, probability, 0.01);
    }

    @Test
    public void testProbabilityMethodValidValue() {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        double probability = pd.probability(1);
        assertEquals(0.3678794, probability, 0.01);
    }

    @Test
    public void testProbabilityMethodLargeValue() {
        PoissonDistribution pd = new PoissonDistributionImpl(10.0);
        double probability = pd.probability(15);
        double expected = FastMath.exp(-10.0) * FastMath.sqrt(FastMath.TWO_PI * 15) / FastMath.SQRT_E;
        assertEquals(expected, probability, 0.01);
    }

    @Test
    public void testCumulativeProbabilityMethodZero() {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        double cumulativeProbability = pd.cumulativeProbability(0);
        assertEquals(0.3678794, cumulativeProbability, 0.01);
    }

    @Test
    public void testCumulativeProbabilityMethodValidValue() {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        double cumulativeProbability = pd.cumulativeProbability(1);
        assertEquals(0.7352989, cumulativeProbability, 0.01);
    }

    @Test
    public void testCumulativeProbabilityMethodLargeValue() {
        PoissonDistribution pd = new PoissonDistributionImpl(10.0);
        double cumulativeProbability = pd.cumulativeProbability(15);
        double expected = Gamma.regularizedGammaP(15.5, 10.0);
        assertEquals(expected, cumulativeProbability, 0.01);
    }

    @Test
    public void testNormalApproximateProbabilityMethodZero() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        double normalApproximateProbability = pd.normalApproximateProbability(0);
        assertEquals(0.3678794, normalApproximateProbability, 0.01);
    }

    @Test
    public void testNormalApproximateProbabilityMethodValidValue() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        double normalApproximateProbability = pd.normalApproximateProbability(1);
        assertEquals(0.3678794, normalApproximateProbability, 0.01);
    }

    @Test
    public void testNormalApproximateProbabilityMethodLargeValue() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(10.0);
        double normalApproximateProbability = pd.normalApproximateProbability(15);
        double expected = Gamma.regularizedGammaQ(15.5, 10.0);
        assertEquals(expected, normalApproximateProbability, 0.01);
    }

    @Test
    public void testSampleMethodSmallMean() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        int sample = pd.sample();
        assertEquals(1, sample);
    }

    @Test
    public void testSampleMethodLargeMean() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(10.0);
        int sample = pd.sample();
        int expected = random.nextInt(1000 * 10) + 1;
        assertEquals(expected, sample);
    }

    @Test
    public void testInverseCumulativeProbabilityMethodSmallProbability() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        int inverseCumulativeProbability = pd.inverseCumulativeProbability(0.3678794);
        assertEquals(0, inverseCumulativeProbability);
    }

    @Test
    public void testInverseCumulativeProbabilityMethodLargeProbability() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        int inverseCumulativeProbability = pd.inverseCumulativeProbability(0.7352989);
        assertEquals(1, inverseCumulativeProbability);
    }

    @Test
    public void testInverseCumulativeProbabilityMethodEdgeCase() throws MathException {
        PoissonDistribution pd = new PoissonDistributionImpl(1.0);
        int inverseCumulativeProbability = pd.inverseCumulativeProbability(0.5);
        assertEquals(1, inverseCumulativeProbability);
    }
}