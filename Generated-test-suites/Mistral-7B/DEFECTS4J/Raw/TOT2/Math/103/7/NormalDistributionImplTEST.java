import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstructor() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(5.0, normalDist.getMean(), 0.001);
        assertEquals(2.0, normalDist.getStandardDeviation(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeMean() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(-5.0, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeStandardDeviation() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(5.0, -2.0);
    }

    @Test
    public void testCumulativeProbabilityWithExtremeValues() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        double result = normalDist.cumulativeProbability(-10.0);
        assertEquals(0.0, result, 0.001);
        result = normalDist.cumulativeProbability(100.0);
        assertEquals(1.0, result, 0.001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityWithInvalidProbability() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        normalDist.inverseCumulativeProbability(-0.5);
        normalDist.inverseCumulativeProbability(2.0);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityWithExtremeProbabilities() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        double result = normalDist.inverseCumulativeProbability(0.0);
        assertEquals(Double.NEGATIVE_INFINITY, result, 0.0);
        result = normalDist.inverseCumulativeProbability(1.0);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
    }
}