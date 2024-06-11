import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

  @Test
  public void testMannWhitneyU() {
    MannWhitneyUTest test = new MannWhitneyUTest();

    double[] x = {1, 2, 3, 4, 5};
    double[] y = {6, 7, 8, 9, 10};

    double expectedU = 0.0;
    double actualU = test.mannWhitneyU(x, y);

    assertEquals(expectedU, actualU, 0.0001);
  }

  @Test
  public void testConcatenateSamples() {
    MannWhitneyUTest test = new MannWhitneyUTest();

    double[] x = {1, 2, 3, 4, 5};
    double[] y = {6, 7, 8, 9, 10};

    double[] expectedConcat = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    double[] actualConcat = test.concatenateSamples(x, y);

    assertArrayEquals(expectedConcat, actualConcat, 0.0001);
  }

  @Test
  public void testCalculateAsymptoticPValue() {
    MannWhitneyUTest test = new MannWhitneyUTest();

    double Umin = 0.0;
    int n1 = 5;
    int n2 = 5;

    double expectedPValue = 1.0;
    double actualPValue = test.calculateAsymptoticPValue(Umin, n1, n2);

    assertEquals(expectedPValue, actualPValue, 0.0001);
  }

  @Test
  public void testMannWhitneyUTest() {
    MannWhitneyUTest test = new MannWhitneyUTest();

    double[] x = {1, 2, 3, 4, 5};
    double[] y = {6, 7, 8, 9, 10};

    double expectedPValue = 1.0;
    double actualPValue = test.mannWhitneyUTest(x, y);

    assertEquals(expectedPValue, actualPValue, 0.0001);
  }
}