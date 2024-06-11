import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double expectedMean = distribution.getMean();
        Assert.assertEquals("Mean should be zero by default", 0.0, expectedMean, 0.001);

        distribution.setMean(5.0);
        expectedMean = distribution.getMean();
        Assert.assertEquals("Mean should be set correctly", 5.0, expectedMean, 0.001);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(3.14);
        double expectedMean = distribution.getMean();
        Assert.assertEquals("Mean should be set correctly", 3.14, expectedMean, 0.001);
    }
}