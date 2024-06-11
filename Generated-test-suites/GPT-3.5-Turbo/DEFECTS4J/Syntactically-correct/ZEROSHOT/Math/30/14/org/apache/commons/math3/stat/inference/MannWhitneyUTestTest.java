import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MannWhitneyUTestTest {
    
    private MannWhitneyUTest test;
    
    @Before
    public void setUp() {
        test = new MannWhitneyUTest();
    }
    
    @Test
    public void testMannWhitneyU() {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        
        try {
            double result = test.mannWhitneyU(x, y);
            assertEquals(15, result, 0.0001);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testMannWhitneyUTest() {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        
        try {
            double result = test.mannWhitneyUTest(x, y);
            assertEquals(0.0175, result, 0.0001);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}