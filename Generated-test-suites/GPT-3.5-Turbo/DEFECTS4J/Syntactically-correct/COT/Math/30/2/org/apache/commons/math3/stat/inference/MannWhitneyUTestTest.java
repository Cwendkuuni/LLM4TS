import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {
    
    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        assertEquals(15.0, test.mannWhitneyU(x, y), 0.0001);
    }
    
    @Test
    public void testEnsureDataConformance() {
        // Test NullArgumentException
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = {1, 2, 3};
        try {
            test.ensureDataConformance(x, y);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
        
        // Test NoDataException
        double[] x2 = {};
        double[] y2 = {1, 2, 3};
        try {
            test.ensureDataConformance(x2, y2);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // expected
        }
    }
    
    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double[] expected = {1, 2, 3, 4, 5, 6};
        assertArrayEquals(expected, test.concatenateSamples(x, y), 0.0001);
    }
    
    @Test
    public void testCalculateAsymptoticPValue() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double Umin = 5.0;
        int n1 = 3;
        int n2 = 4;
        double expected = 0.1281;
        assertEquals(expected, test.calculateAsymptoticPValue(Umin, n1, n2), 0.0001);
    }
    
    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double expected = 0.1281;
        assertEquals(expected, test.mannWhitneyUTest(x, y), 0.0001);
    }
}