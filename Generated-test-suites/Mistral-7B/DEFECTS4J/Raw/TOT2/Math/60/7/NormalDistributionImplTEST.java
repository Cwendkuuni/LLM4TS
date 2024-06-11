import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_DefaultConstructor() {
        NormalDistribution normalDist = new NormalDistributionImpl();
        assertEquals(0.0, normalDist.getMean(), 0.0);
        assertEquals(1.0, normalDist.getStandardDeviation(), 0.0);
    }

    @Test
    public void testNormalDistributionImpl_ConstructorWithMeanAndStandardDeviation() {
        NormalDistribution normalDist = new NormalDistributionImpl(1.5, 2.3);
        assertEquals(1.5, normalDist.getMean(), 0.0);
        assertEquals(2.3, normalDist.getStandardDeviation(), 0.0);
    }

    @Test
    public void testNormalDistributionImpl_ConstructorWithMeanAndStandardDeviationAndAccuracy() {
        NormalDistribution normalDist = new NormalDistributionImpl(1.5, 2.3, 0.001);
        assertEquals(1.5, normalDist.getMean(), 0.0);
        assertEquals(2.3, normalDist.getStandardDeviation(), 0.0);
        assertEquals(0.001, normalDist.getSolverAbsoluteAccuracy(), 0.001);
    }

    @Test
    public void testNormalDistributionImpl_ConstructorWithInvalidStandardDeviation() {
        try {
            new NormalDistributionImpl(0.0, 0.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testDensity() {
        NormalDistribution normalDist = new NormalDistributionImpl(1.5, 2.3);
        double density = normalDist.density(2.5);
        assertEquals(0.045579134525972, density, 0.000001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistribution normalDist = new NormalDistributionImpl(1.5, 2.3);
        double cdf = normalDist.cumulativeProbability(1.5);
        assertEquals(0.5, cdf, 0.0001);

        try {
            normalDist.cumulativeProbability(1000.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected exception
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistribution normalDist = new NormalDistributionImpl(1.5, 2.3);
        double p = 0.8413;
        double inverseCdf = normalDist.inverseCumulativeProbability(p);
        assertEquals(1.536576998337312, inverseCdf, 0.001);

        try {
            normalDist.inverseCumulativeProbability(0.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected exception
        }

        try {
            normalDist.inverseCumulativeProbability(1.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected exception
        }
    }

    @Test
    public void testSample() {
        NormalDistribution normalDist = new NormalDistributionImpl(1.5, 2.3);
        double sample = normalDist.sample();
        assertEquals(1.505535963366516, sample, 0.01);
    }
}