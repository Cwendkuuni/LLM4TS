import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest test;

    @Before
    public void setup() {
        test = new MannWhitneyUTest();
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentException() {
        double[] x = null;
        double[] y = {1, 2, 3};
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentException2() {
        double[] x = {1, 2, 3};
        double[] y = null;
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testNoDataException() {
        double[] x = {};
        double[] y = {1, 2, 3};
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testNoDataException2() {
        double[] x = {1, 2, 3};
        double[] y = {};
        test.mannWhitneyU(x, y);
    }

    @Test
    public void testMannWhitneyU() {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        assertEquals(18.0, test.mannWhitneyU(x, y), 0.001);
    }

    @Test
    public void testMannWhitneyUTest() {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        assertEquals(0.057, test.mannWhitneyUTest(x, y), 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() throws ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        test.calculateAsymptoticPValue(17.0, x.length, y.length);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() throws ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        test.calculateAsymptoticPValue(17.0, x.length, y.length);
    }

}