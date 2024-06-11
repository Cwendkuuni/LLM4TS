import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MannWhitneyUTestTest {
    private MannWhitneyUTest mannWhitneyUTest;
    private static final double[] x = {1, 2, 3, 4, 5};
    private static final double[] y = {6, 7, 8, 9, 10};

    @Before
    public void setup() {
        mannWhitneyUTest = new MannWhitneyUTest();
    }

    @Test
    public void testMannWhitneyU() throws Exception {
        double expected = 15;
        double result = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(expected, result, 0);
    }

    @Test
    public void testMannWhitneyUTest() throws Exception {
        double expected = 0.08333333333333333;
        double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(expected, result, 0);
    }
}