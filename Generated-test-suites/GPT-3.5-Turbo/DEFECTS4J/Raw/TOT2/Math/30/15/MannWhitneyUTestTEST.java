import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest mannWhitneyUTest;

    @Before
    public void setUp() {
        mannWhitneyUTest = new MannWhitneyUTest();
    }

    @After
    public void tearDown() {
        mannWhitneyUTest = null;
    }

    @Test
    public void testEnsureDataConformance_NullArgumentException() {
        try {
            mannWhitneyUTest.ensureDataConformance(null, new double[]{1.0, 2.0});
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        try {
            mannWhitneyUTest.ensureDataConformance(new double[]{1.0, 2.0}, null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEnsureDataConformance_NoDataException() {
        try {
            mannWhitneyUTest.ensureDataConformance(new double[]{}, new double[]{1.0, 2.0});
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        try {
            mannWhitneyUTest.ensureDataConformance(new double[]{1.0, 2.0}, new double[]{});
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }
    }

    @Test
    public void testConcatenateSamples() {
        double[] x = {1.0, 2.0};
        double[] y = {3.0, 4.0};
        double[] expected = {1.0, 2.0, 3.0, 4.0};
        assertArrayEquals(expected, mannWhitneyUTest.concatenateSamples(x, y), 0.0001);
    }

    @Test
    public void testMannWhitneyU() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double expectedU = 4.0;
        assertEquals(expectedU, mannWhitneyUTest.mannWhitneyU(x, y), 0.0001);
    }

    @Test
    public void testCalculateAsymptoticPValue() {
        double Umin = 3.0;
        int n1 = 3;
        int n2 = 3;
        double expectedPValue = 0.1336;
        assertEquals(expectedPValue, mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2), 0.0001);
    }

    @Test
    public void testMannWhitneyUTest() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double expectedPValue = 0.1336;
        assertEquals(expectedPValue, mannWhitneyUTest.mannWhitneyUTest(x, y), 0.0001);
    }

}