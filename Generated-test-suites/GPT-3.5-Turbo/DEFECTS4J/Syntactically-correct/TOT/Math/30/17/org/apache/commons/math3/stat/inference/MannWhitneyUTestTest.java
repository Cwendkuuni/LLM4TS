import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        // Typical use cases
        double[] x1 = {1, 2, 3, 4, 5};
        double[] y1 = {6, 7, 8, 9, 10};
        double actual1 = test.mannWhitneyU(x1, y1);
        double expected1 = 0;
        Assert.assertEquals(expected1, actual1, 0);

        double[] x2 = {1, 3, 5, 7, 9};
        double[] y2 = {2, 4, 6, 8, 10};
        double actual2 = test.mannWhitneyU(x2, y2);
        double expected2 = 25;
        Assert.assertEquals(expected2, actual2, 0);

        // Edge cases
        double[] x3 = {1, 2, 3};
        double[] y3 = {4, 5, 6, 7, 8};
        double actual3 = test.mannWhitneyU(x3, y3);
        double expected3 = 6;
        Assert.assertEquals(expected3, actual3, 0);

        double[] x4 = {};
        double[] y4 = {1, 2, 3};
        try {
            double actual4 = test.mannWhitneyU(x4, y4);
            Assert.fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Test passed
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Error scenarios
        double[] x5 = null;
        double[] y5 = {1, 2, 3};
        try {
            double actual5 = test.mannWhitneyU(x5, y5);
            Assert.fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Test passed
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        // Typical use cases
        double[] x1 = {1, 2, 3, 4, 5};
        double[] y1 = {6, 7, 8, 9, 10};
        try {
            double actual1 = test.mannWhitneyUTest(x1, y1);
            double expected1 = 0;
            Assert.assertEquals(expected1, actual1, 0);
        } catch (NullArgumentException | NoDataException | ConvergenceException | MaxCountExceededException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        double[] x2 = {1, 3, 5, 7, 9};
        double[] y2 = {2, 4, 6, 8, 10};
        try {
            double actual2 = test.mannWhitneyUTest(x2, y2);
            double expected2 = 25;
            Assert.assertEquals(expected2, actual2, 0);
        } catch (NullArgumentException | NoDataException | ConvergenceException | MaxCountExceededException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Edge cases
        double[] x3 = {1, 2, 3};
        double[] y3 = {4, 5, 6, 7, 8};
        try {
            double actual3 = test.mannWhitneyUTest(x3, y3);
            double expected3 = 6;
            Assert.assertEquals(expected3, actual3, 0);
        } catch (NullArgumentException | NoDataException | ConvergenceException | MaxCountExceededException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        double[] x4 = {};
        double[] y4 = {1, 2, 3};
        try {
            double actual4 = test.mannWhitneyUTest(x4, y4);
            Assert.fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Test passed
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Error scenarios
        double[] x5 = null;
        double[] y5 = {1, 2, 3};
        try {
            double actual5 = test.mannWhitneyUTest(x5, y5);
            Assert.fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Test passed
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

}