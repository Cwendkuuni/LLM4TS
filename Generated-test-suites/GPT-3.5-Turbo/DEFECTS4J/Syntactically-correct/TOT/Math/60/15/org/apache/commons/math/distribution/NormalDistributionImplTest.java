import org.junit.*;

public class NormalDistributionImplTest {

    private static final double EPSILON = 1e-9;

    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl(0, 1);
    }

    @Test
    public void testGetMean() {
        Assert.assertEquals(0, normalDistribution.getMean(), EPSILON);
    }

    @Test
    public void testGetStandardDeviation() {
        Assert.assertEquals(1, normalDistribution.getStandardDeviation(), EPSILON);
    }

    @Test
    public void testDensity() {
        double density = normalDistribution.density(0);
        Assert.assertEquals(0.3989422804014327, density, EPSILON);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        double cumulativeProbability = normalDistribution.cumulativeProbability(0);
        Assert.assertEquals(0.5, cumulativeProbability, EPSILON);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(0.5);
        Assert.assertEquals(0, inverseCumulativeProbability, EPSILON);
    }

    @Test
    public void testSample() throws MathException {
        double sample = normalDistribution.sample();
        Assert.assertNotNull(sample);
    }

}