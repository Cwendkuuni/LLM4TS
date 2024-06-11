import static org.junit.Assert.*;
import org.junit.Test;

public class MannWhitneyUTestTest {

  @Test
  public void testMannWhitneyU() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
    double[] y = {6.0, 7.0, 8.0, 9.0, 10.0};
    try {
      double result = test.mannWhitneyU(x, y);
      assertEquals(5.0, result, 0.0);
    } catch (Exception e) {
      fail("Exception thrown");
    }
  }

  @Test
  public void testEnsureDataConformance() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
    double[] y = {6.0, 7.0, 8.0, 9.0, 10.0};
    try {
      test.ensureDataConformance(x, y);
    } catch (Exception e) {
      fail("Exception thrown");
    }
  }

  @Test(expected = NullArgumentException.class)
  public void testEnsureDataConformanceNull() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
    double[] y = null;
    test.ensureDataConformance(x, y);
  }

  @Test(expected = NoDataException.class)
  public void testEnsureDataConformanceZeroLength() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {};
    double[] y = {};
    test.ensureDataConformance(x, y);
  }

  @Test
  public void testConcatenateSamples() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1.0, 2.0, 3.0};
    double[] y = {4.0, 5.0, 6.0};
    double[] expected = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
    double[] result = test.concatenateSamples(x, y);
    assertArrayEquals(expected, result, 0.0);
  }

  @Test
  public void testCalculateAsymptoticPValue() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double Umin = 5.0;
    int n1 = 10;
    int n2 = 10;
    try {
      double result = test.calculateAsymptoticPValue(Umin, n1, n2);
      assertEquals(1.0, result, 0.0);
    } catch (Exception e) {
      fail("Exception thrown");
    }
  }

  @Test
  public void testMannWhitneyUTest() {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
    double[] y = {6.0, 7.0, 8.0, 9.0, 10.0};
    try {
      double result = test.mannWhitneyUTest(x, y);
      assertEquals(1.0, result, 0.0);
    } catch (Exception e) {
      fail("Exception thrown");
    }
  }

}