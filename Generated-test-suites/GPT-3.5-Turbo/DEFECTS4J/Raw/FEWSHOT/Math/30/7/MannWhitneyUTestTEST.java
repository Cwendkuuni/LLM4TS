import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        
        // Test case 1
        double[] x1 = {1, 2, 3, 4, 5};
        double[] y1 = {6, 7, 8, 9, 10};
        assertEquals(0.0, test.mannWhitneyU(x1, y1), 0.0001);
        
        // Test case 2
        double[] x2 = {1.5, 2.5, 3.5, 4.5, 5.5};
        double[] y2 = {6.5, 7.5, 8.5, 9.5, 10.5};
        assertEquals(0.0, test.mannWhitneyU(x2, y2), 0.0001);
        
        // Test case 3
        double[] x3 = {1.2, 2.2, 3.2, 4.2, 5.2};
        double[] y3 = {6.3, 7.3, 8.3, 9.3, 10.3};
        assertEquals(0.0, test.mannWhitneyU(x3, y3), 0.0001);
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        
        // Test case 1
        double[] x1 = {1, 2, 3, 4, 5};
        double[] y1 = {6, 7, 8, 9, 10};
        assertEquals(1.0, test.mannWhitneyUTest(x1, y1), 0.0001);
        
        // Test case 2
        double[] x2 = {1.5, 2.5, 3.5, 4.5, 5.5};
        double[] y2 = {6.5, 7.5, 8.5, 9.5, 10.5};
        assertEquals(1.0, test.mannWhitneyUTest(x2, y2), 0.0001);
        
        // Test case 3
        double[] x3 = {1.2, 2.2, 3.2, 4.2, 5.2};
        double[] y3 = {6.3, 7.3, 8.3, 9.3, 10.3};
        assertEquals(1.0, test.mannWhitneyUTest(x3, y3), 0.0001);
    }
}