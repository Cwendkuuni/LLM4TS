import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest testInstance;

    @Before
    public void setUp() {
        testInstance = new MannWhitneyUTest();
    }

    @Test
    public void testEnsureDataConformance_NullArgumentException() {
        double[] x = null;
        double[] y = {1.0, 2.0, 3.0};

        try {
            testInstance.ensureDataConformance(x, y);
            fail("NullArgumentException was not thrown");
        } catch (NullArgumentException e) {
            // NullArgumentException was thrown as expected
        } catch (NoDataException e) {
            fail("Wrong exception thrown");
        }
    }

    @Test
    public void testEnsureDataConformance_NoDataException() {
        double[] x = {};
        double[] y = {1.0, 2.0, 3.0};

        try {
            testInstance.ensureDataConformance(x, y);
            fail("NoDataException was not thrown");
        } catch (NoDataException e) {
            // NoDataException was thrown as expected
        } catch (NullArgumentException e) {
            fail("Wrong exception thrown");
        }
    }

    @Test
    public void testConcatenateSamples() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double[] expected = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0};

        double[] result = testInstance.concatenateSamples(x, y);

        assertArrayEquals(expected, result, 0.0001);
    }

    // Add more test cases for other methods

}