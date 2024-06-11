import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {
    
    private MannWhitneyUTest test = new MannWhitneyUTest();
    private double[] sample1 = {1.0, 2.0, 3.0, 4.0, 5.0};
    private double[] sample2 = {6.0, 7.0, 8.0, 9.0, 10.0};
    private double[] emptySample = {};
    
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullSample() {
        test.mannWhitneyU(null, sample2);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullSample() {
        test.mannWhitneyUTest(null, sample2);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullSample2() {
        test.mannWhitneyU(sample1, null);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullSample2() {
        test.mannWhitneyUTest(sample1, null);
    }
    
    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_EmptySample() {
        test.mannWhitneyU(emptySample, sample2);
    }
    
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_EmptySample() {
        test.mannWhitneyUTest(emptySample, sample2);
    }
    
    @Test
    public void testMannWhitneyU() {
        double result = test.mannWhitneyU(sample1, sample2);
        assertEquals(15.0, result, 0.001);
    }
    
    @Test
    public void testMannWhitneyUTest() {
        double result = test.mannWhitneyUTest(sample1, sample2);
        assertEquals(0.5743, result, 0.001);
    }
}