import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

    private FDistributionImpl fDistribution;

    @Before
    public void setUp() {
        fDistribution = new FDistributionImpl(3.0, 4.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        // Test for a positive value of x
        double x = 1.5;
        double expected = 0.8106995230524004;
        double actual = fDistribution.cumulativeProbability(x);
        assertEquals(expected, actual, 0.0001);

        // Test for x = 0
        x = 0.0;
        expected = 0.0;
        actual = fDistribution.cumulativeProbability(x);
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        // Test for p = 0
        double p = 0.0;
        double expected = 0.0;
        double actual = fDistribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.0001);

        // Test for p = 1
        p = 1.0;
        expected = Double.POSITIVE_INFINITY;
        actual = fDistribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.0001);

        // Test for p = 0.5
        p = 0.5;
        expected = 1.278820596099706;
        actual = fDistribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        // Test for p = 0
        double p = 0.0;
        double expected = 0.0;
        double actual = fDistribution.getDomainLowerBound(p);
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        // Test for p = 0
        double p = 0.0;
        double expected = Double.MAX_VALUE;
        double actual = fDistribution.getDomainUpperBound(p);
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        // Test for p = 0
        double p = 0.0;
        double expected = 0.5714285714285714;
        double actual = fDistribution.getInitialDomain(p);
        assertEquals(expected, actual, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        // Test for negative degrees of freedom
        double degreesOfFreedom = -1.0;
        fDistribution.setNumeratorDegreesOfFreedom(degreesOfFreedom);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        // Test for negative degrees of freedom
        double degreesOfFreedom = -1.0;
        fDistribution.setDenominatorDegreesOfFreedom(degreesOfFreedom);
    }
}


@RunWith(JUnitParamsRunner.class)
public class FDistributionImplTest {
    
  @Before
  public void setUp() {
      // Initialization
  }
    
  @Test
  public void testCumulativeProbability() throws MathException {
      // Basic positive value of x
      double x = 1.5;
      double expectedResult = 0.8106995230524004;
      double actualResult = fDistribution.cumulativeProbability(x);
      Assert.assertEquals(expectedResult, actualResult, 0.0001);

      // x = 0
      x = 0.0;
      expectedResult = 0.0;
      actualResult = fDistribution.cumulativeProbability(x);
      Assert.assertEquals(expectedResult, actualResult, 0.0001);
  }
  
  @Test
  public void testInverseCumulativeProbability() throws MathException {
      // p = 0
      double p = 0.0;
      double expectedResult = 0.0;
      double actualResult = fDistribution.inverseCumulativeProbability(p);
      Assert.assertEquals(expectedResult, actualResult, 0.0001);

      // p = 1
      p = 1.0;
      expectedResult = Double.POSITIVE_INFINITY;
      actualResult = fDistribution.inverseCumulativeProbability(p);
      Assert.assertEquals(expectedResult, actualResult, 0.0001);

      // p = 0.5
      p = 0.5;
      expectedResult = 1.278820596099706;
      actualResult = fDistribution.inverseCumulativeProbability(p);
      Assert.assertEquals(expectedResult, actualResult, 0.0001);
  }
  
  @Test
  public void testGetDomainLowerBound() {
      // p = 0
      double p = 0.0;
      double expectedResult = 0.0;
      double actualResult = fDistribution.getDomainLowerBound(p);
      Assert.assertEquals(expectedResult, actualResult, 0.0001);
  }
  
  @Test
  public void testGetDomainUpperBound() {
      // p = 0
      double p = 0.0;
      double expectedResult = Double.MAX_VALUE;
      double actualResult = fDistribution.getDomainUpperBound(p);
      Assert.assertEquals(expectedResult, actualResult, 0.0001);
  }
  
  @Test
  public void testGetInitialDomain() {
      // p = 0
      double p = 0.0;
      double expectedResult = 0.5714285714285714;
      double actualResult = fDistribution.getInitialDomain(p);
      Assert.assertEquals(expectedResult, actualResult, 0.0001);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSetNumeratorDegreesOfFreedom() {
      // degreesOfFreedom is negative
      double degreesOfFreedom = -1.0;
      fDistribution.setNumeratorDegreesOfFreedom(degreesOfFreedom);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSetDenominatorDegreesOfFreedom() {
      // degreesOfFreedom is negative
      double degreesOfFreedom = -1.0;
      fDistribution.setDenominatorDegreesOfFreedom(degreesOfFreedom);
  }
}
