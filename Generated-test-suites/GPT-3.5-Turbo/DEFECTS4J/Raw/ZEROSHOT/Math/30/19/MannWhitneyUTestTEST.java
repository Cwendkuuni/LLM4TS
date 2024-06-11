import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest testInstance;

    @Before
    public void setUp() {
        testInstance = new MannWhitneyUTest();
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullX() throws NullArgumentException, NoDataException {
        double[] x = null;
        double[] y = {1.0, 2.0, 3.0};
        testInstance.mannWhitneyU(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullY() throws NullArgumentException, NoDataException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = null;
        testInstance.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_EmptyX() throws NullArgumentException, NoDataException {
        double[] x = {};
        double[] y = {1.0, 2.0, 3.0};
        testInstance.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_EmptyY() throws NullArgumentException, NoDataException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {};
        testInstance.mannWhitneyU(x, y);
    }

    @Test
    public void testMannWhitneyU_ValidInput() throws NullArgumentException, NoDataException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double expectedU = 0.0;

        double actualU = testInstance.mannWhitneyU(x, y);

        assertEquals(expectedU, actualU, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = null;
        double[] y = {1.0, 2.0, 3.0};
        testInstance.mannWhitneyUTest(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = null;
        testInstance.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_EmptyX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {};
        double[] y = {1.0, 2.0, 3.0};
        testInstance.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_EmptyY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {};
        testInstance.mannWhitneyUTest(x, y);
    }

    @Test
    public void testMannWhitneyUTest_ValidInput() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double expectedPValue = 1.0;

        double actualPValue = testInstance.mannWhitneyUTest(x, y);

        assertEquals(expectedPValue, actualPValue, 0.0001);
    }

}