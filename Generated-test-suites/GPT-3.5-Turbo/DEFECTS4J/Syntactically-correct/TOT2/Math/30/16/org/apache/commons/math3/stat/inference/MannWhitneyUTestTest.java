import static org.junit.Assert.*;
import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTestBasic() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {1.5, 2.5, 3.5};
        double result = test.mannWhitneyU(x, y);
        assertEquals(6.0, result, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullDataConformance() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(null, null);
    }

    @Test(expected = NoDataException.class)
    public void testEmptyDataConformance() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {};
        test.mannWhitneyU(x, y);
    }

    @Test
    public void testMannWhitneyUTestEdgeCases() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 1.0, 1.0};
        double[] y = {2.0, 2.0};
        double result = test.mannWhitneyU(x, y);
        assertEquals(6.0, result, 0.0001);
    }

    @Test
    public void testMannWhitneyUTestExceptionHandling() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {1.5, 2.5, 3.5};
        try {
            double pValue = test.mannWhitneyUTest(x, y);
            assertTrue(pValue >= 0 && pValue <= 1);
        } catch (ConvergenceException | MaxCountExceededException e) {
            fail("Exception thrown when calculating p-value");
        }
    }
}