import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU_TypicalCase() {
        MannWhitneyUTest testInstance = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {6.0, 7.0, 8.0, 9.0, 10.0};
        double expectedU = 0.0; // U-statistic depends on the ranks assigned, which may vary
        try {
            double actualU = testInstance.mannWhitneyU(x, y);
            assertEquals(expectedU, actualU, 0.001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testMannWhitneyU_EmptySample() {
        MannWhitneyUTest testInstance = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {1.0, 2.0, 3.0};
        try {
            testInstance.mannWhitneyU(x, y);
            fail("Exception not thrown");
        } catch (NoDataException e) {
            // expected
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }

    @Test
    public void testMannWhitneyU_NullSample() {
        MannWhitneyUTest testInstance = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = null;
        try {
            testInstance.mannWhitneyU(x, y);
            fail("Exception not thrown");
        } catch (NullArgumentException e) {
            // expected
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }

    @Test
    public void testMannWhitneyUTest_TypicalCase() {
        MannWhitneyUTest testInstance = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {6.0, 7.0, 8.0, 9.0, 10.0};
        double expectedP = 1.0; // p-value depends on the U-statistic, which depends on the ranks assigned
        try {
            double actualP = testInstance.mannWhitneyUTest(x, y);
            assertEquals(expectedP, actualP, 0.001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testMannWhitneyUTest_EmptySample() {
        MannWhitneyUTest testInstance = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {1.0, 2.0, 3.0};
        try {
            testInstance.mannWhitneyUTest(x, y);
            fail("Exception not thrown");
        } catch (NoDataException e) {
            // expected
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }

    @Test
    public void testMannWhitneyUTest_NullSample() {
        MannWhitneyUTest testInstance = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = null;
        try {
            testInstance.mannWhitneyUTest(x, y);
            fail("Exception not thrown");
        } catch (NullArgumentException e) {
            // expected
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }

}