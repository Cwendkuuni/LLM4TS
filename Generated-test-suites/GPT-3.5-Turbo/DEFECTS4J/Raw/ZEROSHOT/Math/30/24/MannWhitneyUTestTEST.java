import org.junit.*;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

  @Test
  public void testMannWhitneyU() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1, 2, 3, 4, 5};
    double[] y = {6, 7, 8, 9, 10};
    double result = test.mannWhitneyU(x, y);
    assertEquals(0, result, 0.0001);
  }

  @Test(expected = NullArgumentException.class)
  public void testMannWhitneyU_NullArgumentException() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = null;
    double[] y = {1, 2, 3};
    test.mannWhitneyU(x, y);
  }

  @Test(expected = NoDataException.class)
  public void testMannWhitneyU_NoDataException() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {};
    double[] y = {1, 2, 3};
    test.mannWhitneyU(x, y);
  }

  @Test
  public void testCalculateAsymptoticPValue() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double result = test.calculateAsymptoticPValue(10, 5, 5);
    assertEquals(0.8821, result, 0.0001);
  }

  @Test
  public void testCalculateAsymptoticPValue_MaxCountExceededException() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    try {
      double result = test.calculateAsymptoticPValue(10, 0, 0);
      fail("Should have thrown MaxCountExceededException");
    } catch (MaxCountExceededException e) {
      // Expected exception
    }
  }

  @Test
  public void testMannWhitneyUTest() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1, 2, 3, 4, 5};
    double[] y = {6, 7, 8, 9, 10};
    double result = test.mannWhitneyUTest(x, y);
    assertEquals(0.235, result, 0.0001);
  }

  @Test(expected = NullArgumentException.class)
  public void testMannWhitneyUTest_NullArgumentException() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = null;
    double[] y = {1, 2, 3};
    test.mannWhitneyUTest(x, y);
  }

  @Test(expected = NoDataException.class)
  public void testMannWhitneyUTest_NoDataException() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {};
    double[] y = {1, 2, 3};
    test.mannWhitneyUTest(x, y);
  }

  @Test
  public void testMannWhitneyUTest_ConvergenceException() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1, 2, 3};
    double[] y = {4, 5, 6};
    try {
      double result = test.mannWhitneyUTest(x, y);
      fail("Should have thrown ConvergenceException");
    } catch (ConvergenceException e) {
      // Expected exception
    } catch (Exception e) {
      fail("Should have thrown ConvergenceException");
    }
  }

}