import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithValidArgs() {
        double mean = 3.5;
        PoissonDistribution poissonDist = new PoissonDistributionImpl(mean);
        PoissonDistributionImpl poissonDistImpl = (PoissonDistributionImpl) poissonDist;
        assertEquals(mean, poissonDistImpl.getMean());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidArg() {
        PoissonDistributionImpl poissonDist = new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbability() {
        double mean = 3.5;
        PoissonDistributionImpl poissonDist = new PoissonDistributionImpl(mean);

        for (int i = 0; i <= 10; i++) {
            double probability = poissonDist.probability(i);
            double normalApproximateProbability = poissonDist.normalApproximateProbability(i);
            assertEquals(Precision.isClose(probability, normalApproximateProbability), true);
        }
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        double mean = 3.5;
        PoissonDistributionImpl poissonDist = new PoissonDistributionImpl(mean);

        for (int i = 0; i <= 10; i++) {
            double cumulativeProbability = poissonDist.cumulativeProbability(i);
            double normalApproximateProbability = poissonDist.normalApproximateProbability(i);
            assertEquals(Precision.isClose(cumulativeProbability, normalApproximateProbability), true);
        }

        double epsilon = 0.001;
        double maxIterations = 1000000;

        PoissonDistributionImpl poissonDistWithCustomArgs = new PoissonDistributionImpl(mean, epsilon, maxIterations);

        for (int i = 0; i <= 10; i++) {
            double cumulativeProbability = poissonDistWithCustomArgs.cumulativeProbability(i);
            assertEquals(cumulativeProbability, Gamma.regularizedGammaQ((double) i + 1, mean, epsilon, maxIterations), Precision.SAFE_EPSILON);
        }
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithInvalidArg() throws Exception {
        double mean = 3.5;
        PoissonDistributionImpl poissonDist = new PoissonDistributionImpl(mean);
        poissonDist.cumulativeProbability(-1);
    }

    @Test
    public void testSample() throws Exception {
        PoissonDistributionImpl poissonDist = new PoissonDistributionImpl(5.5);
        int sample = poissonDist.sample();
        assertEquals(sample, poissonDist.sample());
    }

    @Test(expected = MathException.class)
    public void testSampleWithInvalidMean() throws Exception {
        PoissonDistributionImpl poissonDist = new PoissonDistributionImpl(-5.5);
        poissonDist.sample();
    }

}