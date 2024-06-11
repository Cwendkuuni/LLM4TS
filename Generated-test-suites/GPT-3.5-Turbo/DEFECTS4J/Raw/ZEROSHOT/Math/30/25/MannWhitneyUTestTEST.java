import org.junit.Test;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest test = new MannWhitneyUTest();

    @Test
    public void testMannWhitneyU() {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};

        try {
            double result = test.mannWhitneyU(x, y);
            assertEquals(25.0, result, 0.001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testMannWhitneyUTest() {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};

        try {
            double result = test.mannWhitneyUTest(x, y);
            assertEquals(0.0287, result, 0.001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testMannWhitneyU_NullX() {
        double[] y = {1, 2, 3, 4, 5};

        try {
            test.mannWhitneyU(null, y);
            fail("No IllegalArgumentException thrown for null x");
        } catch (IllegalArgumentException e) {
            assertEquals("x cannot be null", e.getMessage());
        }
    }

    @Test
    public void testMannWhitneyU_NullY() {
        double[] x = {1, 2, 3, 4, 5};

        try {
            test.mannWhitneyU(x, null);
            fail("No IllegalArgumentException thrown for null y");
        } catch (IllegalArgumentException e) {
            assertEquals("y cannot be null", e.getMessage());
        }
    }

    @Test
    public void testMannWhitneyU_ZeroLengthX() {
        double[] x = {};
        double[] y = {1, 2, 3, 4, 5};

        try {
            test.mannWhitneyU(x, y);
            fail("No IllegalArgumentException thrown for zero-length x");
        } catch (IllegalArgumentException e) {
            assertEquals("x cannot be zero-length", e.getMessage());
        }
    }

    @Test
    public void testMannWhitneyU_ZeroLengthY() {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {};

        try {
            test.mannWhitneyU(x, y);
            fail("No IllegalArgumentException thrown for zero-length y");
        } catch (IllegalArgumentException e) {
            assertEquals("y cannot be zero-length", e.getMessage());
        }
    }

    @Test
    public void testMannWhitneyUTest_NullX() {
        double[] y = {1, 2, 3, 4, 5};

        try {
            test.mannWhitneyUTest(null, y);
            fail("No IllegalArgumentException thrown for null x");
        } catch (IllegalArgumentException e) {
            assertEquals("x cannot be null", e.getMessage());
        }
    }

    @Test
    public void testMannWhitneyUTest_NullY() {
        double[] x = {1, 2, 3, 4, 5};

        try {
            test.mannWhitneyUTest(x, null);
            fail("No IllegalArgumentException thrown for null y");
        } catch (IllegalArgumentException e) {
            assertEquals("y cannot be null", e.getMessage());
        }
    }

    @Test
    public void testMannWhitneyUTest_ZeroLengthX() {
        double[] x = {};
        double[] y = {1, 2, 3, 4, 5};

        try {
            test.mannWhitneyUTest(x, y);
            fail("No IllegalArgumentException thrown for zero-length x");
        } catch (IllegalArgumentException e) {
            assertEquals("x cannot be zero-length", e.getMessage());
        }
    }

    @Test
    public void testMannWhitneyUTest_ZeroLengthY() {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {};

        try {
            test.mannWhitneyUTest(x, y);
            fail("No IllegalArgumentException thrown for zero-length y");
        } catch (IllegalArgumentException e) {
            assertEquals("y cannot be zero-length", e.getMessage());
        }
    }
}