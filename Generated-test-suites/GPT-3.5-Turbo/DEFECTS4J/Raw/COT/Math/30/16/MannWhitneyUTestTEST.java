
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Assert;
import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 3, 5};
        double[] y = {2, 4, 6};
        MannWhitneyUTest test = new MannWhitneyUTest();
        double pValue = test.mannWhitneyUTest(x, y);
        Assert.assertEquals(0.5, pValue, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = null;
        double[] y = {2, 4, 6};
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 3, 5};
        double[] y = null;
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestEmptyX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {};
        double[] y = {2, 4, 6};
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestEmptyY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 3, 5};
        double[] y = {};
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(x, y);
    }

    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        double[] x = {1, 3, 5};
        double[] y = {2, 4, 6};
        MannWhitneyUTest test = new MannWhitneyUTest();
        double U = test.mannWhitneyU(x, y);
        Assert.assertEquals(3.0, U, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUNullX() throws NullArgumentException, NoDataException {
        double[] x = null;
        double[] y = {2, 4, 6};
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUNullY() throws NullArgumentException, NoDataException {
        double[] x = {1, 3, 5};
        double[] y = null;
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUEmptyX() throws NullArgumentException, NoDataException {
        double[] x = {};
        double[] y = {2, 4, 6};
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUEmptyY() throws NullArgumentException, NoDataException {
        double[] x = {1, 3, 5};
        double[] y = {};
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(x, y);
    }
}
