import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTestConstructor1() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertNotNull(test);
    }

    @Test
    public void testMannWhitneyUTestConstructor2() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.FIXED, TiesStrategy.AVERAGE);
        assertNotNull(test);
    }

    @Test
    public void testEnsureDataConformanceNull() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertThrows(NullArgumentException.class, () -> test.ensureDataConformance(null, new double[]{1.0, 2.0}));
    }

    @Test
    public void testEnsureDataConformanceEmpty() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertThrows(NoDataException.class, () -> test.ensureDataConformance(new double[]{}, new double[]{1.0, 2.0}));
    }

    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] combined = test.concatenateSamples(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
        assertArrayEquals(new double[]{1.0, 2.0, 3.0, 4.0}, combined, 0.0);
    }

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double result = test.mannWhitneyU(new double[]{1.0, 2.0, 3.0}, new double[]{4.0, 5.0});
        assertEquals(6.0, result, 0.0);
    }

    @Test
    public void testCalculateAsymptoticPValue() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double result = test.calculateAsymptoticPValue(3.0, 3, 2);
        assertEquals(1.0, result, 0.0);
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double result = test.mannWhitneyUTest(new double[]{1.0, 2.0, 3.0}, new double[]{4.0, 5.0});
        assertEquals(1.0, result, 0.0);
    }

}