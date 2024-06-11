import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.junit.Before;
import org.junit.Test;

public class ChiSquareTestImplTest {
  
  private ChiSquareTestImpl chiSquareTestImpl;
  private ChiSquaredDistribution chiSquaredDistribution;

  @Before
  public void setUp() {
    chiSquaredDistribution = new ChiSquaredDistributionImpl(1.0);
    chiSquareTestImpl = new ChiSquareTestImpl(chiSquaredDistribution);
  }

  @Test
  public void testChiSquare() {
    double[] expected = {1, 2, 3};
    long[] observed = {1, 2, 3};
    assertEquals(0.0, chiSquareTestImpl.chiSquare(expected, observed), 0.0001);
  }

  @Test
  public void testChiSquareTest() throws MathException {
    double[] expected = {1, 2, 3};
    long[] observed = {1, 2, 3};
    double pValue = chiSquareTestImpl.chiSquareTest(expected, observed);
    assertTrue(pValue >= 0 && pValue <= 1);
  }

  @Test
  public void testChiSquareTestAlpha() throws MathException {
    double[] expected = {1, 2, 3};
    long[] observed = {1, 2, 3};
    double alpha = 0.05;
    assertFalse(chiSquareTestImpl.chiSquareTest(expected, observed, alpha));
  }

  @Test
  public void testChiSquareLongArrayArray() {
    long[][] counts = {{1, 2, 3}, {4, 5, 6}};
    assertEquals(0.0, chiSquareTestImpl.chiSquare(counts), 0.0001);
  }

  @Test
  public void testChiSquareTestLongArrayArray() throws MathException {
    long[][] counts = {{1, 2, 3}, {4, 5, 6}};
    double pValue = chiSquareTestImpl.chiSquareTest(counts);
    assertTrue(pValue >= 0 && pValue <= 1);
  }

  @Test
  public void testChiSquareTestLongArrayArrayAlpha() throws MathException {
    long[][] counts = {{1, 2, 3}, {4, 5, 6}};
    double alpha = 0.05;
    assertFalse(chiSquareTestImpl.chiSquareTest(counts, alpha));
  }

  @Test
  public void testChiSquareDataSetsComparison() {
    long[] observed1 = {1, 2, 3};
    long[] observed2 = {1, 2, 3};
    assertEquals(0.0, chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2), 0.0001);
  }

  @Test
  public void testChiSquareTestDataSetsComparison() throws MathException {
    long[] observed1 = {1, 2, 3};
    long[] observed2 = {1, 2, 3};
    double pValue = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
    assertTrue(pValue >= 0 && pValue <= 1);
  }

  @Test
  public void testChiSquareTestDataSetsComparisonAlpha() throws MathException {
    long[] observed1 = {1, 2, 3};
    long[] observed2 = {1, 2, 3};
    double alpha = 0.05;
    assertFalse(chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, alpha));
  }
  
}