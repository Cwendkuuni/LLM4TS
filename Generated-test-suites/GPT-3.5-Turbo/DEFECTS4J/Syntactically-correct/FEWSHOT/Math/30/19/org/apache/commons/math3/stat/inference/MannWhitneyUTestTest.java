import org.junit.Test;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import static org.junit.Assert.assertEquals;

public class MannWhitneyUTestTest {
    
    @Test(expected=NullArgumentException.class)
    public void testMannWhitneyU_NullX() throws NullArgumentException, NoDataException {
        double[] y = {1.0, 2.0, 3.0};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyU(null, y);
    }

    @Test(expected=NullArgumentException.class)
    public void testMannWhitneyU_NullY() throws NullArgumentException, NoDataException {
        double[] x = {1.0, 2.0, 3.0};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyU(x, null);
    }

    @Test(expected=NoDataException.class)
    public void testMannWhitneyU_EmptyX() throws NullArgumentException, NoDataException {
        double[] x = {};
        double[] y = {1.0, 2.0, 3.0};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyU(x, y);
    }

    @Test(expected=NoDataException.class)
    public void testMannWhitneyU_EmptyY() throws NullArgumentException, NoDataException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyU(x, y);
    }

    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double result = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(9.0, result, 0.0001);
    }

    @Test(expected=NullArgumentException.class)
    public void testMannWhitneyUTest_NullX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] y = {1.0, 2.0, 3.0};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(null, y);
    }

    @Test(expected=NullArgumentException.class)
    public void testMannWhitneyUTest_NullY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 2.0, 3.0};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(x, null);
    }

    @Test(expected=NoDataException.class)
    public void testMannWhitneyUTest_EmptyX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {};
        double[] y = {1.0, 2.0, 3.0};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test(expected=NoDataException.class)
    public void testMannWhitneyUTest_EmptyY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.2113248654051871, result, 0.0001);
    }
    
}