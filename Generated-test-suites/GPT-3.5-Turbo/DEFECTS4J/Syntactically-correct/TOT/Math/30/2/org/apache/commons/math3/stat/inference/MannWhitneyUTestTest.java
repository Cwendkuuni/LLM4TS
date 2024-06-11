import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

public class MannWhitneyUTestTest {

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = null;
        double[] y = {1, 2, 3};
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        uTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3};
        double[] y = null;
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        uTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_EmptyX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {};
        double[] y = {1, 2, 3};
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        uTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_EmptyY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3};
        double[] y = {};
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        uTest.mannWhitneyUTest(x, y);
    }

    @Test
    public void testMannWhitneyUTest_EqualSamples() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3};
        double[] y = {1, 2, 3};
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double pValue = uTest.mannWhitneyUTest(x, y);
        Assert.assertEquals(0.5, pValue, 0.0001);
    }

    @Test
    public void testMannWhitneyUTest_DifferentSamples() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double pValue = uTest.mannWhitneyUTest(x, y);
        Assert.assertEquals(0.0348264, pValue, 0.0001);
    }

    @Test
    public void testMannWhitneyUTest_OneSampleEmpty() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {};
        double[] y = {1, 2, 3};
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double pValue = uTest.mannWhitneyUTest(x, y);
        Assert.assertEquals(0.5, pValue, 0.0001);
    }

    @Test
    public void testMannWhitneyUTest_MaxCountExceeded() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2};
        double[] y = {3, 4};
        MannWhitneyUTest uTest = new MannWhitneyUTest();
        double pValue = uTest.mannWhitneyUTest(x, y);
        Assert.assertEquals(1.0, pValue, 0.0001);
    }
}