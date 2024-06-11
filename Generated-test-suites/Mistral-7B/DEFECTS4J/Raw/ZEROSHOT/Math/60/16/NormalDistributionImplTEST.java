import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplCreation() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        NormalDistributionImpl normalDistributionImpl = (NormalDistributionImpl) distribution;
        assertEquals(0.0, normalDistributionImpl.getMean(), 0.0);
        assertEquals(1.0, normalDistributionImpl.getStandardDeviation(), 0.0);
        assertNotNull(normalDistributionImpl.getSolverAbsoluteAccuracy());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplCreationWithInvalidStandardDeviation() {
        new NormalDistributionImpl(-1.0, 1.0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl normalDistributionImpl = new NormalDistributionImpl(0, 1);
        double density = normalDistributionImpl.density(0.5);
        assertEquals(0.1543080634815279, density, 0.00001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normalDistributionImpl = new NormalDistributionImpl(0, 1);
        double cumulativeProbability = normalDistributionImpl.cumulativeProbability(0.5);
        assertEquals(0.6826894921373235, cumulativeProbability, 0.00001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normalDistributionImpl = new NormalDistributionImpl(0, 1);
        double inverseCumulativeProbability = normalDistributionImpl.inverseCumulativeProbability(0.5);
        assertEquals(0.4765129713262381, inverseCumulativeProbability, 0.00001);
    }

    @Test
    public void testSample() {
        NormalDistributionImpl normalDistributionImpl = new NormalDistributionImpl(0, 1);
        double sample = normalDistributionImpl.sample();
        assertFalse(Double.isNaN(sample));
        assertEquals(0.2796131612109532, sample, 0.0001);
    }

}