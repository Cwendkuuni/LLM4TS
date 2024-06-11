import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double expected = 9;
        double result = test.mannWhitneyU(x, y);
        assertEquals(expected, result, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUWithNullX() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = {4, 5, 6};
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUWithNullY() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = null;
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUWithEmptyX() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {4, 5, 6};
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUWithEmptyY() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {};
        test.mannWhitneyU(x, y);
    }

    @Test
    public void testCalculateAsymptoticPValue() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double Umin = 9;
        int n1 = 3;
        int n2 = 3;
        double expected = 0.1539;
        double result = test.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(expected, result, 0.0001);
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double expected = 0.1539;
        double result = test.mannWhitneyUTest(x, y);
        assertEquals(expected, result, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestWithNullX() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = {4, 5, 6};
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestWithNullY() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = null;
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestWithEmptyX() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {4, 5, 6};
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestWithEmptyY() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {};
        test.mannWhitneyUTest(x, y);
    }
}