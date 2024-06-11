import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstruction() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.getMean(), 0.001);
        assertEquals(1, distribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testNormalDistributionImplMeanAndStddevConstruction() {
        NormalDistribution distribution = new NormalDistributionImpl(2.5, 1.2);
        assertEquals(2.5, distribution.getMean(), 0.001);
        assertEquals(1.2, distribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testNormalDistributionImplThrowsExceptionForNegativeStddev() {
        assertThrows(NotStrictlyPositiveException.class, () ->
                new NormalDistributionImpl(0.1, -1.2));
    }

    @Test
    public void testNormalDistributionImplDensity() {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double density = distribution.density(0.5);
        assertEquals(0.146036852369147, density, 0.0001);

        distribution = new NormalDistributionImpl(1, 1);
        density = distribution.density(1.5);
        assertEquals(0.103162881981353, density, 0.0001);
    }

    @Test
    public void testNormalDistributionImplCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double probability = distribution.cumulativeProbability(0.5);
        assertEquals(0.682689492137, probability, 0.0001);

        distribution = new NormalDistributionImpl(1, 1);
        probability = distribution.cumulativeProbability(1.5);
        assertEquals(0.933192758698, probability, 0.0001);
    }

    @Test
    public void testNormalDistributionImplInverseCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double inverseProbability = distribution.inverseCumulativeProbability(0.5);
        assertEquals(0, inverseProbability, 0.0001);

        distribution = new NormalDistributionImpl(1, 1);
        inverseProbability = distribution.inverseCumulativeProbability(0.95);
        assertEquals(1.64485362585, inverseProbability, 0.0001);

        assertThrows(NumberIsTooLargeException.class, () ->
                distribution.inverseCumulativeProbability(1));

        distribution = new NormalDistributionImpl(0, 1);
        assertThrows(MathException.class, () ->
                distribution.inverseCumulativeProbability(0.1));
    }

    @Test
    public void testNormalDistributionImplSample() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double sample = distribution.sample();
        assertEquals(0, sample, 0.001);

        distribution = new NormalDistributionImpl(1, 1);
        sample = distribution.sample();
        assertEquals(1.54322808633, sample, 0.001);
    }
}