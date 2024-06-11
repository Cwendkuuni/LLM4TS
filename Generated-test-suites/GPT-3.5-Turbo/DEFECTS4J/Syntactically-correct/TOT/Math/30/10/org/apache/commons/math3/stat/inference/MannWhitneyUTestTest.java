import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU_SampleSizesEqual_Valid() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        double expectedU = 0;
        try {
            expectedU = test.mannWhitneyU(x, y);
        } catch (Exception e) {
            Assert.fail("Exception thrown for valid test case.");
        }
        Assert.assertEquals("Mann-Whitney U was not computed correctly for equal sample sizes.", 12.5, expectedU, 0.001);
    }

    @Test
    public void testMannWhitneyU_SampleSizesUnequal_Valid() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8};
        double expectedU = 0;
        try {
            expectedU = test.mannWhitneyU(x, y);
        } catch (Exception e) {
            Assert.fail("Exception thrown for valid test case.");
        }
        Assert.assertEquals("Mann-Whitney U was not computed correctly for unequal sample sizes.", 9.5, expectedU, 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullSampleX_ThrowsNullArgumentException() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = {1, 2, 3, 4, 5};
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullSampleY_ThrowsNullArgumentException() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = null;
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_EmptySampleX_ThrowsNoDataException() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {1, 2, 3, 4, 5};
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_EmptySampleY_ThrowsNoDataException() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {};
        test.mannWhitneyU(x, y);
    }

    @Test
    public void testMannWhitneyUTest_ValidSamples() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        double expectedPValue = 0;
        try {
            expectedPValue = test.mannWhitneyUTest(x, y);
        } catch (Exception e) {
            Assert.fail("Exception thrown for valid test case.");
        }
        Assert.assertEquals("Mann-Whitney U test p-value not computed correctly for valid samples.", 0.5, expectedPValue, 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullSampleX_ThrowsNullArgumentException() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = {1, 2, 3, 4, 5};
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullSampleY_ThrowsNullArgumentException() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = null;
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_EmptySampleX_ThrowsNoDataException() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {1, 2, 3, 4, 5};
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_EmptySampleY_ThrowsNoDataException() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {};
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = ConvergenceException.class)
    public void testMannWhitneyUTest_ConvergenceError_ThrowsConvergenceException() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.FIXED, TiesStrategy.AVERAGE);
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, Double.NaN};
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMannWhitneyUTest_MaxIterationsExceeded_ThrowsMaxCountExceededException() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.FIXED, TiesStrategy.AVERAGE);
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        test.mannWhitneyUTest(x, y);
    }

}