import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        // Test with empty arrays
        try {
            mannWhitneyUTest.mannWhitneyUTest(new double[0], new double[0]);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        // Test with null arrays
        try {
            mannWhitneyUTest.mannWhitneyUTest(null, new double[0]);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        try {
            mannWhitneyUTest.mannWhitneyUTest(new double[0], null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        // Test with arrays of different sizes
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0, 7.0};

        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        double mannWhitneyUTestResult = mannWhitneyUTest.mannWhitneyUTest(x, y);

        assertEquals(mannWhitneyU, mannWhitneyUTestResult, 0.001);

        // Test with arrays of the same size and same elements
        double[] xSame = {1.0, 2.0, 3.0};
        double[] ySame = {1.0, 2.0, 3.0};

        mannWhitneyU = mannWhitneyUTest.mannWhitneyU(xSame, ySame);
        mannWhitneyUTestResult = mannWhitneyUTest.mannWhitneyUTest(xSame, ySame);

        assertEquals(mannWhitneyU, mannWhitneyUTestResult, 0.001);

        // Test with arrays of the same size and different elements
        double[] xDifferent = {1.0, 2.0, 3.0};
        double[] yDifferent = {4.0, 2.0, 3.0};

        mannWhitneyU = mannWhitneyUTest.mannWhitneyU(xDifferent, yDifferent);
        mannWhitneyUTestResult = mannWhitneyUTest.mannWhitneyUTest(xDifferent, yDifferent);

        assertEquals(mannWhitneyU, mannWhitneyUTestResult, 0.001);

        // Test with arrays of different sizes and same elements
        double[] xDifferentSizes = {1.0, 2.0, 3.0};
        double[] yDifferentSizes = {1.0, 2.0};

        mannWhitneyU = mannWhitneyUTest.mannWhitneyU(xDifferentSizes, yDifferentSizes);
        mannWhitneyUTestResult = mannWhitneyUTest.mannWhitneyUTest(xDifferentSizes, yDifferentSizes);

        assertEquals(mannWhitneyU, mannWhitneyUTestResult, 0.001);
    }
}