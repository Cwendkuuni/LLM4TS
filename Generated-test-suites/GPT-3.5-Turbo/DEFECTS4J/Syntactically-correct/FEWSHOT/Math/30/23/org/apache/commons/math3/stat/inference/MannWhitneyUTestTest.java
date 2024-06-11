import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        double result = test.mannWhitneyU(x, y);
        assertEquals(0.0, result, 0.0001);

        double[] x2 = {1, 2, 3, 4, 5};
        double[] y2 = {2, 4, 6, 8, 10};
        double result2 = test.mannWhitneyU(x2, y2);
        assertEquals(0.0, result2, 0.0001);

        double[] x3 = {2, 4, 6, 8, 10};
        double[] y3 = {1, 2, 3, 4, 5};
        double result3 = test.mannWhitneyU(x3, y3);
        assertEquals(25.0, result3, 0.0001);
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        double result = test.mannWhitneyUTest(x, y);
        assertNotNull(result);

        double[] x2 = {1, 2, 3, 4, 5};
        double[] y2 = {2, 4, 6, 8, 10};
        double result2 = test.mannWhitneyUTest(x2, y2);
        assertNotNull(result2);

        double[] x3 = {2, 4, 6, 8, 10};
        double[] y3 = {1, 2, 3, 4, 5};
        double result3 = test.mannWhitneyUTest(x3, y3);
        assertNotNull(result3);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentExceptionMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        double[] x = null;
        double[] y = {1, 2, 3, 4, 5};
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testNoDataExceptionMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        double[] x = {};
        double[] y = {1, 2, 3, 4, 5};
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentExceptionMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        double[] x = null;
        double[] y = {1, 2, 3, 4, 5};
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testNoDataExceptionMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        double[] x = {};
        double[] y = {1, 2, 3, 4, 5};
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceExceptionMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededExceptionMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        double[] x = {1, 2, 3, 4, 5};
        double[] y = {2, 4, 6, 8, 10};
        test.mannWhitneyUTest(x, y);
    }

}