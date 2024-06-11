import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0};
        try {
            double result = test.mannWhitneyU(x, y);
            assertEquals(0.0, result, 0.0001); // First sample always has U1 = 0 in this case
        } catch (Exception e) {
            fail("Exception thrown during test");
        }
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0};
        try {
            double pValue = test.mannWhitneyUTest(x, y);
            assertEquals(1.0, pValue, 0.0001); // As samples are very different, p-value should be 1
        } catch (Exception e) {
            fail("Exception thrown during test");
        }
    }

    // Add more test cases for edge cases and error scenarios

}