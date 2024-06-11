
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.FDistributionImpl;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        Assert.assertEquals(0.022808610461160106, fDistribution.cumulativeProbability(1.0), 0.0001);
        Assert.assertEquals(0.09148956269264013, fDistribution.cumulativeProbability(1.5), 0.0001);
        Assert.assertEquals(0.2538097766913104, fDistribution.cumulativeProbability(2.0), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        Assert.assertEquals(0.92604768995, fDistribution.inverseCumulativeProbability(0.8), 0.0001);
        Assert.assertEquals(2.866421063535461, fDistribution.inverseCumulativeProbability(0.95), 0.0001);
        Assert.assertEquals(8.403737447934949, fDistribution.inverseCumulativeProbability(0.999), 0.0001);
    }

    @Test
    public void testNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        Assert.assertEquals(2.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
        fDistribution.setNumeratorDegreesOfFreedom(4.0);
        Assert.assertEquals(4.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        Assert.assertEquals(3.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
        fDistribution.setDenominatorDegreesOfFreedom(5.0);
        Assert.assertEquals(5.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
    }

}
