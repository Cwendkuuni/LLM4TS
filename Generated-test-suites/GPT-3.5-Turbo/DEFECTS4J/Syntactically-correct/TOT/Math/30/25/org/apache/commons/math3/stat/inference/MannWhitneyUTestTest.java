import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest mannWhitneyUTest;
    private double[] x = {1, 2, 3, 4};
    private double[] y = {5, 6, 7};

    @Before
    public void setUp() {
        mannWhitneyUTest = new MannWhitneyUTest();
    }

    @Test
    public void testMannWhitneyU_ValidInput() throws NullArgumentException, NoDataException {
        double U = mannWhitneyUTest.mannWhitneyU(x, y);
        // Test that U value is calculated correctly
        assertEquals(6, U, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullX() throws NullArgumentException, NoDataException {
        mannWhitneyUTest.mannWhitneyU(null, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullY() throws NullArgumentException, NoDataException {
        mannWhitneyUTest.mannWhitneyU(x, null);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_EmptyX() throws NullArgumentException, NoDataException {
        double[] emptyX = new double[0];
        mannWhitneyUTest.mannWhitneyU(emptyX, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_EmptyY() throws NullArgumentException, NoDataException {
        double[] emptyY = new double[0];
        mannWhitneyUTest.mannWhitneyU(x, emptyY);
    }

    @Test
    public void testMannWhitneyUTest_ValidInput() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        // Test that p-value is calculated correctly
        assertEquals(0.3333, pValue, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        mannWhitneyUTest.mannWhitneyUTest(null, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        mannWhitneyUTest.mannWhitneyUTest(x, null);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_EmptyX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] emptyX = new double[0];
        mannWhitneyUTest.mannWhitneyUTest(emptyX, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_EmptyY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] emptyY = new double[0];
        mannWhitneyUTest.mannWhitneyUTest(x, emptyY);
    }

    @Test
    public void testMannWhitneyUTest_ConvergenceError() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 1, 1, 1};
        double[] y = {1, 1, 1, 1};
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        // Test that p-value is NaN when convergence error occurs
        assertTrue(Double.isNaN(pValue));
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMannWhitneyUTest_MaxIterationsExceeded() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2};
        double[] y = {2, 3};
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

}