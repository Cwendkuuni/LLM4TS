
import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {
    
    // Test the constructor - MannWhitneyUTest()
    @Test
    public void testConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertNotNull(test);
    }
    
    // Test the constructor - MannWhitneyUTest(nanStrategy, tiesStrategy)
    @Test
    public void testConstructorWithStrategies() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.REMOVED, TiesStrategy.MINIMUM);
        assertNotNull(test);
    }
    
    // Test the mannWhitneyU(double[] x, double[] y) method
    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double result = test.mannWhitneyU(x, y);
        assertEquals(9.0, result, 0.0001);
    }
    
    // Test the calculateAsymptoticPValue(double Umin, int n1, int n2) method
    @Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double result = test.calculateAsymptoticPValue(9.0, 3, 3);
        assertEquals(0.19061989000158697, result, 0.0001);
    }
    
    // Test the mannWhitneyUTest(double[] x, double[] y) method
    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double result = test.mannWhitneyUTest(x, y);
        assertEquals(0.38123978000317395, result, 0.0001);
    }

}
