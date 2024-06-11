import org.junit.Assert;
import org.junit.Test;

public class MannWhitneyUTestTest {
  
  @Test
  public void testConstructor_Default() {
      MannWhitneyUTest testObj = new MannWhitneyUTest();
      Assert.assertNotNull(testObj);
  }
  
  @Test
  public void testConstructor_Custom() {
      MannWhitneyUTest testObj = new MannWhitneyUTest( NaNStrategy.FIXED, TiesStrategy.AVERAGE);
      Assert.assertNotNull(testObj);
  }
  
  @Test(expected = NullArgumentException.class)
  public void testEnsureDataConformance_NullX_ThrowsNullArgumentException() {
      MannWhitneyUTest testObj = new MannWhitneyUTest();
      testObj.ensureDataConformance(null, new double[]{});
  }
  
  @Test(expected = NullArgumentException.class)
  public void testEnsureDataConformance_NullY_ThrowsNullArgumentException() {
      MannWhitneyUTest testObj = new MannWhitneyUTest();
      testObj.ensureDataConformance(new double[]{}, null);
  }
  
  @Test(expected = NoDataException.class)
  public void testEnsureDataConformance_ZeroLengthX_ThrowsNoDataException() {
      MannWhitneyUTest testObj = new MannWhitneyUTest();
      testObj.ensureDataConformance(new double[]{}, new double[]{2.3, 4.1});
  }
  
  @Test(expected = NoDataException.class)
  public void testEnsureDataConformance_ZeroLengthY_ThrowsNoDataException() {
      MannWhitneyUTest testObj = new MannWhitneyUTest();
      testObj.ensureDataConformance(new double[]{2.3, 4.1}, new double[]{});
  }
  
  @Test
  public void testConcatenateSamples() {
      MannWhitneyUTest testObj = new MannWhitneyUTest();
      double[] x = new double[]{2.1, 4.3, 6.6};
      double[] y = new double[]{1.2, 3.4, 5.6};
      double[] expected = new double[]{2.1, 4.3, 6.6, 1.2, 3.4, 5.6};
      double[] actual = testObj.concatenateSamples(x,y);
      Assert.assertArrayEquals(expected, actual, 0.001);
  }
  
  @Test
  public void testMannWhitneyU() {
      MannWhitneyUTest testObj = new MannWhitneyUTest();
      double[] x = new double[]{0.5, 2.0, 3.7, 5.2};
      double[] y = new double[]{1.4, 2.6, 4.8};
      double expected = 15;
      double actual = testObj.mannWhitneyU(x,y);
      Assert.assertEquals(expected, actual, 0.001);
  }
  
  @Test
  public void testCalculateAsymptoticPValue() {
      MannWhitneyUTest testObj = new MannWhitneyUTest();
      double Umin = 10;
      int n1 = 4;
      int n2 = 3;
      double expected = 0.212;
      double actual = testObj.calculateAsymptoticPValue(Umin, n1, n2);
      Assert.assertEquals(expected, actual, 0.001);
  }
  
  @Test
  public void testMannWhitneyUTest() {
      MannWhitneyUTest testObj = new MannWhitneyUTest();
      double[] x = new double[]{0.5, 2.0, 3.7, 5.2};
      double[] y = new double[]{1.4, 2.6, 4.8};
      double expected = 0.221;
      double actual = testObj.mannWhitneyUTest(x,y);
      Assert.assertEquals(expected, actual, 0.001);
  }
  
}