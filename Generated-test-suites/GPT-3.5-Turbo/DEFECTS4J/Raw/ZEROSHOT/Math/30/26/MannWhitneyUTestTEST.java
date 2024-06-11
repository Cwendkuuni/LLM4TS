import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double expected = 9.0;
        double result = mannWhitneyUTest.mannWhitneyU(x, y);
        Assert.assertEquals(expected, result, 0.00001);
    }

    @Test
    public void testMannWhitneyUWithEmptyArray() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {1.0, 2.0, 3.0};
      
        try {
            mannWhitneyUTest.mannWhitneyU(x, y);
            Assert.fail("Expected NoDataException to be thrown");
        } catch (NoDataException e) {
            // NoDataException expected
        } catch (NullArgumentException e) {
            Assert.fail("Expected NoDataException to be thrown");
        }
    }

    @Test
    public void testMannWhitneyUWithNullArray() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = null;
      
        try {
            mannWhitneyUTest.mannWhitneyU(x, y);
            Assert.fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            // NullArgumentException expected
        } catch (NoDataException e) {
            Assert.fail("Expected NullArgumentException to be thrown");
        }
    }

    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double expected = 0.14285714285714285;
        double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
        Assert.assertEquals(expected, result, 0.00001);
    }

    @Test
    public void testMannWhitneyUTestWithEmptyArray() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {1.0, 2.0, 3.0};
      
        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            Assert.fail("Expected NoDataException to be thrown");
        } catch (NoDataException e) {
            // NoDataException expected
        } catch (NullArgumentException e) {
            Assert.fail("Expected NoDataException to be thrown");
        } catch (ConvergenceException e) {
            Assert.fail("Expected NoDataException to be thrown");
        } catch (MaxCountExceededException e) {
            Assert.fail("Expected NoDataException to be thrown");
        }
    }

    @Test
    public void testMannWhitneyUTestWithNullArray() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = null;
      
        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            Assert.fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            // NullArgumentException expected
        } catch (NoDataException e) {
            Assert.fail("Expected NullArgumentException to be thrown");
        } catch (ConvergenceException e) {
            Assert.fail("Expected NullArgumentException to be thrown");
        } catch (MaxCountExceededException e) {
            Assert.fail("Expected NullArgumentException to be thrown");
        }
    }

    @Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double Umin = 9;
        int n1 = 3;
        int n2 = 3;
        double expected = 0.14285714285714285;
        double result = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        Assert.assertEquals(expected, result, 0.00001);
    }

    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double[] expected = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
        double[] result = mannWhitneyUTest.concatenateSamples(x, y);
        Assert.assertArrayEquals(expected, result, 0.00001);
    }

    @Test
    public void testEnsureDataConformance() throws NullArgumentException, NoDataException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
      
        mannWhitneyUTest.ensureDataConformance(x, y);
    }

    @Test
    public void testEnsureDataConformanceWithEmptyArray() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {1.0, 2.0, 3.0};
      
        try {
            mannWhitneyUTest.ensureDataConformance(x, y);
            Assert.fail("Expected NoDataException to be thrown");
        } catch (NoDataException e) {
            // NoDataException expected
        } catch (NullArgumentException e) {
            Assert.fail("Expected NoDataException to be thrown");
        }
    }

    @Test
    public void testEnsureDataConformanceWithNullArray() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = null;
      
        try {
            mannWhitneyUTest.ensureDataConformance(x, y);
            Assert.fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            // NullArgumentException expected
        } catch (NoDataException e) {
            Assert.fail("Expected NullArgumentException to be thrown");
        }
    }

}