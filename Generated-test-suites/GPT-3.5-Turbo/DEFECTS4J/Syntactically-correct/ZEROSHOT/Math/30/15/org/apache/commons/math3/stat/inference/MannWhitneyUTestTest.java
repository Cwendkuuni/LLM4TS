import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {
    
    private MannWhitneyUTest testInstance;
    
    @Before
    public void setUp() {
        testInstance = new MannWhitneyUTest();
    }
    
    @Test
    public void testMannWhitneyU() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        
        double result = testInstance.mannWhitneyU(x, y);
        
        assertEquals(9.0, result, 0.0001);
    }
    
    @Test
    public void testMannWhitneyUTest() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        
        double result = testInstance.mannWhitneyUTest(x, y);
        
        assertEquals(0.3333, result, 0.0001);
    }
}