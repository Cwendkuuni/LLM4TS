import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    private MannWhitneyUTest uTest = new MannWhitneyUTest();
    private double[] x = {1, 2, 3, 4, 5};
    private double[] y = {6, 7, 8, 9, 10};

    @Test
    public void testMannWhitneyU() {
        double expected = 15.0;
        double result = uTest.mannWhitneyU(x, y);
        assertEquals(expected, result, 0.0001);
    }

    @Test
    public void testMannWhitneyUTest() {
        double expected = 0.0576;
        double result = uTest.mannWhitneyUTest(x, y);
        assertEquals(expected, result, 0.0001);
    }
}