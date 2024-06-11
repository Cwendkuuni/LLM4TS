import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.junit.Assert;
import org.junit.Test;

public class MannWhitneyUTestTest {

  // Test if the constructor initializes the naturalRanking object
  @Test public void testConstructor() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    Assert.assertNotNull(test.getNaturalRanking());
  }

  // Test if the naturalRanking object is correctly initialized with the given strategies
  @Test public void testConstructorWithStrategies() {
    MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.FIXED, TiesStrategy.AVERAGE);
    Assert.assertNotNull(test.getNaturalRanking());
    Assert.assertEquals(NaNStrategy.FIXED, test.getNaturalRanking().getNanStrategy());
    Assert.assertEquals(TiesStrategy.AVERAGE, test.getNaturalRanking().getTiesStrategy());
  }

  // Test if a NullArgumentException is thrown when either x or y is null
  @Test(expected = NullArgumentException.class) public void testNullArguments() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = null;
    double[] y = {1, 2, 3};
    test.mannWhitneyU(x, y);
  }

  // Test if a NoDataException is thrown when either x or y is empty
  @Test(expected = NoDataException.class) public void testEmptyArrays() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {};
    double[] y = {1, 2, 3};
    test.mannWhitneyU(x, y);
  }

  // Test if the result of mannWhitneyU is correct for two samples with different lengths
  @Test public void testMannWhitneyU() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1, 2, 3};
    double[] y = {4, 5, 6, 7};
    Assert.assertEquals(10.0, test.mannWhitneyU(x, y), 0.0001);
  }

  // Test if the result of mannWhitneyU is correct for two samples with equal lengths
  @Test public void testMannWhitneyUWithEqualLengths() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1, 2, 3};
    double[] y = {4, 5, 6};
    Assert.assertEquals(6.0, test.mannWhitneyU(x, y), 0.0001);
  }

  // Test if a ConvergenceException is thrown when the p-value calculation does not converge
  @Test(expected = ConvergenceException.class) public void testConvergenceException() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1, 2, 3};
    double[] y = {4, 5, 6, 7};
    test.mannWhitneyUTest(x, y);
  }

  // Test if the result of mannWhitneyUTest is correct for two samples with different lengths
  @Test public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1, 2, 3};
    double[] y = {4, 5, 6, 7};
    Assert.assertEquals(0.2389, test.mannWhitneyUTest(x, y), 0.0001);
  }

  // Test if the result of mannWhitneyUTest is correct for two samples with equal lengths
  @Test public void testMannWhitneyUTestWithEqualLengths() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1, 2, 3};
    double[] y = {4, 5, 6};
    Assert.assertEquals(0.5, test.mannWhitneyUTest(x, y), 0.0001);
  }

}