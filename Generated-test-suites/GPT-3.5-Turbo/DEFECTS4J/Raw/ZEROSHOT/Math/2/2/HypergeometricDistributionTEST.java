import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    private HypergeometricDistribution dist;

    @Before
    public void setup() {
        dist = new HypergeometricDistribution(100, 50, 10);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.0, dist.cumulativeProbability(-1), 0.00001);
        assertEquals(0.5370676822971778, dist.cumulativeProbability(0), 0.00001);
        assertEquals(0.8738415226858532, dist.cumulativeProbability(5), 0.00001);
        assertEquals(0.9946201290994826, dist.cumulativeProbability(10), 0.00001);
        assertEquals(1.0, dist.cumulativeProbability(11), 0.00001);
    }

    @Test
    public void testProbability() {
        assertEquals(0.0, dist.probability(-1), 0.00001);
        assertEquals(0.013551059212457501, dist.probability(0), 0.00001);
        assertEquals(0.2567345604172397, dist.probability(5), 0.00001);
        assertEquals(0.002785066163570808, dist.probability(10), 0.00001);
        assertEquals(0.0, dist.probability(11), 0.00001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        assertEquals(1.0, dist.upperCumulativeProbability(-1), 0.00001);
        assertEquals(0.4629323177028222, dist.upperCumulativeProbability(0), 0.00001);
        assertEquals(0.12615847731414677, dist.upperCumulativeProbability(5), 0.00001);
        assertEquals(0.005379870900517351, dist.upperCumulativeProbability(10), 0.00001);
        assertEquals(0.0, dist.upperCumulativeProbability(11), 0.00001);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(5.0, dist.getNumericalMean(), 0.00001);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(0.8762626262626262, dist.getNumericalVariance(), 0.00001);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(40, dist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(10, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(dist.isSupportConnected());
    }
}