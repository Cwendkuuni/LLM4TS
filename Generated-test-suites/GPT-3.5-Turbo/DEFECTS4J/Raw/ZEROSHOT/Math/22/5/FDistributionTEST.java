import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionTest {
  
  @Test
  public void testDensity() {
    FDistribution fd = new FDistribution(3, 6);
    assertEquals(0.0035278564806237727, fd.density(0.5), 1e-15);
    assertEquals(0.17885331404327148, fd.density(1.5), 1e-15);
    assertEquals(0.1846424306835631, fd.density(2.5), 1e-15);
  }
  
  @Test
  public void testCumulativeProbability() {
    FDistribution fd = new FDistribution(3, 6);
    assertEquals(0.16538851043710075, fd.cumulativeProbability(0.5), 1e-15);
    assertEquals(0.6771761442475796, fd.cumulativeProbability(1.5), 1e-15);
    assertEquals(0.7982123552424965, fd.cumulativeProbability(2.5), 1e-15);
  }
  
  @Test
  public void testGetNumeratorDegreesOfFreedom() {
    FDistribution fd = new FDistribution(3, 6);
    assertEquals(3.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
  }
  
  @Test
  public void testGetDenominatorDegreesOfFreedom() {
    FDistribution fd = new FDistribution(3, 6);
    assertEquals(6.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
  }
  
  @Test
  public void testGetNumericalMean() {
    FDistribution fd = new FDistribution(3, 6);
    assertEquals(1.5, fd.getNumericalMean(), 0.0);
  }
  
  @Test
  public void testGetNumericalVariance() {
    FDistribution fd = new FDistribution(3, 6);
    assertEquals(9.0, fd.getNumericalVariance(), 0.0);
  }
  
  @Test
  public void testGetSupportLowerBound() {
    FDistribution fd = new FDistribution(3, 6);
    assertEquals(0.0, fd.getSupportLowerBound(), 0.0);
  }
  
  @Test
  public void testGetSupportUpperBound() {
    FDistribution fd = new FDistribution(3, 6);
    assertEquals(Double.POSITIVE_INFINITY, fd.getSupportUpperBound(), 0.0);
  }
  
  @Test
  public void testIsSupportLowerBoundInclusive() {
    FDistribution fd = new FDistribution(3, 6);
    assertEquals(true, fd.isSupportLowerBoundInclusive());
  }
  
  @Test
  public void testIsSupportUpperBoundInclusive() {
    FDistribution fd = new FDistribution(3, 6);
    assertEquals(false, fd.isSupportUpperBoundInclusive());
  }
  
  @Test
  public void testIsSupportConnected() {
    FDistribution fd = new FDistribution(3, 6);
    assertEquals(true, fd.isSupportConnected());
  }
}