
import static org.junit.Assert.*;
import org.junit.Test;

public class MannWhitneyUTestTest {

    // test for mannWhitneyU method
    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        assertEquals(15.0, test.mannWhitneyU(x, y), 0.0001);
        
        double[] a = {1.5, 2.5, 3.5, 4.5};
        double[] b = {5.5, 6.5, 7.5};
        assertEquals(6.0, test.mannWhitneyU(a, b), 0.0001);
    }

    // test for calculateAsymptoticPValue method
    @Test
    public void testCalculateAsymptoticPValue() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertEquals(0.05, test.calculateAsymptoticPValue(5, 5, 5), 0.0001);
        assertEquals(0.01, test.calculateAsymptoticPValue(10, 10, 10), 0.0001);
    }

    // test for mannWhitneyUTest method
    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.5, 2.5, 3.5, 4.5};
        double[] y = {5.5, 6.5, 7.5};
        assertEquals(0.04746, test.mannWhitneyUTest(x, y), 0.0001);
        
        double[] a = {1, 2, 3, 4, 5};
        double[] b = {6, 7, 8, 9, 10};
        assertEquals(0.001, test.mannWhitneyUTest(a, b), 0.0001);
    }
}
