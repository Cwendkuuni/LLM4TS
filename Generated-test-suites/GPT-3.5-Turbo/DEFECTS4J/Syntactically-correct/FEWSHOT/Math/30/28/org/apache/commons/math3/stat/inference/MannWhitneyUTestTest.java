import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        
        // Test case 1: x and y have one element each
        double[] x1 = {1.0};
        double[] y1 = {2.0};
        try {
            double result = test.mannWhitneyU(x1, y1);
            assertEquals(1.0, result, 0.0);
        } catch (Exception e) {
            fail("Exception thrown: " + e);
        }
        
        // Test case 2: x has 3 elements and y has 2 elements
        double[] x2 = {1.0, 2.0, 3.0};
        double[] y2 = {4.0, 5.0};
        try {
            double result = test.mannWhitneyU(x2, y2);
            assertEquals(8.0, result, 0.0);
        } catch (Exception e) {
            fail("Exception thrown: " + e);
        }
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        
        // Test case 1: x and y have one element each
        double[] x1 = {1.0};
        double[] y1 = {2.0};
        try {
            double result = test.mannWhitneyUTest(x1, y1);
            assertEquals(1.0, result, 0.0);
        } catch (Exception e) {
            fail("Exception thrown: " + e);
        }
        
        // Test case 2: x has 3 elements and y has 2 elements
        double[] x2 = {1.0, 2.0, 3.0};
        double[] y2 = {4.0, 5.0};
        try {
            double result = test.mannWhitneyUTest(x2, y2);
            assertEquals(0.0196, result, 0.0001);
        } catch (Exception e) {
            fail("Exception thrown: " + e);
        }
    }

}