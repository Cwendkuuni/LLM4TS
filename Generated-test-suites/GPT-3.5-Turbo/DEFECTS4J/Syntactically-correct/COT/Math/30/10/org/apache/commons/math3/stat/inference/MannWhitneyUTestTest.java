import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double result = test.mannWhitneyU(x, y);
        assertEquals(9.0, result, 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUNullX() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = {4, 5, 6};
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUZeroY() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {};
        test.mannWhitneyU(x, y);
    }

    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double result = test.mannWhitneyUTest(x, y);
        assertEquals(0.06666666666666667, result, 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = {4, 5, 6};
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestZeroY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {};
        test.mannWhitneyUTest(x, y);
    }

    @Test
    public void testMannWhitneyUTestOneSample() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {1, 1, 1};
        double result = test.mannWhitneyUTest(x, y);
        assertEquals(0.07871720116618043, result, 0.001);
    }

    @Test
    public void testMannWhitneyUTestSameSample() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {1, 2, 3};
        double result = test.mannWhitneyUTest(x, y);
        assertEquals(1.0, result, 0.001);
    }

    @Test
    public void testMannWhitneyUTestLargeSample() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[100000];
        double[] y = new double[100000];
        for (int i = 0; i < 100000; i++) {
            x[i] = i;
            y[i] = i;
        }
        double result = test.mannWhitneyUTest(x, y);
        assertEquals(1.0, result, 0.001);
    }
}

