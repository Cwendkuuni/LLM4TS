import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HypergeometricDistributionTest {
  
  private HypergeometricDistribution hypergeometricDistribution;
  
  @Before
  public void setUp() {
    int populationSize = 10;
    int numberOfSuccesses = 4;
    int sampleSize = 3;

    hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
  }
  
  @Test
  public void testProbability() {
    Assert.assertEquals(0.0, hypergeometricDistribution.probability(-1), 0.0);
    Assert.assertEquals(0.0, hypergeometricDistribution.probability(11), 0.0);
    Assert.assertEquals(10.0 / CombinatoricsUtils.binomialCoefficient(10, 3), hypergeometricDistribution.probability(3), 0.0);
    Assert.assertEquals(7.0 / CombinatoricsUtils.binomialCoefficient(10, 3), hypergeometricDistribution.probability(2), 0.0);
  }
  
  @Test
  public void testCumulativeProbability() {
    Assert.assertEquals(0.0, hypergeometricDistribution.cumulativeProbability(-1), 0.0);
    Assert.assertEquals(0.0, hypergeometricDistribution.cumulativeProbability(11), 0.0);
    Assert.assertEquals(1.0, hypergeometricDistribution.cumulativeProbability(3), 0.0);
    Assert.assertEquals(MathUtils.binomialCoefficient(10, 3) - MathUtils.binomialCoefficient(7, 2), hypergeometricDistribution.cumulativeProbability(2), 0.0);
  }
  
  @Test
  public void testUpperCumulativeProbability() {
    Assert.assertEquals(1.0, hypergeometricDistribution.upperCumulativeProbability(-1), 0.0);
    Assert.assertEquals(1.0, hypergeometricDistribution.upperCumulativeProbability(11), 0.0);
    Assert.assertEquals(0.0, hypergeometricDistribution.upperCumulativeProbability(3), 0.0);
    Assert.assertEquals(MathUtils.binomialCoefficient(7, 2) + 1, hypergeometricDistribution.upperCumulativeProbability(2), 0.0);
  }
  
  @Test
  public void testGetNumericalMean() {
    Assert.assertEquals(1.2, hypergeometricDistribution.getNumericalMean(), 0.0);
  }
  
  @Test
  public void testGetNumericalVariance() {
    Assert.assertEquals(0.96, hypergeometricDistribution.getNumericalVariance(), 0.0);
  }
  
  @Test
  public void testGetSupportLowerBound() {
    Assert.assertEquals(0, hypergeometricDistribution.getSupportLowerBound());
  }
  
  @Test
  public void testGetSupportUpperBound() {
    Assert.assertEquals(3, hypergeometricDistribution.getSupportUpperBound());
  }
  
  @Test
  public void testIsSupportConnected() {
    Assert.assertTrue(hypergeometricDistribution.isSupportConnected());
  }
  
  @Test(expected = NotStrictlyPositiveException.class)
  public void testNegativePopulationSize() {
    hypergeometricDistribution = new HypergeometricDistribution(-1, 4, 3);
  }
  
  @Test(expected = NotPositiveException.class)
  public void testNegativeNumberOfSuccesses() {
    hypergeometricDistribution = new HypergeometricDistribution(10, -4, 3);
  }
  
  @Test(expected = NotPositiveException.class)
  public void testNegativeSampleSize() {
    hypergeometricDistribution = new HypergeometricDistribution(10, 4, -3);
  }
  
  @Test(expected = NumberIsTooLargeException.class)
  public void testNumberOfSuccessesGreaterThanPopulationSize() {
    hypergeometricDistribution = new HypergeometricDistribution(4, 10, 3);
  }
  
  @Test(expected = NumberIsTooLargeException.class)
  public void testSampleSizeGreaterThanPopulationSize() {
    hypergeometricDistribution = new HypergeometricDistribution(10, 4, 11);
  }
}