import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstructorWithMeanAndStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.0, distribution.getMean(), 0.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testNormalDistributionImplConstructorWithMeanStandardDeviationAndAccuracy() {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0, 0.1);
        assertEquals(1.0, distribution.getMean(), 0.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplExceptionWithNegativeStandardDeviation() {
        new NormalDistributionImpl(1.0, -1.0, 0.1);
    }

    @Test
    public void testDensity() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989422804010993, distribution.density(0.0), 0.0001);
        assertEquals(0.3989422804010993, distribution.density(1.0), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.5, distribution.cumulativeProbability(0.0), 0.001);
        assertEquals(0.8413447460429453, distribution.cumulativeProbability(0.5), 0.001);
        assertEquals(1.0, distribution.cumulativeProbability(1.0), 0.001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithOutOfRangeValue() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        distribution.cumulativeProbability(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.0), 0.001);
        assertEquals(-0.577350269189626, distribution.inverseCumulativeProbability(0.5), 0.001);
        assertEquals(1.0, distribution.inverseCumulativeProbability(1.0), 0.001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityWithInvalidProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        distribution.inverseCumulativeProbability(-1.0);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.121335596521573, distribution.sample(), 0.01);
        assertEquals(-0.20246455520429, distribution.sample(), 0.01);
    }

    @Test
    public void testSampleWithMeanAndStandardDeviation() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(3.11883239869235, distribution.sample(), 0.01);
        assertEquals(-0.62704790718651, distribution.sample(), 0.01);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), 1e-10);
    }
}