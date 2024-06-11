import org.junit.Test;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullX() throws NullArgumentException, NoDataException {
        double[] y = {1, 2, 3};
        mannWhitneyUTest.mannWhitneyU(null, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullY() throws NullArgumentException, NoDataException {
        double[] x = {1, 2, 3};
        mannWhitneyUTest.mannWhitneyU(x, null);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_ZeroLengthX() throws NullArgumentException, NoDataException {
        double[] x = {};
        double[] y = {1, 2, 3};
        mannWhitneyUTest.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_ZeroLengthY() throws NullArgumentException, NoDataException {
        double[] x = {1, 2, 3};
        double[] y = {};
        mannWhitneyUTest.mannWhitneyU(x, y);
    }

    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double result = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(6.0, result, 0);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] y = {1, 2, 3};
        mannWhitneyUTest.mannWhitneyUTest(null, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3};
        mannWhitneyUTest.mannWhitneyUTest(x, null);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_ZeroLengthX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {};
        double[] y = {1, 2, 3};
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_ZeroLengthY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3};
        double[] y = {};
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.0, result, 0);
    }
}