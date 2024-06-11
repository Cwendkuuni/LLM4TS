import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_MeanAndStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals("Mean should be 0", 0, dist.getMean(), Precision.EPSILON);
        assertEquals("Standard deviation should be 1", 1, dist.getStandardDeviation(), Precision.EPSILON);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImpl_NegativeStandardDeviation() {
        new NormalDistributionImpl(0, -1);
    }

    @Test
    public void testNormalDistributionImpl_Density() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double density = dist.density(0);
        assertEquals("Density at mean should be max", 0.3989422804, density, Precision.EPSILON);
    }

    @Test
    public void testNormalDistributionImpl_CumulativeProbability() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double cumProb = dist.cumulativeProbability(0);
        assertEquals("Probability of 0 is 0.5", 0.5, cumProb, Precision.EPSILON);
        cumProb = dist.cumulativeProbability(3);
        assertEquals("Probability of 3 is very small", 0.000488559614, cumProb, Precision.EPSILON);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImpl_InverseCumulativeProbability_Zero() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.inverseCumulativeProbability(0);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImpl_InverseCumulativeProbability_One() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.inverseCumulativeProbability(1);
    }

    @Test
    public void testNormalDistributionImpl_Sample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double sample = dist.sample();
        assertEquals("Sample should be around 0", 0, sample, Precision.EPSILON);
    }

    @Test
    public void testNormalDistributionImpl_EdgeCases_NegativeValues() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double densityLeft = dist.density(-3);
        assertEquals("Density of negative values should be very small", -0.000488559614, densityLeft, Precision.EPSILON);
        double cumProbLeft = dist.cumulativeProbability(-3);
        assertEquals("Probability of negative values is 0", 0, cumProbLeft, Precision.EPSILON);

        double densityRight = dist.density(3);
        assertEquals("Density of positive values should be very small", -0.000488559614, densityRight, Precision.EPSILON);
        double cumProbRight = dist.cumulativeProbability(3);
        assertEquals("Probability of positive values is 0.5", 0.5, cumProbRight, Precision.EPSILON);
    }
}