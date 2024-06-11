import static org.junit.Assert.*;
import org.junit.Test;

public class MannWhitneyUTestTest {

    // Creating an instance of MannWhitneyUTest
    MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

    @Test
    public void testEnsureDataConformance_NullData_ExceptionThrown() {
        try {
            mannWhitneyUTest.ensureDataConformance(null, new double[] {1.0, 2.0});
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testEnsureDataConformance_EmptyData_ExceptionThrown() {
        try {
            mannWhitneyUTest.ensureDataConformance(new double[] {}, new double[] {1.0, 2.0});
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testConcatenateSamples() {
        double[] x = {1.0, 2.0};
        double[] y = {3.0, 4.0};
        double[] expected = {1.0, 2.0, 3.0, 4.0};
        double[] result = mannWhitneyUTest.concatenateSamples(x, y);
        assertArrayEquals(expected, result, 0.0);
    }

    // Add more test cases for other methods

}