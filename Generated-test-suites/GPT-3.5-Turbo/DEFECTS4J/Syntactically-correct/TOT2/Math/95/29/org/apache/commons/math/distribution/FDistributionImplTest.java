import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;

public class FDistributionImplTest {

  @Test
  public void testCumulativeProbability() throws MathException {
    FDistributionImpl fdistribution = new FDistributionImpl(5.0, 10.0);
    assertEquals(0.0, fdistribution.cumulativeProbability(-1.0), 0.0001);
    assertEquals(0.9474, fdistribution.cumulativeProbability(1.0), 0.0001);
  }

  @Test
  public void testInverseCumulativeProbability() throws MathException {
    FDistributionImpl fdistribution = new FDistributionImpl(5.0, 10.0);
    assertEquals(0.0, fdistribution.inverseCumulativeProbability(0.0), 0.0001);
    assertEquals(Double.POSITIVE_INFINITY, fdistribution.inverseCumulativeProbability(1.0), 0.0001);
  }

  @Test
  public void testSetNumeratorDegreesOfFreedom() {
    FDistributionImpl fdistribution = new FDistributionImpl(5.0, 10.0);
    fdistribution.setNumeratorDegreesOfFreedom(6.0);
    assertEquals(6.0, fdistribution.getNumeratorDegreesOfFreedom(), 0.0001);
  }

  @Test
  public void testSetDenominatorDegreesOfFreedom() {
    FDistributionImpl fdistribution = new FDistributionImpl(5.0, 10.0);
    fdistribution.setDenominatorDegreesOfFreedom(12.0);
    assertEquals(12.0, fdistribution.getDenominatorDegreesOfFreedom(), 0.0001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetNumeratorDegreesOfFreedomWithInvalidValue() {
    FDistributionImpl fdistribution = new FDistributionImpl(5.0, 10.0);
    fdistribution.setNumeratorDegreesOfFreedom(-1.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetDenominatorDegreesOfFreedomWithInvalidValue() {
    FDistributionImpl fdistribution = new FDistributionImpl(5.0, 10.0);
    fdistribution.setDenominatorDegreesOfFreedom(-1.0);
  }
}