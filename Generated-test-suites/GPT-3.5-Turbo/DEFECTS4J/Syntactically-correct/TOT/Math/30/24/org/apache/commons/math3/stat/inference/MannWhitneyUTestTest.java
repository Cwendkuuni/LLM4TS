
import org.junit.Test;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();

        // Typical input with sample x and y having the same length
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double expectedU = 0;
        try {
            expectedU = test.mannWhitneyU(x, y);
        } catch (ConvergenceException | MaxCountExceededException e) {
            fail(e.getMessage());
        }
        double tolerance = 1e-6;
        assertEquals(9.0, expectedU, tolerance);

        // Edge case with empty sample x and non-empty sample y
        double[] emptyX = {};
        double[] nonEmptyY = {4, 5, 6};
        try {
            expectedU = test.mannWhitneyU(emptyX, nonEmptyY);
            fail("Expected NoDataException to be thrown");
        } catch (NoDataException e) {
            // Pass
        }
    }

    @Test
    public void testCalculateAsymptoticPValue() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();

        // Typical input with Umin smaller than EU
        double Umin = 10;
        int n1 = 5;
        int n2 = 8;
        double expectedPValue = 0;
        try {
            expectedPValue = test.calculateAsymptoticPValue(Umin, n1, n2);
        } catch (ConvergenceException | MaxCountExceededException e) {
            fail(e.getMessage());
        }
        double tolerance = 1e-6;
        assertEquals(0.663531, expectedPValue, tolerance);

        // Edge case with Umin equal to EU
        Umin = 5;
        try {
            expectedPValue = test.calculateAsymptoticPValue(Umin, n1, n2);
        } catch (ConvergenceException | MaxCountExceededException e) {
            fail(e.getMessage());
        }
        assertEquals(1.0, expectedPValue, tolerance);
    }

    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();

        // Typical input with sample x and y having the same length
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double expectedPValue = 0;
        try {
            expectedPValue = test.mannWhitneyUTest(x, y);
        } catch (ConvergenceException | MaxCountExceededException e) {
            fail(e.getMessage());
        }
        double tolerance = 1e-6;
        assertEquals(0.171614, expectedPValue, tolerance);

        // Error scenario with empty samples x and y
        double[] emptyX = {};
        double[] emptyY = {};
        try {
            expectedPValue = test.mannWhitneyUTest(emptyX, emptyY);
            fail("Expected NoDataException to be thrown");
        } catch (NoDataException e) {
            // Pass
        }

        // Sample x is longer than sample y
        double[] longX = {1, 2, 3, 4, 5};
        double[] shortY = {6, 7, 8};
        try {
            expectedPValue = test.mannWhitneyUTest(longX, shortY);
        } catch (ConvergenceException | MaxCountExceededException e) {
            fail(e.getMessage());
        }
        assertEquals(0.089084, expectedPValue, tolerance);
    }

    @Test
    public void testEnsureDataConformance() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        // Non-empty valid input
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        try {
            test.ensureDataConformance(x, y);
        } catch (NoDataException | NullArgumentException e) {
            fail(e.getMessage());
        }

        // Null input
        double[] nullX = null;
        double[] nullY = null;
        try {
            test.ensureDataConformance(nullX, nullY);
            fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            // Pass
        }

        // Empty input
        double[] emptyX = {};
        double[] emptyY = {};
        try {
            test.ensureDataConformance(emptyX, emptyY);
            fail("Expected NoDataException to be thrown");
        } catch (NoDataException e) {
            // Pass
        }
    }

    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        // Non-empty input
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double[] expected = {1, 2, 3, 4, 5, 6};
        double[] actual = test.concatenateSamples(x, y);
        assertArrayEquals(expected, actual, 0);

        // Empty input
        double[] emptyX = {};
        double[] emptyY = {};
        expected = new double[0];
        actual = test.concatenateSamples(emptyX, emptyY);
        assertArrayEquals(expected, actual, 0);
    }

}
