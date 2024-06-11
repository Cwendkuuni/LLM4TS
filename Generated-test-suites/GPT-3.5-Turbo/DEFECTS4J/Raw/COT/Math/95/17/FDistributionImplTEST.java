import org.junit.Assert;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        double x = 2.0;
        double expectedResult = 0.686;
        double actualResult = fDistribution.cumulativeProbability(x);
        Assert.assertEquals(expectedResult, actualResult, 0.001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        double p = 0.5;
        double expectedResult = 0.8642114081362537;
        double actualResult = fDistribution.inverseCumulativeProbability(p);
        Assert.assertEquals(expectedResult, actualResult, 0.001);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        double degreesOfFreedom = 5;
        fDistribution.setNumeratorDegreesOfFreedom(degreesOfFreedom);
        Assert.assertEquals(degreesOfFreedom, fDistribution.getNumeratorDegreesOfFreedom(), 0.001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        double expectedResult = 3;
        double actualResult = fDistribution.getNumeratorDegreesOfFreedom();
        Assert.assertEquals(expectedResult, actualResult, 0.001);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        double degreesOfFreedom = 6;
        fDistribution.setDenominatorDegreesOfFreedom(degreesOfFreedom);
        Assert.assertEquals(degreesOfFreedom, fDistribution.getDenominatorDegreesOfFreedom(), 0.001);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        double expectedResult = 4;
        double actualResult = fDistribution.getDenominatorDegreesOfFreedom();
        Assert.assertEquals(expectedResult, actualResult, 0.001);
    }
}