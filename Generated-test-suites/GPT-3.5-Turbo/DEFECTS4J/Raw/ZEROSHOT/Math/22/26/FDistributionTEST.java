import org.junit.Test;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

import static org.junit.Assert.*;

public class FDistributionTest {
  
  @Test
  public void testDensity() {
    FDistribution fDist = new FDistribution(3, 7);
    assertEquals(0.012401922, fDist.density(0.2), 1e-6);
    assertEquals(0.072499186, fDist.density(0.5), 1e-6);
    assertEquals(0.142897309, fDist.density(1.0), 1e-6);
    assertEquals(0.179003087, fDist.density(1.5), 1e-6);
    assertEquals(0.179003087, fDist.density(2.0), 1e-6);
    assertEquals(0.151781618, fDist.density(2.5), 1e-6);
    assertEquals(0.111949774, fDist.density(3.0), 1e-6);
    assertEquals(0.071318398, fDist.density(3.5), 1e-6);
    assertEquals(0.04100099, fDist.density(4.0), 1e-6);
    assertEquals(0.021569874, fDist.density(4.5), 1e-6);
    assertEquals(0.011446867, fDist.density(5.0), 1e-6);
  }
  
  @Test
  public void testCumulativeProbability() {
    FDistribution fDist = new FDistribution(3, 7);
    assertEquals(0, fDist.cumulativeProbability(0), 1e-10);
    assertEquals(0.111949774, fDist.cumulativeProbability(1.5), 1e-6);
    assertEquals(0.394347228, fDist.cumulativeProbability(3.0), 1e-6);
    assertEquals(0.779391286, fDist.cumulativeProbability(6.0), 1e-6);
    assertEquals(0.959237212, fDist.cumulativeProbability(10.0), 1e-6);
    assertEquals(0.999984583, fDist.cumulativeProbability(100.0), 1e-6);
    assertEquals(1, fDist.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0);
  }
  
  @Test
  public void testGetNumeratorDegreesOfFreedom() {
    FDistribution fDist = new FDistribution(3, 7);
    assertEquals(3, fDist.getNumeratorDegreesOfFreedom(), 0.0);
  }
  
  @Test
  public void testGetDenominatorDegreesOfFreedom() {
    FDistribution fDist = new FDistribution(3, 7);
    assertEquals(7, fDist.getDenominatorDegreesOfFreedom(), 0.0);
  }
  
  @Test
  public void testGetNumericalMean() {
    FDistribution fDist = new FDistribution(3, 7);
    assertEquals(Double.NaN, fDist.getNumericalMean(), 0.0);
  }
  
  @Test
  public void testGetNumericalVariance() {
    FDistribution fDist = new FDistribution(3, 7);
    assertEquals(Double.NaN, fDist.getNumericalVariance(), 0.0);
  }
  
  @Test
  public void testGetSupportLowerBound() {
    FDistribution fDist = new FDistribution(3, 7);
    assertEquals(0, fDist.getSupportLowerBound(), 0.0);
  }
  
  @Test
  public void testGetSupportUpperBound() {
    FDistribution fDist = new FDistribution(3, 7);
    assertEquals(Double.POSITIVE_INFINITY, fDist.getSupportUpperBound(), 0.0);
  }
  
  @Test
  public void testIsSupportLowerBoundInclusive() {
    FDistribution fDist = new FDistribution(3, 7);
    assertTrue(fDist.isSupportLowerBoundInclusive());
  }
  
  @Test
  public void testIsSupportUpperBoundInclusive() {
    FDistribution fDist = new FDistribution(3, 7);
    assertFalse(fDist.isSupportUpperBoundInclusive());
  }
  
  @Test
  public void testIsSupportConnected() {
    FDistribution fDist = new FDistribution(3, 7);
    assertTrue(fDist.isSupportConnected());
  }
}