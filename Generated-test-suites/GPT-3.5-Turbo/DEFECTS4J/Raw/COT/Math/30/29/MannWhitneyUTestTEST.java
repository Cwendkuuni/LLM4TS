
import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        assertEquals(6.0, test.mannWhitneyU(x, y), 0.0001);
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        assertEquals(1.0, test.mannWhitneyUTest(x, y), 0.0001);
    }
    
    // Edge cases and exception handling tests
    
    @Test(expected = NullArgumentException.class)
    public void testNullX() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = {4.0, 5.0, 6.0};
        test.mannWhitneyUTest(x, y);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testNullY() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = null;
        test.mannWhitneyUTest(x, y);
    }
    
    @Test(expected = NoDataException.class)
    public void testEmptyX() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {4.0, 5.0, 6.0};
        test.mannWhitneyUTest(x, y);
    }
    
    @Test(expected = NoDataException.class)
    public void testEmptyY() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {};
        test.mannWhitneyUTest(x, y);
    }
    
    @Test
    public void testTiesStrategy() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.FIXED, TiesStrategy.MAXIMUM);
        double[] x = {1.0, 2.0, 2.0};
        double[] y = {2.0, 3.0, 4.0};
        assertEquals(4.0, test.mannWhitneyU(x, y), 0.0001);
    }
    
    @Test
    public void testMannWhitneyUWithLargeSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[1000];
        double[] y = new double[1000];
        for (int i = 0; i < 1000; i++) {
            x[i] = i + 1;
            y[i] = i + 1001;
        }
        assertEquals(1000000.0, test.mannWhitneyU(x, y), 0.0001);
    }
    
    @Test
    public void testMannWhitneyUTestWithLargeSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[1000];
        double[] y = new double[1000];
        for (int i = 0; i < 1000; i++) {
            x[i] = i + 1;
            y[i] = i + 1001;
        }
        assertEquals(0.0, test.mannWhitneyUTest(x, y), 0.0001);
    }
}

