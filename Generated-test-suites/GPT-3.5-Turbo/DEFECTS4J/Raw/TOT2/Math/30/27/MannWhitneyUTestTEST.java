import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    @Test
    public void testConstructor1() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertNotNull(test);
    }

    @Test
    public void testConstructor2() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.FIXED, TiesStrategy.AVERAGE);
        assertNotNull(test);
    }

    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0};
        double[] expected = {1.0, 2.0, 3.0, 4.0, 5.0};
        assertArrayEquals(expected, test.concatenateSamples(x, y), 0.0001);
    }

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0};
        double expected = 3.0;
        assertEquals(expected, test.mannWhitneyU(x, y), 0.0001);
    }

    @Test
    public void testCalculateAsymptoticPValue() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double Umin = 3.0;
        int n1 = 3;
        int n2 = 2;
        double pValue = test.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.08569516, pValue, 0.0001);
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0};
        double pValue = test.mannWhitneyUTest(x, y);
        assertEquals(0.08569516, pValue, 0.0001);
    }
}