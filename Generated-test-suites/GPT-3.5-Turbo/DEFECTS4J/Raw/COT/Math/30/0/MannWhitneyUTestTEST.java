
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest test;

    @Before
    public void setUp() {
        test = new MannWhitneyUTest();
    }

    @Test
    public void testMannWhitneyU() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double result = test.mannWhitneyU(x, y);
        Assert.assertEquals(9, result, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullX() {
        double[] x = null;
        double[] y = {4, 5, 6};
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullY() {
        double[] x = {1, 2, 3};
        double[] y = null;
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_EmptyX() {
        double[] x = {};
        double[] y = {4, 5, 6};
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_EmptyY() {
        double[] x = {1, 2, 3};
        double[] y = {};
        test.mannWhitneyU(x, y);
    }

    @Test
    public void testMannWhitneyUTest() {
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double result = test.mannWhitneyUTest(x, y);
        Assert.assertEquals(1, result, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullX() {
        double[] x = null;
        double[] y = {4, 5, 6};
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullY() {
        double[] x = {1, 2, 3};
        double[] y = null;
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_EmptyX() {
        double[] x = {};
        double[] y = {4, 5, 6};
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_EmptyY() {
        double[] x = {1, 2, 3};
        double[] y = {};
        test.mannWhitneyUTest(x, y);
    }

}
