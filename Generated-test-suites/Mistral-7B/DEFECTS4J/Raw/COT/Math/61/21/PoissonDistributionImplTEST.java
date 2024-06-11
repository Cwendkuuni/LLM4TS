import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomDataGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructor_ValidMean() {
        double mean = 2.5;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);
        assertEquals(mean, poissonDistribution.getMean());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_InvalidMean() {
        double mean = 0;
        new PoissonDistributionImpl(mean);
    }

    @Test
    public void testProbability_ValidInput() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        double probability = poissonDistribution.probability(0);
        assertEquals(0.1353352832306153, probability, 0.000001);

        probability = poissonDistribution.probability(1);
        assertEquals(0.22314033910696236, probability, 0.000001);

        probability = poissonDistribution.probability(5);
        assertEquals(0.02271688255764344, probability, 0.000001);

        probability = poissonDistribution.probability(10);
        assertEquals(0.00658396833806015, probability, 0.000001);
    }

    @Test
    public void testProbability_InvalidInput() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        try {
            poissonDistribution.probability(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            poissonDistribution.probability(Integer.MAX_VALUE);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCumulativeProbability_ValidInput() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(0);
        assertEquals(0.1353352832306153, cumulativeProbability, 0.000001);

        cumulativeProbability = poissonDistribution.cumulativeProbability(1);
        assertEquals(0.3553805170839316, cumulativeProbability, 0.000001);

        cumulativeProbability = poissonDistribution.cumulativeProbability(5);
        assertEquals(0.9772322065700424, cumulativeProbability, 0.000001);

        cumulativeProbability = poissonDistribution.cumulativeProbability(10);
        assertEquals(1.0, cumulativeProbability);
    }

    @Test
    public void testCumulativeProbability_InvalidInput() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        try {
            poissonDistribution.cumulativeProbability(-1);
            fail();
        } catch (MathException e) {
            // expected
        }

        try {
            poissonDistribution.cumulativeProbability(Integer.MAX_VALUE);
            fail();
        } catch (MathException e) {
            // expected
        }
    }

    @Test
    public void testNormalApproximateProbability_ValidInput() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);

        int x = 0;
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(x);
        assertEquals(0.1359140351152721, normalApproximateProbability, 0.00001);

        x = 1;
        normalApproximateProbability = poissonDistribution.normalApproximateProbability(x);
        assertEquals(0.22314033910696236, normalApproximateProbability, 0.00001);

        x = 5;
        normalApproximateProbability = poissonDistribution.normalApproximateProbability(x);
        assertEquals(0.02271688255764344, normalApproximateProbability, 0.00001);

        x = 10;
        normalApproximateProbability = poissonDistribution.normalApproximateProbability(x);
        assertEquals(0.00658396833806015, normalApproximateProbability, 0.00001);
    }

    @Test
    public void testNormalApproximateProbability_InvalidInput() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        try {
            poissonDistribution.normalApproximateProbability(-1);
            fail();
        } catch (MathException e) {
            // expected
        }

        try {
            poissonDistribution.normalApproximateProbability(Integer.MAX_VALUE);
            fail();
        } catch (MathException e) {
            // expected
        }
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        int sample = poissonDistribution.sample();
        assertTrue(sample >= 0);
        assertTrue(sample <= 1000 * 2.5);

        sample = poissonDistribution.sample();
        assertTrue(sample >= 0);
        assertTrue(sample <= 1000 * 2.5);

        sample = poissonDistribution.sample();
        assertTrue(sample >= 0);
        assertTrue(sample <= 1000 * 2.5);
    }
}