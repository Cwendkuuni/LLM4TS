import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest mannWhitneyUTest;

    @Before
    public void setUp() {
        mannWhitneyUTest = new MannWhitneyUTest();
    }

    @After
    public void tearDown() {
        mannWhitneyUTest = null;
    }

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
    public void testMannWhitneyU_EmptyX() throws NullArgumentException, NoDataException {
        double[] x = {};
        double[] y = {1, 2, 3};
        mannWhitneyUTest.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_EmptyY() throws NullArgumentException, NoDataException {
        double[] x = {1, 2, 3};
        double[] y = {};
        mannWhitneyUTest.mannWhitneyU(x, y);
    }

    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double expectedU = 0.0;
        double actualU = mannWhitneyUTest.mannWhitneyU(x, y);
        Assert.assertEquals(expectedU, actualU, 0.0001);
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
    public void testMannWhitneyUTest_EmptyX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {};
        double[] y = {1, 2, 3};
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_EmptyY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3};
        double[] y = {};
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double expectedPValue = 0.0875;
        double actualPValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        Assert.assertEquals(expectedPValue, actualPValue, 0.0001);
    }

    @Test
    public void testMannWhitneyUTest_PValue() throws ConvergenceException, MaxCountExceededException {
        double Umin = 6.0;
        int n1 = 3;
        int n2 = 3;
        double expectedPValue = 0.4458;
        double actualPValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        Assert.assertEquals(expectedPValue, actualPValue, 0.0001);
    }
}