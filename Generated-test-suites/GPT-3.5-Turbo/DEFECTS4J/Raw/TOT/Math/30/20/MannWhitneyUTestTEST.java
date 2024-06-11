import org.junit.Assert;
import org.junit.Test;

public class MannWhitneyUTestTest {
    
    @Test
    public void testMannWhitneyU() {
        // Typical use case
        double[] x1 = {1, 2, 3, 4, 5};
        double[] y1 = {6, 7, 8, 9, 10};
        MannWhitneyUTest test1 = new MannWhitneyUTest();
        double U1 = test1.mannWhitneyU(x1, y1);
        Assert.assertEquals(5.0, U1, 0.0);
        
        // Edge case: Samples with only one observation
        double[] x2 = {1};
        double[] y2 = {2};
        MannWhitneyUTest test2 = new MannWhitneyUTest();
        double U2 = test2.mannWhitneyU(x2, y2);
        Assert.assertEquals(0.0, U2, 0.0);
        
        // Error scenario: Null input
        double[] x3 = null;
        double[] y3 = {1, 2, 3};
        MannWhitneyUTest test3 = new MannWhitneyUTest();
        try {
            double U3 = test3.mannWhitneyU(x3, y3);
            Assert.fail("Expected NullArgumentException was not thrown");
        } catch (NullArgumentException e) {
            // Expected exception
        } catch (Exception e) {
            Assert.fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testMannWhitneyUTest() {
        // Typical use case
        double[] x1 = {1, 2, 3, 4, 5};
        double[] y1 = {6, 7, 8, 9, 10};
        MannWhitneyUTest test1 = new MannWhitneyUTest();
        double pValue1 = test1.mannWhitneyUTest(x1, y1);
        Assert.assertEquals(0.142857, pValue1, 0.000001);
        
        // Edge case: Samples with only one observation
        double[] x2 = {1};
        double[] y2 = {2};
        MannWhitneyUTest test2 = new MannWhitneyUTest();
        double pValue2 = test2.mannWhitneyUTest(x2, y2);
        Assert.assertEquals(0.5, pValue2, 0.0);
        
        // Error scenario: Null input
        double[] x3 = null;
        double[] y3 = {1, 2, 3};
        MannWhitneyUTest test3 = new MannWhitneyUTest();
        try {
            double pValue3 = test3.mannWhitneyUTest(x3, y3);
            Assert.fail("Expected NullArgumentException was not thrown");
        } catch (NullArgumentException e) {
            // Expected exception
        } catch (Exception e) {
            Assert.fail("Unexpected exception thrown");
        }
    }
}