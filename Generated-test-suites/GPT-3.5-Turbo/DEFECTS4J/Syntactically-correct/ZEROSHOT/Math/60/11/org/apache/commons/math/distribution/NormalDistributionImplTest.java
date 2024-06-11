import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    private static NormalDistributionImpl normalDistribution;

    @BeforeClass
    public static void setUp() {
        normalDistribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        assertEquals(0, normalDistribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, normalDistribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        assertEquals(0.3989422804014327, normalDistribution.density(0), 1e-9);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, normalDistribution.cumulativeProbability(0), 1e-9);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(0, normalDistribution.inverseCumulativeProbability(0), 1e-9);
    }

    @Test
    public void testSample() throws MathException {
        double sample = normalDistribution.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0), 0);
    }

    @Test
    public void testGetInitialDomain() {
        assertEquals(-1, normalDistribution.getInitialDomain(0), 0);
    }
}