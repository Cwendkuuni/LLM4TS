import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;

public class MannWhitneyUTestTest {

   @Test(expected=NullArgumentException.class)
   public void testMannWhitneyU_NullX() throws NullArgumentException, NoDataException {
      MannWhitneyUTest test = new MannWhitneyUTest();
      double[] y = {1, 2, 3};
      test.mannWhitneyU(null, y);
   }
   
   @Test(expected=NullArgumentException.class)
   public void testMannWhitneyU_NullY() throws NullArgumentException, NoDataException {
      MannWhitneyUTest test = new MannWhitneyUTest();
      double[] x = {1, 2, 3};
      test.mannWhitneyU(x, null);
   }
   
   @Test(expected=NoDataException.class)
   public void testMannWhitneyU_EmptyX() throws NullArgumentException, NoDataException {
      MannWhitneyUTest test = new MannWhitneyUTest();
      double[] x = {};
      double[] y = {1, 2, 3};
      test.mannWhitneyU(x, y);
   }
   
   @Test(expected=NoDataException.class)
   public void testMannWhitneyU_EmptyY() throws NullArgumentException, NoDataException {
      MannWhitneyUTest test = new MannWhitneyUTest();
      double[] x = {1, 2, 3};
      double[] y = {};
      test.mannWhitneyU(x, y);
   }
   
   @Test
   public void testMannWhitneyU_SameLength() throws NullArgumentException, NoDataException {
      MannWhitneyUTest test = new MannWhitneyUTest();
      double[] x = {1, 2, 3};
      double[] y = {4, 5, 6};
      double result = test.mannWhitneyU(x, y);
      Assert.assertEquals(0, result, 0.0001);
   }
   
   @Test
   public void testMannWhitneyU_DifferentLength() throws NullArgumentException, NoDataException {
      MannWhitneyUTest test = new MannWhitneyUTest();
      double[] x = {1, 2, 3};
      double[] y = {4, 5};
      double result = test.mannWhitneyU(x, y);
      Assert.assertEquals(3, result, 0.0001);
   }
   
   @Test(expected=NullArgumentException.class)
   public void testMannWhitneyUTest_NullX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
      MannWhitneyUTest test = new MannWhitneyUTest();
      double[] y = {1, 2, 3};
      test.mannWhitneyUTest(null, y);
   }
   
   @Test(expected=NullArgumentException.class)
   public void testMannWhitneyUTest_NullY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
      MannWhitneyUTest test = new MannWhitneyUTest();
      double[] x = {1, 2, 3};
      test.mannWhitneyUTest(x, null);
   }
   
   @Test(expected=NoDataException.class)
   public void testMannWhitneyUTest_EmptyX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
      MannWhitneyUTest test = new MannWhitneyUTest();
      double[] x = {};
      double[] y = {1, 2, 3};
      test.mannWhitneyUTest(x, y);
   }
   
   @Test(expected=NoDataException.class)
   public void testMannWhitneyUTest_EmptyY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
      MannWhitneyUTest test = new MannWhitneyUTest();
      double[] x = {1, 2, 3};
      double[] y = {};
      test.mannWhitneyUTest(x, y);
   }
   
   @Test
   public void testMannWhitneyUTest_SameLength() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
      MannWhitneyUTest test = new MannWhitneyUTest();
      double[] x = {1, 2, 3};
      double[] y = {4, 5, 6};
      double result = test.mannWhitneyUTest(x, y);
      Assert.assertEquals(1, result, 0.0001);
   }
   
   @Test
   public void testMannWhitneyUTest_DifferentLength() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
      MannWhitneyUTest test = new MannWhitneyUTest();
      double[] x = {1, 2, 3};
      double[] y = {4, 5};
      double result = test.mannWhitneyUTest(x, y);
      Assert.assertEquals(0.4286, result, 0.0001);
   }
}