import org.junit.Assert;
import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        // Test case 1
        double[] x1 = {1.0, 2.0, 3.0};
        double[] y1 = {4.0, 5.0, 6.0};
        double expected1 = 9.0;
        double actual1 = test.mannWhitneyU(x1, y1);
        Assert.assertEquals(expected1, actual1, 0.0001);

        // Test case 2
        double[] x2 = {1.0, 2.0, 3.0};
        double[] y2 = {3.0, 2.0, 1.0};
        double expected2 = 9.0;
        double actual2 = test.mannWhitneyU(x2, y2);
        Assert.assertEquals(expected2, actual2, 0.0001);

        // Test case 3
        double[] x3 = {};
        double[] y3 = {};
        try {
            double actual3 = test.mannWhitneyU(x3, y3);
            Assert.fail("NoDataException was not thrown");
        } catch (NoDataException e) {
            // expected
        }

        // Test case 4
        double[] x4 = {1.0, 2.0, 3.0};
        double[] y4 = null;
        try {
            double actual4 = test.mannWhitneyU(x4, y4);
            Assert.fail("NullArgumentException was not thrown");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        // Test case 1
        double[] x1 = {1.0, 2.0, 3.0};
        double[] y1 = {4.0, 5.0, 6.0};
        double expected1 = 0.16666666666666666;
        double actual1 = test.mannWhitneyUTest(x1, y1);
        Assert.assertEquals(expected1, actual1, 0.0001);

        // Test case 2
        double[] x2 = {1.0, 2.0, 3.0};
        double[] y2 = {3.0, 2.0, 1.0};
        double expected2 = 0.16666666666666666;
        double actual2 = test.mannWhitneyUTest(x2, y2);
        Assert.assertEquals(expected2, actual2, 0.0001);

        // Test case 3
        double[] x3 = {};
        double[] y3 = {};
        try {
            double actual3 = test.mannWhitneyUTest(x3, y3);
            Assert.fail("NoDataException was not thrown");
        } catch (NoDataException e) {
            // expected
        }

        // Test case 4
        double[] x4 = {1.0, 2.0, 3.0};
        double[] y4 = null;
        try {
            double actual4 = test.mannWhitneyUTest(x4, y4);
            Assert.fail("NullArgumentException was not thrown");
        } catch (NullArgumentException e) {
            // expected
        }
    }

}