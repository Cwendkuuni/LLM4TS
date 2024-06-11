import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testDefaultConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        // assertion here
    }
    
    @Test
    public void testConstructorWithStrategies() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.FIXED, TiesStrategy.AVERAGE);
        // assertion here
    }
    
    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceWithNullX() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] y = {1, 2, 3};
        test.ensureDataConformance(null, y);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceWithNullY() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        test.ensureDataConformance(x, null);
    }
    
    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceWithEmptyX() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {1, 2, 3};
        test.ensureDataConformance(x, y);
    }
    
    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceWithEmptyY() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {};
        test.ensureDataConformance(x, y);
    }
    
    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double[] z = test.concatenateSamples(x, y);
        // assertion here
    }
    
    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double U = test.mannWhitneyU(x, y);
        // assertion here
    }
    
    @Test
    public void testCalculateAsymptoticPValue() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double Umin = 10.0;
        int n1 = 3;
        int n2 = 4;
        double pValue = test.calculateAsymptoticPValue(Umin, n1, n2);
        // assertion here
    }
    
    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3};
        double[] y = {4, 5, 6};
        double pValue = test.mannWhitneyUTest(x, y);
        // assertion here
    }

}