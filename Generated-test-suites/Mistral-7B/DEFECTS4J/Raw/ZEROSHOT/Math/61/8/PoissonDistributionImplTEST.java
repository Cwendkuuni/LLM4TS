import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testPoissonDistributionImplWithDefaultConstructor() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        assertEquals(1.0, poissonDistribution.getMean(), 0.0);

        double probability = poissonDistribution.probability(0);
        assertEquals(0.2704859836334354, probability, 0.00001);
        probability = poissonDistribution.probability(1);
        assertEquals(0.2231387803723425, probability, 0.00001);
        probability = poissonDistribution.probability(2);
        assertEquals(0.1550405059063565, probability, 0.00001);

        try {
            double cumulativeProbability = poissonDistribution.cumulativeProbability(-1);
            fail("Expected MathException");
        } catch (MathException e) {
            // expected
        }

        cumulativeProbability = poissonDistribution.cumulativeProbability(0);
        assertEquals(0.3678794411414484, cumulativeProbability, 0.00001);
        cumulativeProbability = poissonDistribution.cumulativeProbability(1);
        assertEquals(0.5758928080892527, cumulativeProbability, 0.00001);
        cumulativeProbability = poissonDistribution.cumulativeProbability(2);
        assertEquals(0.7313445532265339, cumulativeProbability, 0.00001);

        int sample = poissonDistribution.sample();
        assertEquals(1, sample);

        PoissonDistribution poissonDistribution2 = new PoissonDistributionImpl(2.0);
        probability = poissonDistribution2.probability(0);
        assertEquals(0.1353352832306477, probability, 0.00001);
        probability = poissonDistribution2.probability(1);
        assertEquals(0.2231407239052956, probability, 0.00001);
        probability = poissonDistribution2.probability(2);
        assertEquals(0.2231387803723425, probability, 0.00001);

        cumulativeProbability = poissonDistribution2.cumulativeProbability(0);
        assertEquals(0.1353352832306477, cumulativeProbability, 0.00001);
        cumulativeProbability = poissonDistribution2.cumulativeProbability(1);
        assertEquals(0.3584746665612954, cumulativeProbability, 0.00001);
        cumulativeProbability = poissonDistribution2.cumulativeProbability(2);
        assertEquals(0.5818311927919415, cumulativeProbability, 0.00001);

        sample = poissonDistribution2.sample();
        assertEquals(1, sample);

        PoissonDistribution poissonDistribution3 = new PoissonDistributionImpl(5.0);
        probability = poissonDistribution3.probability(0);
        assertEquals(0.00673131327583623, probability, 0.00001);
        probability = poissonDistribution3.probability(1);
        assertEquals(0.0670820322524837, probability, 0.00001);
        probability = poissonDistribution3.probability(2);
        assertEquals(0.1005164844234656, probability, 0.00001);
        probability = poissonDistribution3.probability(3);
        assertEquals(0.0921049207341201, probability, 0.00001);
        probability = poissonDistribution3.probability(4);
        assertEquals(0.0536504238859866, probability, 0.00001);

        cumulativeProbability = poissonDistribution3.cumulativeProbability(0);
        assertEquals(0.00673131327583623, cumulativeProbability, 0.00001);
        cumulativeProbability = poissonDistribution3.cumulativeProbability(1);
        assertEquals(0.0671026626613231, cumulativeProbability, 0.00001);
        cumulativeProbability = poissonDistribution3.cumulativeProbability(2);
        assertEquals(0.1637691321037598, cumulativeProbability, 0.00001);
        cumulativeProbability = poissonDistribution3.cumulativeProbability(3);
        assertEquals(0.2563835570508595, cumulativeProbability, 0.00001);
        cumulativeProbability = poissonDistribution3.cumulativeProbability(4);
        assertEquals(0.3541732863754271, cumulativeProbability, 0.00001);

        sample = poissonDistribution3.sample();
        assertEquals(1, sample);
    }

    @Test
    public void testPoissonDistributionImplWithCustomConstructor() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0, 1e-12, 10000000);
        assertEquals(1.0, poissonDistribution.getMean(), 0.0);

        double probability = poissonDistribution.probability(0);
        assertEquals(0.2704859836334354, probability, 0.00001);
        probability = poissonDistribution.probability(1);
        assertEquals(0.2231387803723425, probability, 0.00001);
        probability = poissonDistribution.probability(2);
        assertEquals(0.1550405059063565, probability, 0.00001);

        try {
            double cumulativeProbability = poissonDistribution.cumulativeProbability(-1);
            fail("Expected MathException");
        } catch (MathException e) {
            // expected
        }

        cumulativeProbability = poissonDistribution.cumulativeProbability(0);
        assertEquals(0.3678794411414484, cumulativeProbability, 0.00001);
        cumulativeProbability = poissonDistribution.cumulativeProbability(1);
        assertEquals(0.5758928080892527, cumulativeProbability, 0.00001);
        cumulativeProbability = poissonDistribution.cumulativeProbability(2);
        assertEquals(0.7313445532265339, cumulativeProbability, 0.00001);

        int sample = poissonDistribution.sample();
        assertEquals(1, sample);

        PoissonDistribution poissonDistribution2 = new PoissonDistributionImpl(2.0, 1e-12, 10000000);
        probability = poissonDistribution2.probability(0);
        assertEquals(0.1353352832306477, probability, 0.00001);
        probability = poissonDistribution2.probability(1);
        assertEquals(0.2231407239052956, probability, 0.00001);
        probability = poissonDistribution2.probability(2);
        assertEquals(0.2231387803723425, probability, 0.00001);

        cumulativeProbability = poissonDistribution2.cumulativeProbability(0);
        assertEquals(0.1353352832306477, cumulativeProbability, 0.00001);
        cumulativeProbability = poissonDistribution2.cumulativeProbability(1);
        assertEquals(0.3584746665612954, cumulativeProbability, 0.00001);
        cumulativeProbability = poissonDistribution2.cumulativeProbability(2);
        assertEquals(0.5818311927919415, cumulativeProbability, 0.00001);

        sample = poissonDistribution2.sample();
        assertEquals(1, sample);

        PoissonDistribution poissonDistribution3 = new PoissonDistributionImpl(5.0, 1e-12, 10000000);
        probability = poissonDistribution3.probability(0);
        assertEquals(0.00673131327583623, probability, 0.00001);
        probability = poissonDistribution3.probability(1);
        assertEquals(0.0670820322524837, probability, 0.00001);
        probability = poissonDistribution3.probability(2);
        assertEquals(0.1005164844234656, probability, 0.00001);
        probability = poissonDistribution3.probability(3);
        assertEquals(0.0921049207341201, probability, 0.00001);
        probability = poissonDistribution3.probability(4);
        assertEquals(0.0536504238859866, probability, 0.00001);

        cumulativeProbability = poissonDistribution3.cumulativeProbability(0);
        assertEquals(0.00673131327583623, cumulativeProbability, 0.00001);
        cumulativeProbability = poissonDistribution3.cumulativeProbability(1);
        assertEquals(0.0671026626613231, cumulativeProbability, 0.00001);
        cumulativeProbability = poissonDistribution3.cumulativeProbability(2);
        assertEquals(0.1637691321037598, cumulativeProbability, 0.00001);
        cumulativeProbability = poissonDistribution3.cumulativeProbability(3);
        assertEquals(0.2563835570508595, cumulativeProbability, 0.00001);
        cumulativeProbability = poissonDistribution3.cumulativeProbability(4);
        assertEquals(0.3541732863754271, cumulativeProbability, 0.00001);

        sample = poissonDistribution3.sample();
        assertEquals(1, sample);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPoissonDistributionImplWithNegativeMean() {
        new PoissonDistributionImpl(-1.0);
        fail("Expected IllegalArgumentException");
    }
}