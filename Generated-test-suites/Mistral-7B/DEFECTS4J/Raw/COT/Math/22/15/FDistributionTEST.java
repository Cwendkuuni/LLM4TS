import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionWithInvalidDegreesOfFreedom() {
        try {
            new FDistribution(-1.0, 2.0);
        } catch (NotStrictlyPositiveException e) {
            // expected exception
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionWithInvalidDenominatorDegreesOfFreedom() {
        try {
            new FDistribution(1.0, -1.0);
        } catch (NotStrictlyPositiveException e) {
            // expected exception
        }
    }

    @Test
    public void testFDistributionConstructor() {
        double numeratorDegreesOfFreedom = 2.0;
        double denominatorDegreesOfFreedom = 3.0;
        RealDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testFDistributionDensity() {
        double numeratorDegreesOfFreedom = 2.0;
        double denominatorDegreesOfFreedom = 3.0;
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double x = 1.5;
        double expectedDensity = 0.45322522325146039;
        double delta = 1e-6;
        double density = distribution.density(x);
        assertTrue(Math.abs(density - expectedDensity) < delta);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        double numeratorDegreesOfFreedom = 2.0;
        double denominatorDegreesOfFreedom = 3.0;
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double x = 1.5;
        double expectedCumulativeProbability = 0.919141158482522;
        double delta = 1e-6;
        double cumulativeProbability = distribution.cumulativeProbability(x);
        assertTrue(Math.abs(cumulativeProbability - expectedCumulativeProbability) < delta);
    }

    @Test
    public void testFDistributionMean() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double expectedMean = 3.0 / 2.0;
        double mean = distribution.getNumericalMean();
        assertTrue(Math.abs(mean - expectedMean) < 1e-6);
    }

    @Test(expected = IllegalStateException.class)
    public void testFDistributionVarianceBeforeCalculation() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double variance = distribution.getNumericalVariance();
    }

    @Test
    public void testFDistributionVarianceAfterCalculation() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        distribution.calculateNumericalVariance();
        double expectedVariance = 0.116760357127513;
        double variance = distribution.getNumericalVariance();
        assertTrue(Math.abs(variance - expectedVariance) < 1e-6);
    }
}