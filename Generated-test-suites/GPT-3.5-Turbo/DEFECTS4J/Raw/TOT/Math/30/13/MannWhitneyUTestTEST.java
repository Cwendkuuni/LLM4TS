import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    MannWhitneyUTest mannWhitneyUTest;

    @Before
    public void setUp() {
        mannWhitneyUTest = new MannWhitneyUTest();
    }

    @Test
    public void testMannWhitneyU_typicalCase() {
        // Typical case: two non-empty arrays
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};

        try {
            // Calculate the Mann-Whitney U statistic
            double U = mannWhitneyUTest.mannWhitneyU(x, y);

            // Expected U value
            double expectedU = 0;

            // Check if the calculated U value matches the expected U value
            assertEquals(expectedU, U, 0);
        } catch (Exception e) {
            fail("Exception: " + e.getMessage());
        }
    }

    @Test
    public void testMannWhitneyU_emptyArray() {
        // Edge case: Empty arrays
        double[] x = {};
        double[] y = {};

        try {
            // Calculate the Mann-Whitney U statistic
            double U = mannWhitneyUTest.mannWhitneyU(x, y);

            // Expected U value
            double expectedU = 0;

            // Check if the calculated U value matches the expected U value
            assertEquals(expectedU, U, 0);
        } catch (Exception e) {
            fail("Exception: " + e.getMessage());
        }
    }

    @Test
    public void testMannWhitneyU_nullArray() {
        // Error scenario: Null arrays
        double[] x = null;
        double[] y = null;

        try {
            // Calculate the Mann-Whitney U statistic
            double U = mannWhitneyUTest.mannWhitneyU(x, y);

            // Expected U value
            double expectedU = 0;

            // Check if the calculated U value matches the expected U value
            assertEquals(expectedU, U, 0);
        } catch (Exception e) {
            fail("Exception: " + e.getMessage());
        }
    }

    @Test
    public void testMannWhitneyUTest_typicalCase() {
        // Typical case: two non-empty arrays
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};

        try {
            // Calculate the p-value using Mann-Whitney U test
            double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);

            // Expected p-value
            double expectedPValue = 1.0; // (p-value cannot be calculated in this example)

            // Check if the calculated p-value matches the expected p-value
            assertEquals(expectedPValue, pValue, 0);
        } catch (Exception e) {
            fail("Exception: " + e.getMessage());
        }
    }

    @Test
    public void testMannWhitneyUTest_emptyArray() {
        // Edge case: Empty arrays
        double[] x = {};
        double[] y = {};

        try {
            // Calculate the p-value using Mann-Whitney U test
            double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);

            // Expected p-value
            double expectedPValue = 0;

            // Check if the calculated p-value matches the expected p-value
            assertEquals(expectedPValue, pValue, 0);
        } catch (Exception e) {
            fail("Exception: " + e.getMessage());
        }
    }

    @Test
    public void testMannWhitneyUTest_nullArray() {
        // Error scenario: Null arrays
        double[] x = null;
        double[] y = null;

        try {
            // Calculate the p-value using Mann-Whitney U test
            double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);

            // Expected p-value
            double expectedPValue = 0;

            // Check if the calculated p-value matches the expected p-value
            assertEquals(expectedPValue, pValue, 0);
        } catch (Exception e) {
            fail("Exception: " + e.getMessage());
        }
    }
}