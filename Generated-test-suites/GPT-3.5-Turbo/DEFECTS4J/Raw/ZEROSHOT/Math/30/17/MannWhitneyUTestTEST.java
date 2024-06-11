import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();
        
        // Test with two non-empty samples
        double[] x1 = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y1 = {6.0, 7.0, 8.0, 9.0, 10.0};
        double U1 = test.mannWhitneyU(x1, y1);
        assertEquals(25.0, U1, 0.0);

        // Test with empty sample
        double[] x2 = {};
        double[] y2 = {1.0, 2.0, 3.0};
        try {
            test.mannWhitneyU(x2, y2);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected exception
        }
    }

    @Test
    public void testMannWhitneyUTest() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();

        // Test with two non-empty samples
        double[] x1 = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y1 = {6.0, 7.0, 8.0, 9.0, 10.0};
        double p1 = test.mannWhitneyUTest(x1, y1);
        assertEquals(0.25, p1, 0.01);

        // Test with empty sample
        double[] x2 = {};
        double[] y2 = {1.0, 2.0, 3.0};
        try {
            test.mannWhitneyUTest(x2, y2);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected exception
        }
    }

}