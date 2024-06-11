import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {

    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl(0, 1);
    }

    @Test
    public void testGetMean() {
        Assert.assertEquals(0, normalDistribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        Assert.assertEquals(1, normalDistribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        double x = 1;
        double expectedDensity = 0.24197072451914337;
        Assert.assertEquals(expectedDensity, normalDistribution.density(x), 0.000001);
    }

    @Test
    public void testDensity_BoundaryCase() {
        double x = 0;
        double expectedDensity = 0.3989422804014327;
        Assert.assertEquals(expectedDensity, normalDistribution.density(x), 0.000001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        double x = 1;
        double expectedCumulativeProbability = 0.8413447460685429;
        Assert.assertEquals(expectedCumulativeProbability, normalDistribution.cumulativeProbability(x), 0.000001);
    }

    @Test
    public void testCumulativeProbability_BoundaryCase() throws MathException {
        double x = 0;
        double expectedCumulativeProbability = 0.5;
        Assert.assertEquals(expectedCumulativeProbability, normalDistribution.cumulativeProbability(x), 0.000001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testCumulativeProbability_ExceptionCase() throws MathException {
        double x = Double.MAX_VALUE;
        normalDistribution.cumulativeProbability(x);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double p = 0.5;
        double expectedInverseCumulativeProbability = 0;
        Assert.assertEquals(expectedInverseCumulativeProbability, normalDistribution.inverseCumulativeProbability(p), 0.000001);
    }

    @Test
    public void testInverseCumulativeProbability_BoundaryCase() throws MathException {
        double p = 0;
        double expectedInverseCumulativeProbability = Double.NEGATIVE_INFINITY;
        Assert.assertEquals(expectedInverseCumulativeProbability, normalDistribution.inverseCumulativeProbability(p), 0.000001);
    }

    @Test
    public void testInverseCumulativeProbability_ExceptionCase() throws MathException {
        double p = 2;
        double expectedInverseCumulativeProbability = Double.POSITIVE_INFINITY;
        Assert.assertEquals(expectedInverseCumulativeProbability, normalDistribution.inverseCumulativeProbability(p), 0.000001);
    }

    @Test
    public void testSample() throws MathException {
        double sample = normalDistribution.sample();
        Assert.assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }

    @Test
    public void testGetDomainLowerBound() {
        double p = 0.4;
        double expectedDomainLowerBound = Double.NEGATIVE_INFINITY;
        Assert.assertEquals(expectedDomainLowerBound, normalDistribution.getDomainLowerBound(p), 0.000001);
    }

    @Test
    public void testGetDomainUpperBound() {
        double p = 0.6;
        double expectedDomainUpperBound = Double.POSITIVE_INFINITY;
        Assert.assertEquals(expectedDomainUpperBound, normalDistribution.getDomainUpperBound(p), 0.000001);
    }

    @Test
    public void testGetInitialDomain() {
        double p = 0.5;
        double expectedInitialDomain = 0;
        Assert.assertEquals(expectedInitialDomain, normalDistribution.getInitialDomain(p), 0.000001);
    }

}