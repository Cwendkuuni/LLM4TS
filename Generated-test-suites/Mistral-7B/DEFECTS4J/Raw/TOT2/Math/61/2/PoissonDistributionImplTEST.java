import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithValidMean() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.5);
        assertEquals(1.5, poissonDistribution.getMean(), 0.001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithInvalidMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double probability = poissonDistribution.probability(0);
        assertEquals(0.110223621864846, probability, 0.0001);

        probability = poissonDistribution.probability(1);
        assertEquals(0.238075202134649, probability, 0.0001);

        probability = poissonDistribution.probability(5);
        assertEquals(0.129390825080223, probability, 0.0001);

        probability = poissonDistribution.probability(10);
        assertEquals(0.0598299326837519, probability, 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.5);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(0);
        assertEquals(0.0497629865289273, cumulativeProbability, 0.0001);

        cumulativeProbability = poissonDistribution.cumulativeProbability(1);
        assertEquals(0.150223123291126, cumulativeProbability, 0.0001);

        cumulativeProbability = poissonDistribution.cumulativeProbability(2);
        assertEquals(0.312283233367961, cumulativeProbability, 0.0001);

        cumulativeProbability = poissonDistribution.cumulativeProbability(10);
        assertEquals(0.879243576418828, cumulativeProbability, 0.0001);

        cumulativeProbability = poissonDistribution.cumulativeProbability(50);
        assertEquals(1, cumulativeProbability, 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(4.5);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(0);
        assertEquals(0.0359753430844162, normalApproximateProbability, 0.0001);

        normalApproximateProbability = poissonDistribution.normalApproximateProbability(1);
        assertEquals(0.136015457683866, normalApproximateProbability, 0.0001);

        normalApproximateProbability = poissonDistribution.normalApproximateProbability(5);
        assertEquals(0.136015457683866, normalApproximateProbability, 0.0001);

        normalApproximateProbability = poissonDistribution.normalApproximateProbability(10);
        assertEquals(0.0675478804034853, normalApproximateProbability, 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        int sample = poissonDistribution.sample();
        assertEquals(0, sample, 0);

        sample = poissonDistribution.sample();
        assertEquals(1, sample, 0);

        sample = poissonDistribution.sample();
        assertEquals(2, sample, 0);

        sample = poissonDistribution.sample();
        assertEquals(3, sample, 0);

        sample = poissonDistribution.sample();
        assertEquals(4, sample, 0);
    }

    @Test
    public void testGammaRegularizedGammaQ() throws MathException {
        double value = Gamma.regularizedGammaQ(0.5, 0.5);
        assertEquals(0.5, value, 0.0001);

        value = Gamma.regularizedGammaQ(1.5, 0.5);
        assertEquals(0.888182656436342, value, 0.0001);

        value = Gamma.regularizedGammaQ(2.5, 0.5);
        assertEquals(0.992313733755506, value, 0.0001);

        value = Gamma.regularizedGammaQ(3.5, 0.5);
        assertEquals(0.999999999999998, value, 0.0001);
    }
}