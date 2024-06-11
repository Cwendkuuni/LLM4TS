
import static org.junit.Assert.*;
import org.junit.Test;

public class MannWhitneyUTestTest {

  @Test
  public void testMannWhitneyU() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1, 2, 3};
    double[] y = {4, 5, 6};
    double expected = 9.5;
    double result = test.mannWhitneyU(x, y);
    assertEquals(expected, result, 0.001);
  }

  @Test(expected = NullArgumentException.class)
  public void testMannWhitneyUWithNullArguments() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    test.mannWhitneyU(null, null);
  }

  @Test(expected = NoDataException.class)
  public void testMannWhitneyUWithZeroLengthArrays() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {};
    double[] y = {};
    test.mannWhitneyU(x, y);
  }

  @Test
  public void testCalculateAsymptoticPValue() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double Umin = 2.0;
    int n1 = 3;
    int n2 = 4;
    double expected = 0.440;
    double result = test.calculateAsymptoticPValue(Umin, n1, n2);
    assertEquals(expected, result, 0.001);
  }

  @Test
  public void testMannWhitneyUTest() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1, 2, 3};
    double[] y = {4, 5, 6};
    double expected = 0.440;
    double result = test.mannWhitneyUTest(x, y);
    assertEquals(expected, result, 0.001);
  }

}
