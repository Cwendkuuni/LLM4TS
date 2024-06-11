import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    @Test
    public void testDefaultConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertNotNull(test);
    }

    @Test
    public void testCustomConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.REMOVED, TiesStrategy.MINIMUM);
        assertNotNull(test);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullDataConformance() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(null, null);
    }

    @Test(expected = NoDataException.class)
    public void testEmptyDataConformance() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(new double[]{}, new double[]{});
    }

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        assertEquals(9.0, test.mannWhitneyU(x, y), 0.0001);
    }

    @Test
    public void testAsymptoticPValue() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double pValue = test.calculateAsymptoticPValue(6.0, 3, 3);
        assertEquals(0.3326, pValue, 0.0001);
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double pValue = test.mannWhitneyUTest(x, y);
        assertEquals(0.3326, pValue, 0.0001);
    }

}