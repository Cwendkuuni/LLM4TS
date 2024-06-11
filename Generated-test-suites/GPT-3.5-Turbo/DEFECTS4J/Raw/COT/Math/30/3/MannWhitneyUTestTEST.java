import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullX() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(null, new double[]{1.0, 2.0, 3.0});
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullY() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(new double[]{1.0, 2.0, 3.0}, null);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_EmptyX() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(new double[]{}, new double[]{1.0, 2.0, 3.0});
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_EmptyY() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(new double[]{1.0, 2.0, 3.0}, new double[]{});
    }

    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {4.0, 5.0, 6.0};
        double[] y = {1.0, 2.0, 3.0};
        double result = test.mannWhitneyU(x, y);
        assertEquals(9.0, result, 0.0);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(null, new double[]{1.0, 2.0, 3.0});
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(new double[]{1.0, 2.0, 3.0}, null);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_EmptyX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(new double[]{}, new double[]{1.0, 2.0, 3.0});
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_EmptyY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(new double[]{1.0, 2.0, 3.0}, new double[]{});
    }

    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {4.0, 5.0, 6.0};
        double[] y = {1.0, 2.0, 3.0};
        double result = test.mannWhitneyUTest(x, y);
        assertEquals(0.0667, result, 0.0001);
    }
}