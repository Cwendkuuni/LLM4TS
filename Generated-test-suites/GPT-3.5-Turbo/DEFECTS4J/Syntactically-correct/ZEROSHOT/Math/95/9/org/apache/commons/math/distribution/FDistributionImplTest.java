import org.junit.Assert;
import org.junit.Test;

public class FDistributionImplTest {
  
  @Test
  public void testCumulativeProbability() throws Exception {
    FDistributionImpl fDistribution = new FDistributionImpl(3.0, 2.0);
    double result = fDistribution.cumulativeProbability(4.0);
    Assert.assertEquals(0.972472501, result, 0.000001);
  }
  
  @Test
  public void testInverseCumulativeProbability() throws Exception {
    FDistributionImpl fDistribution = new FDistributionImpl(3.0, 2.0);
    double result = fDistribution.inverseCumulativeProbability(0.972472501);
    Assert.assertEquals(4.0, result, 0.000001);
  }
  
  @Test
  public void testGetDomainLowerBound() throws Exception {
    FDistributionImpl fDistribution = new FDistributionImpl(3.0, 2.0);
    double result = fDistribution.getDomainLowerBound(0.972472501);
    Assert.assertEquals(0.0, result, 0.000001);
  }
  
  @Test
  public void testGetDomainUpperBound() throws Exception {
    FDistributionImpl fDistribution = new FDistributionImpl(3.0, 2.0);
    double result = fDistribution.getDomainUpperBound(0.972472501);
    Assert.assertEquals(Double.MAX_VALUE, result, 0.000001);
  }
  
  @Test
  public void testGetInitialDomain() throws Exception {
    FDistributionImpl fDistribution = new FDistributionImpl(3.0, 2.0);
    double result = fDistribution.getInitialDomain(0.972472501);
    Assert.assertEquals(1.5, result, 0.000001);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSetNumeratorDegreesOfFreedom() throws Exception {
    FDistributionImpl fDistribution = new FDistributionImpl(3.0, 2.0);
    fDistribution.setNumeratorDegreesOfFreedom(-1.0);
  }
  
  @Test
  public void testGetNumeratorDegreesOfFreedom() throws Exception {
    FDistributionImpl fDistribution = new FDistributionImpl(3.0, 2.0);
    double result = fDistribution.getNumeratorDegreesOfFreedom();
    Assert.assertEquals(3.0, result, 0.000001);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSetDenominatorDegreesOfFreedom() throws Exception {
    FDistributionImpl fDistribution = new FDistributionImpl(3.0, 2.0);
    fDistribution.setDenominatorDegreesOfFreedom(-1.0);
  }
  
  @Test
  public void testGetDenominatorDegreesOfFreedom() throws Exception {
    FDistributionImpl fDistribution = new FDistributionImpl(3.0, 2.0);
    double result = fDistribution.getDenominatorDegreesOfFreedom();
    Assert.assertEquals(2.0, result, 0.000001);
  }
  
}