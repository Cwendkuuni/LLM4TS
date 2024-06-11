import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.special.Erf;
import org.apache.commons.math.util.FastMath;

public class NormalDistributionImplTest {

    // Test case for getMean()
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        assertEquals(5, distribution.getMean(), 1e-9);
    }

    // Test case for getStandardDeviation()
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        assertEquals(2, distribution.getStandardDeviation(), 1e-9);
    }

    // Test case for density(double x)
    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        double density = distribution.density(7);
        assertEquals(0.120985362, density, 1e-9);
    }

    // Test case for cumulativeProbability(double x)
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        double cumulativeProbability = distribution.cumulativeProbability(7);
        assertEquals(0.841344746, cumulativeProbability, 1e-9);
    }

    // Test case for inverseCumulativeProbability(double p)
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        double inverseCumulativeProbability = distribution.inverseCumulativeProbability(0.841344746);
        assertEquals(7, inverseCumulativeProbability, 1e-9);
    }

    // Test case for sample()
    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        double sample = distribution.sample();
        assertEquals(true, sample >= 0 && sample <= 10);
    }

    // Test case for getDomainLowerBound(double p)
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        double domainLowerBound = distribution.getDomainLowerBound(0.5);
        assertEquals(5, domainLowerBound, 1e-9);
    }

    // Test case for getDomainUpperBound(double p)
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        double domainUpperBound = distribution.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, domainUpperBound, 1e-9);
    }

    // Test case for getInitialDomain(double p)
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        double initialDomain = distribution.getInitialDomain(0.5);
        assertEquals(7, initialDomain, 1e-9);
    }
}