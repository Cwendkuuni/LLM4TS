import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionTest {
  
  @Test
  public void testDensity() {
    FDistribution fd = new FDistribution(5.0, 10.0);
    assertEquals(0.4283937437061913, fd.density(2.0), 1e-15);
  }
  
  @Test
  public void testCumulativeProbability() {
    FDistribution fd = new FDistribution(5.0, 10.0);
    assertEquals(0.007554929780163929, fd.cumulativeProbability(2.0), 1e-15);
  }
  
  @Test
  public void testGetNumeratorDegreesOfFreedom() {
    FDistribution fd = new FDistribution(5.0, 10.0);
    assertEquals(5.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
  }
  
  @Test
  public void testGetDenominatorDegreesOfFreedom() {
    FDistribution fd = new FDistribution(5.0, 10.0);
    assertEquals(10.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
  }
  
  @Test
  public void testGetSupportLowerBound() {
    FDistribution fd = new FDistribution(5.0, 10.0);
    assertEquals(0.0, fd.getSupportLowerBound(), 0.0);
  }
  
  @Test
  public void testGetSupportUpperBound() {
    FDistribution fd = new FDistribution(5.0, 10.0);
    assertEquals(Double.POSITIVE_INFINITY, fd.getSupportUpperBound(), 0.0);
  }
}