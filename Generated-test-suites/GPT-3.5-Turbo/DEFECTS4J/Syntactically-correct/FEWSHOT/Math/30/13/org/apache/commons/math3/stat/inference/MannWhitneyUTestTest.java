import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MannWhitneyUTestTest {
    
    @Test
    public void testEnsureDataConformance() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        test.ensureDataConformance(x, y);
        
        double[] xEmpty = {};
        try {
            test.ensureDataConformance(xEmpty, y);
        } catch (NoDataException e) {
            // expected exception
        }
        
        try {
            test.ensureDataConformance(x, null);
        } catch (NullArgumentException e) {
            // expected exception
        }
    }
    
    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        
        double[] expectedResult = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
        double[] result = test.concatenateSamples(x, y);
        
        assertEquals(expectedResult.length, result.length);
        for (int i = 0; i < expectedResult.length; i++) {
            assertEquals(expectedResult[i], result[i], 0.0001);
        }
    }
    
    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        
        double expectedResult = 0.0;
        double result = test.mannWhitneyU(x, y);
        
        assertEquals(expectedResult, result, 0.0001);
    }
    
    @Test
    public void testCalculateAsymptoticPValue() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        
        double Umin = 0.0;
        int n1 = 3;
        int n2 = 3;
        
        double expectedResult = 1.0;
        double result = test.calculateAsymptoticPValue(Umin, n1, n2);
        
        assertEquals(expectedResult, result, 0.0001);
    }
    
    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};

        double expectedResult = 1.0;
        double result = test.mannWhitneyUTest(x, y);
        
        assertEquals(expectedResult, result, 0.0001);
    }

}