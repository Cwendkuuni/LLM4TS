import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest mannWhitneyUTest;

    @Before
    public void setUp() {
        mannWhitneyUTest = new MannWhitneyUTest();
    }

    @Test
    public void mannWhitneyU_shouldReturnCorrectValue_whenValidInput() throws NullArgumentException, NoDataException {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        double expected = 5; // U1 or U2 will be the maximum of Umax and Umin

        double actual = mannWhitneyUTest.mannWhitneyU(x, y);

        assertEquals(expected, actual, 0);
    }

    @Test(expected = NullArgumentException.class)
    public void mannWhitneyU_shouldThrowNullArgumentException_whenNullInput() throws NullArgumentException, NoDataException {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = null;

        mannWhitneyUTest.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void mannWhitneyU_shouldThrowNoDataException_whenEmptyInput() throws NullArgumentException, NoDataException {
        double[] x = {};
        double[] y = {6, 7, 8, 9, 10};

        mannWhitneyUTest.mannWhitneyU(x, y);
    }

    @Test
    public void mannWhitneyUTest_shouldReturnCorrectPValue_whenValidInput() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        double expected = 0.02118644067796616;

        double actual = mannWhitneyUTest.mannWhitneyUTest(x, y);

        assertEquals(expected, actual, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void mannWhitneyUTest_shouldThrowNullArgumentException_whenNullInput() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = null;

        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void mannWhitneyUTest_shouldThrowNoDataException_whenEmptyInput() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {};
        double[] y = {6, 7, 8, 9, 10};

        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = ConvergenceException.class)
    public void mannWhitneyUTest_shouldThrowConvergenceException_whenConvergenceError() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {1, 2, 3, 4, 5};

        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = MaxCountExceededException.class)
    public void mannWhitneyUTest_shouldThrowMaxCountExceededException_whenMaxIterationsExceeded() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};

        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

}