import org.junit.Test;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

public class MannWhitneyUTestTest {

    @Test
    public void testConstructor1() {
        MannWhitneyUTest test = new MannWhitneyUTest();
    }
    
    @Test
    public void testConstructor2() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.FIXED, TiesStrategy.AVERAGE);
    }
    
    @Test
    public void testMannWhitneyU() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        MannWhitneyUTest test = new MannWhitneyUTest();
        double result = test.mannWhitneyU(x, y);
        //assertions here
    }
    
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUWithNullX() {
        double[] x = null;
        double[] y = {4.0, 5.0, 6.0};
        MannWhitneyUTest test = new MannWhitneyUTest();
        double result = test.mannWhitneyU(x, y);
    }
    
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUWithEmptyY() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {};
        MannWhitneyUTest test = new MannWhitneyUTest();
        double result = test.mannWhitneyU(x, y);
    }
    
    @Test
    public void testMannWhitneyUTest() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        MannWhitneyUTest test = new MannWhitneyUTest();
        double result = test.mannWhitneyUTest(x, y);
        //assertions here
    }
    
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestWithEmptyX() {
        double[] x = {};
        double[] y = {4.0, 5.0, 6.0};
        MannWhitneyUTest test = new MannWhitneyUTest();
        double result = test.mannWhitneyUTest(x, y);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testMannWhitneyUTestWithConvergenceError() {
        double[] x = {1.0, 1.0, 1.0};
        double[] y = {2.0, 2.0, 2.0};
        MannWhitneyUTest test = new MannWhitneyUTest();
        double result = test.mannWhitneyUTest(x, y);
    }
    
    @Test(expected = MaxCountExceededException.class)
    public void testMannWhitneyUTestWithMaxCountExceeded() {
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {5.0, 6.0, 7.0, 8.0};
        MannWhitneyUTest test = new MannWhitneyUTest();
        double result = test.mannWhitneyUTest(x, y);
    }
}