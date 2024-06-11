import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MannWhitneyUTestTest {
    
    private MannWhitneyUTest test;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        test = new MannWhitneyUTest();
    }

    // Test for method mannWhitneyU(double[], double[])
    @Test
    public void testMannWhitneyU_Sample1Empty_ThrowsNoDataException() {
      expectedException.expect(NoDataException.class);
      test.mannWhitneyU(new double[]{}, new double[]{1, 2, 3});
    }

    // Test for method mannWhitneyU(double[], double[])
    @Test
    public void testMannWhitneyU_Sample2Empty_ThrowsNoDataException() {
      expectedException.expect(NoDataException.class);
      test.mannWhitneyU(new double[]{1, 2, 3}, new double[]{});
    }

    // Test for method mannWhitneyU(double[], double[])
    @Test
    public void testMannWhitneyU_Sample1Null_ThrowsNullArgumentException() {
      expectedException.expect(NullArgumentException.class);
      test.mannWhitneyU(null, new double[]{1, 2, 3});
    }

    // Test for method mannWhitneyU(double[], double[])
    @Test
    public void testMannWhitneyU_Sample2Null_ThrowsNullArgumentException() {
      expectedException.expect(NullArgumentException.class);
      test.mannWhitneyU(new double[]{1, 2, 3}, null);
    }

    // Test for method mannWhitneyU(double[], double[])
    @Test
    public void testMannWhitneyU_Sample1Length1_Sample2Length1_ShouldReturn0() throws NullArgumentException, NoDataException {
      double[] sample1 = new double[]{1};
      double[] sample2 = new double[]{2};
      double result = test.mannWhitneyU(sample1, sample2);
      assertEquals(0, result, 0);
    }

    // Test for method mannWhitneyU(double[], double[])
    @Test
    public void testMannWhitneyU_Sample1Length3_Sample2Length3_ShouldReturn3() throws NullArgumentException, NoDataException {
      double[] sample1 = new double[]{1, 2, 3};
      double[] sample2 = new double[]{4, 5, 6};
      double result = test.mannWhitneyU(sample1, sample2);
      assertEquals(3, result, 0);
    }

    // Test for method mannWhitneyU(double[], double[])
    @Test
    public void testMannWhitneyU_Sample1Length1_Sample2Length1_CheckSymmetry() throws NullArgumentException, NoDataException {
      double[] sample1 = new double[]{2};
      double[] sample2 = new double[]{1};
      double result1 = test.mannWhitneyU(sample1, sample2);
      double result2 = test.mannWhitneyU(sample2, sample1);
      assertEquals(result1, result2, 0);
    }

    // Test for method mannWhitneyU(double[], double[])
    @Test
    public void testMannWhitneyU_Sample1Length1_Sample2Length1_CheckUminAndUmax() throws NullArgumentException, NoDataException {
      double[] sample1 = new double[]{1};
      double[] sample2 = new double[]{2};
      double result = test.mannWhitneyU(sample1, sample2);
      assertTrue(result >= 0);
      assertTrue(result <= sample1.length * sample2.length);
    }

    // Test for method mannWhitneyUTest(double[], double[])
    @Test
    public void testMannWhitneyUTest_Sample1Empty_ThrowsNoDataException() throws ConvergenceException, NullArgumentException, NoDataException, MaxCountExceededException {
      expectedException.expect(NoDataException.class);
      test.mannWhitneyUTest(new double[]{}, new double[]{1, 2, 3});
    }

    // Test for method mannWhitneyUTest(double[], double[])
    @Test
    public void testMannWhitneyUTest_Sample2Empty_ThrowsNoDataException() throws ConvergenceException, NullArgumentException, NoDataException, MaxCountExceededException {
      expectedException.expect(NoDataException.class);
      test.mannWhitneyUTest(new double[]{1, 2, 3}, new double[]{});
    }

    // Test for method mannWhitneyUTest(double[], double[])
    @Test
    public void testMannWhitneyUTest_Sample1Null_ThrowsNullArgumentException() throws ConvergenceException, NullArgumentException, NoDataException, MaxCountExceededException {
      expectedException.expect(NullArgumentException.class);
      test.mannWhitneyUTest(null, new double[]{1, 2, 3});
    }

    // Test for method mannWhitneyUTest(double[], double[])
    @Test
    public void testMannWhitneyUTest_Sample2Null_ThrowsNullArgumentException() throws ConvergenceException, NullArgumentException, NoDataException, MaxCountExceededException {
      expectedException.expect(NullArgumentException.class);
      test.mannWhitneyUTest(new double[]{1, 2, 3}, null);
    }

    // Test for method mannWhitneyUTest(double[], double[])
    @Test
    public void testMannWhitneyUTest_Sample1Length1_Sample2Length1_CheckSymmetry() throws ConvergenceException, NullArgumentException, NoDataException, MaxCountExceededException {
      double[] sample1 = new double[]{2};
      double[] sample2 = new double[]{1};
      double result1 = test.mannWhitneyUTest(sample1, sample2);
      double result2 = test.mannWhitneyUTest(sample2, sample1);
      assertEquals(result1, result2, 0);
    }

    // Test for method mannWhitneyUTest(double[], double[])
    @Test
    public void testMannWhitneyUTest_Sample1Length1_Sample2Length1_CheckPValueRange() throws ConvergenceException, NullArgumentException, NoDataException, MaxCountExceededException {
      double[] sample1 = new double[]{1};
      double[] sample2 = new double[]{2};
      double result = test.mannWhitneyUTest(sample1, sample2);
      assertTrue(result >= 0);
      assertTrue(result <= 1);
    }
}