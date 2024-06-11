import org.apache.commons.math.MathException;
import org.junit.Assert;
import org.junit.Test;

public class FDistributionImplTest {

  @Test
  public void testCumulativeProbability() throws MathException {
    FDistributionImpl fDistribution = new FDistributionImpl(1.0, 1.0);
    Assert.assertEquals(0.0, fDistribution.cumulativeProbability(0.0), 0.0001);
    
    fDistribution.setNumeratorDegreesOfFreedom(2.0);
    fDistribution.setDenominatorDegreesOfFreedom(3.0);
    Assert.assertEquals(0.1667, fDistribution.cumulativeProbability(0.5), 0.0001);
    
    fDistribution.setNumeratorDegreesOfFreedom(3.0);
    fDistribution.setDenominatorDegreesOfFreedom(2.0);
    Assert.assertEquals(0.8333, fDistribution.cumulativeProbability(0.5), 0.0001);
  }

  @Test
  public void testInverseCumulativeProbability() throws MathException {
    FDistributionImpl fDistribution = new FDistributionImpl(1.0, 1.0);
    Assert.assertEquals(0.0, fDistribution.inverseCumulativeProbability(0.0), 0.0001);
    
    fDistribution.setNumeratorDegreesOfFreedom(2.0);
    fDistribution.setDenominatorDegreesOfFreedom(3.0);
    Assert.assertEquals(0.4086, fDistribution.inverseCumulativeProbability(0.5), 0.0001);
    
    fDistribution.setNumeratorDegreesOfFreedom(3.0);
    fDistribution.setDenominatorDegreesOfFreedom(2.0);
    double infValue = Double.POSITIVE_INFINITY;
    Assert.assertEquals(infValue, fDistribution.inverseCumulativeProbability(1.0), 0.0001);
  }

  @Test
  public void testGetDomainLowerBound() {
    FDistributionImpl fDistribution = new FDistributionImpl(1.0, 1.0);
    Assert.assertEquals(0.0, fDistribution.getDomainLowerBound(0.5), 0.0001);
  }

  @Test
  public void testGetDomainUpperBound() {
    FDistributionImpl fDistribution = new FDistributionImpl(1.0, 1.0);
    Assert.assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.5), 0.0001);
  }

  @Test
  public void testGetInitialDomain() {
    FDistributionImpl fDistribution = new FDistributionImpl(1.0, 1.0);
    Assert.assertEquals(1.0, fDistribution.getInitialDomain(0.5), 0.0001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetNumeratorDegreesOfFreedom() {
    FDistributionImpl fDistribution = new FDistributionImpl(1.0, 1.0);
    fDistribution.setNumeratorDegreesOfFreedom(-1.0);
  }

  @Test
  public void testGetNumeratorDegreesOfFreedom() {
    FDistributionImpl fDistribution = new FDistributionImpl(1.0, 1.0);
    Assert.assertEquals(1.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetDenominatorDegreesOfFreedom() {
    FDistributionImpl fDistribution = new FDistributionImpl(1.0, 1.0);
    fDistribution.setDenominatorDegreesOfFreedom(-1.0);
  }

  @Test
  public void testGetDenominatorDegreesOfFreedom() {
    FDistributionImpl fDistribution = new FDistributionImpl(1.0, 1.0);
    Assert.assertEquals(1.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
  }
}