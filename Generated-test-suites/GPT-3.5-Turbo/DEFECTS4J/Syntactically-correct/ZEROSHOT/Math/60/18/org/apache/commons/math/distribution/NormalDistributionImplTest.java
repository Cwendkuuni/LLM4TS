import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.MathException;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        double mean = distribution.getMean();
        assertEquals(10, mean, 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        double standardDeviation = distribution.getStandardDeviation();
        assertEquals(2, standardDeviation, 0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        double density = distribution.density(12);
        assertEquals(0.12098536225957168, density, 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        double cumulativeProbability = distribution.cumulativeProbability(12);
        assertEquals(0.8413447460685429, cumulativeProbability, 0.0001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, -2);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        double inverseCumulativeProbability = distribution.inverseCumulativeProbability(0.5);
        assertEquals(10, inverseCumulativeProbability, 0);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        double sample = distribution.sample();
        assertNotNull(sample);
    }
}