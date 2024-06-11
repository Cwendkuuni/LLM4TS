import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {

    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl();
    }

    @After
    public void tearDown() {
        normalDistribution = null;
    }

    @Test
    public void testGetMean() {
        double expected = 0.0;
        double actual = normalDistribution.getMean();
        Assert.assertEquals(expected, actual, 0);
    }

    @Test
    public void testSetMean() {
        double expected = 1.0;
        normalDistribution.setMean(expected);
        double actual = normalDistribution.getMean();
        Assert.assertEquals(expected, actual, 0);
    }

    @Test
    public void testGetStandardDeviation() {
        double expected = 1.0;
        double actual = normalDistribution.getStandardDeviation();
        Assert.assertEquals(expected, actual, 0);
    }

    @Test
    public void testSetStandardDeviation() {
        double expected = 2.0;
        normalDistribution.setStandardDeviation(expected);
        double actual = normalDistribution.getStandardDeviation();
        Assert.assertEquals(expected, actual, 0);
    }

    @Test
    public void testCumulativeProbability() {
        double x = 0.0;
        double expected = 0.5;
        try {
            double actual = normalDistribution.cumulativeProbability(x);
            Assert.assertEquals(expected, actual, 0);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        double p = 0.5;
        double expected = 0.0;
        try {
            double actual = normalDistribution.inverseCumulativeProbability(p);
            Assert.assertEquals(expected, actual, 0);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        double p = 0.5;
        double expected = 0.0;
        double actual = normalDistribution.getDomainLowerBound(p);
        Assert.assertEquals(expected, actual, 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        double p = 0.5;
        double expected = Double.MAX_VALUE;
        double actual = normalDistribution.getDomainUpperBound(p);
        Assert.assertEquals(expected, actual, 0);
    }

    @Test
    public void testGetInitialDomain() {
        double p = 0.5;
        double expected = 0.0;
        double actual = normalDistribution.getInitialDomain(p);
        Assert.assertEquals(expected, actual, 0);
    }
}