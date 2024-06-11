import static org.junit.Assert.*;
import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double result = test.mannWhitneyU(x, y);
        double expected = 9.0;
        assertEquals(expected, result, 0.0001);
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double result = test.mannWhitneyUTest(x, y);
        double expected = 0.0958;
        assertEquals(expected, result, 0.0001);
    }
}