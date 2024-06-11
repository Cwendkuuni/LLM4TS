import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl(0, 1);
    }

    @Test
    public void testGetMean() {
        assertEquals(0, normalDistribution.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, normalDistribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testDensity() {
        // Typical case
        double density = normalDistribution.density(0);
        assertEquals(0.39894228, density, 0.0001);

        // Edge cases
        density = normalDistribution.density(Double.POSITIVE_INFINITY);
        assertEquals(0, density, 0.0001);

        density = normalDistribution.density(Double.NEGATIVE_INFINITY);
        assertEquals(0, density, 0.0001);

        density = normalDistribution.density(Double.NaN);
        assertEquals(Double.NaN, density, 0.0001);

        density = normalDistribution.density(-1);
        assertEquals(0.24197072, density, 0.0001);

        density = normalDistribution.density(1);
        assertEquals(0.24197072, density, 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        // Typical case
        double cumulativeProbability = normalDistribution.cumulativeProbability(0);
        assertEquals(0.5, cumulativeProbability, 0.0001);

        // Edge cases
        cumulativeProbability = normalDistribution.cumulativeProbability(Double.POSITIVE_INFINITY);
        assertEquals(1, cumulativeProbability, 0.0001);

        cumulativeProbability = normalDistribution.cumulativeProbability(Double.NEGATIVE_INFINITY);
        assertEquals(0, cumulativeProbability, 0.0001);

        cumulativeProbability = normalDistribution.cumulativeProbability(Double.NaN);
        assertEquals(Double.NaN, cumulativeProbability, 0.0001);

        cumulativeProbability = normalDistribution.cumulativeProbability(-1);
        assertEquals(0.15865525, cumulativeProbability, 0.0001);

        cumulativeProbability = normalDistribution.cumulativeProbability(1);
        assertEquals(0.84134475, cumulativeProbability, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        // Typical case
        double inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(0.5);
        assertEquals(0, inverseCumulativeProbability, 0.0001);

        // Edge cases
        inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(0);
        assertEquals(Double.NEGATIVE_INFINITY, inverseCumulativeProbability, 0.0001);

        inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(1);
        assertEquals(Double.POSITIVE_INFINITY, inverseCumulativeProbability, 0.0001);

        try {
            inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(1.5);
            fail("Expected org.apache.commons.math.exception.OutOfRangeException was not thrown.");
        } catch (org.apache.commons.math.exception.OutOfRangeException e) {
            assertTrue(true);
        }

        try {
            inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(-0.5);
            fail("Expected org.apache.commons.math.exception.OutOfRangeException was not thrown.");
        } catch (org.apache.commons.math.exception.OutOfRangeException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSample() throws MathException {
        // No appropriate way to test random data generation
        // However, we can test that the generated value is within the expected range

        double sample = normalDistribution.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        double solverAbsoluteAccuracy = normalDistribution.getSolverAbsoluteAccuracy();
        assertEquals(1e-9, solverAbsoluteAccuracy, 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        double domainLowerBound = normalDistribution.getDomainLowerBound(0.5);
        assertEquals(0, domainLowerBound, 0.0001);

        domainLowerBound = normalDistribution.getDomainLowerBound(0);
        assertEquals(-Double.MAX_VALUE, domainLowerBound, 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        double domainUpperBound = normalDistribution.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, domainUpperBound, 0.0001);

        domainUpperBound = normalDistribution.getDomainUpperBound(1);
        assertEquals(Double.MAX_VALUE, domainUpperBound, 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        double initialDomain = normalDistribution.getInitialDomain(0.5);
        assertEquals(1, initialDomain, 0.0001);

        initialDomain = normalDistribution.getInitialDomain(0);
        assertEquals(-0.5, initialDomain, 0.0001);

        initialDomain = normalDistribution.getInitialDomain(1);
        assertEquals(0.5, initialDomain, 0.0001);
    }
}