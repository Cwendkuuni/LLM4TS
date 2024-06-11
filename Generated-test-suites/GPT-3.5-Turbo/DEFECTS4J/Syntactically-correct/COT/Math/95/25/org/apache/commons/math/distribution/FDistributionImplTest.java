import org.junit.Test;
import org.junit.Assert;

public class FDistributionImplTest {

  @Test
  public void testCumulativeProbability() throws MathException {
    FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
    double cumulativeProbability = fDistribution.cumulativeProbability(0.5);
    Assert.assertEquals(0.452567763798, cumulativeProbability, 0.0001);
  }
  
  @Test
  public void testInverseCumulativeProbability() throws MathException {
    FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
    double inverseCumulativeProbability = fDistribution.inverseCumulativeProbability(0.8);
    Assert.assertEquals(0.99626924251, inverseCumulativeProbability, 0.0001);
  }
  
  @Test
  public void testGetDomainLowerBound() {
    FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
    double domainLowerBound = fDistribution.getDomainLowerBound(0.5);
    Assert.assertEquals(0.0, domainLowerBound, 0.0001);
  }
  
  @Test
  public void testGetDomainUpperBound() {
    FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
    double domainUpperBound = fDistribution.getDomainUpperBound(0.5);
    Assert.assertEquals(Double.MAX_VALUE, domainUpperBound, 0.0001);
  }
  
  @Test
  public void testGetInitialDomain() {
    FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
    double initialDomain = fDistribution.getInitialDomain(0.5);
    Assert.assertEquals(1.25, initialDomain, 0.0001);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSetNumeratorDegreesOfFreedomInvalid() {
    FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
    fDistribution.setNumeratorDegreesOfFreedom(0);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSetDenominatorDegreesOfFreedomInvalid() {
    FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
    fDistribution.setDenominatorDegreesOfFreedom(0);
  }
}

