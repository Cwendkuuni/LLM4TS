import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, pd.getMean(), 0.01);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbabilityMethod() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);
        assertEquals(0.110223621945, pd.probability(1), 0.0001);
        assertEquals(0.172311531736, pd.probability(2), 0.0001);
        assertEquals(0.200885556319, pd.probability(3), 0.0001);
    }

    @Test
    public void testCumulativeProbabilityMethod() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);
        assertEquals(0.0, pd.cumulativeProbability(-1), 0.0);
        assertEquals(0.110223621945, pd.cumulativeProbability(0), 0.0);
        assertEquals(0.353632006515, pd.cumulativeProbability(1), 0.0001);
        assertEquals(0.585995562358, pd.cumulativeProbability(2), 0.0001);
        assertEquals(0.816653737881, pd.cumulativeProbability(3), 0.0001);
        assertEquals(1.0, pd.cumulativeProbability(Integer.MAX_VALUE), 0.0);
    }

    @Test
    public void testNormalApproximateProbabilityMethod() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);
        NormalDistributionImpl normal = new NormalDistributionImpl(2.5, 1.5);
        assertEquals(0.110223621945, pd.normalApproximateProbability(0), 0.0001);
        assertEquals(0.353632006515, pd.normalApproximateProbability(1), 0.0001);
        assertEquals(0.585995562358, pd.normalApproximateProbability(2), 0.0001);
        assertEquals(0.816653737881, pd.normalApproximateProbability(3), 0.0001);
        assertArrayEquals(new double[]{0.110223621945, 0.353632006515, 0.585995562358, 0.816653737881},
                normal.cumulativeProbability(new double[]{0.0, 1.0, 2.0, 3.0}));
    }

    @Test
    public void testSampleMethod() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);
        int sample = pd.sample();
        assertEquals(1, sample, 0);
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testCumulativeProbabilityWithLargeMean() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(10000.0);
        double expectedCumulativeProbability = 1.0 - Gamma.regularizedGammaP(5000.5, 10000.0);
        assertEquals(expectedCumulativeProbability, pd.cumulativeProbability(4999), 0.001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithInvalidInput() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);
        pd.cumulativeProbability(Integer.MIN_VALUE);
    }
}