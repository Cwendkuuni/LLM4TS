import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullArgumentException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = {1.0, 2.0, 3.0};
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullArgumentException2() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = null;
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_NoDataException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {1.0, 2.0, 3.0};
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_NoDataException2() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {};
        test.mannWhitneyUTest(x, y);
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double result = test.mannWhitneyUTest(x, y);
        assertEquals(0.0, result, 0.00001);
    }

    @Test
    public void testMannWhitneyUTest_2() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {3.0, 4.0, 5.0};
        double result = test.mannWhitneyUTest(x, y);
        assertEquals(0.42857, result, 0.00001);
    }

    @Test
    public void testMannWhitneyUTest_3() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {6.0, 7.0, 8.0};
        double result = test.mannWhitneyUTest(x, y);
        assertEquals(0.0, result, 0.00001);
    }

}