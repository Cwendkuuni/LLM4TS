import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class ContinuedFractionTest {

    @Test
    public void testGetA() {
        // Test typical use case
        ContinuedFraction cf = new ContinuedFractionImpl();
        double result = cf.getA(3, 2.5);
        Assert.assertEquals(0.65625, result, 0.00001);
    }

    @Test
    public void testGetA_OutOfRange() {
        // Test error scenario (out of range)
        ContinuedFraction cf = new ContinuedFractionImpl();
        try {
            double result = cf.getA(-1, 2.5);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Coefficient index must be greater than or equal to zero",
                                e.getMessage());
        }
    }

    @Test
    public void testGetB() {
        // Test typical use case
        ContinuedFraction cf = new ContinuedFractionImpl();
        double result = cf.getB(3, 2.5);
        Assert.assertEquals(1.875, result, 0.00001);
    }

    @Test
    public void testGetB_OutOfRange() {
        // Test error scenario (out of range)
        ContinuedFraction cf = new ContinuedFractionImpl();
        try {
            double result = cf.getB(-1, 2.5);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Coefficient index must be greater than or equal to zero",
                                e.getMessage());
        }
    }

    @Test
    public void testEvaluate_DefaultEpsilon() {
        // Test typical use case
        ContinuedFraction cf = new ContinuedFractionImpl();
        double result = cf.evaluate(2.5);
        Assert.assertEquals(0.50625, result, 0.00001);
    }

    @Test
    public void testEvaluate_CustomEpsilon() {
        // Test typical use case
        ContinuedFraction cf = new ContinuedFractionImpl();
        double result = cf.evaluate(2.5, 0.0001);
        Assert.assertEquals(0.50625, result, 0.00001);
    }

    @Test
    public void testEvaluate_CustomMaxIterations() {
        // Test typical use case
        ContinuedFraction cf = new ContinuedFractionImpl();
        double result = cf.evaluate(2.5, 10);
        Assert.assertEquals(0.50625, result, 0.00001);
    }

    @Test
    public void testEvaluate_CustomEpsilonAndMaxIterations() {
        // Test typical use case
        ContinuedFraction cf = new ContinuedFractionImpl();
        double result = cf.evaluate(2.5, 0.0001, 10);
        Assert.assertEquals(0.50625, result, 0.00001);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluate_ConvergenceException() {
        // Test error scenario (ConvergenceException)
        ContinuedFraction cf = new ContinuedFractionImpl();
        cf.evaluate(0.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluate_MaxCountExceededException() {
        // Test error scenario (MaxCountExceededException)
        ContinuedFraction cf = new ContinuedFractionImpl();
        cf.evaluate(2.5, 0.0001, 5);
    }

    private static class ContinuedFractionImpl extends ContinuedFraction {

        @Override
        protected double getA(int n, double x) {
            double[] aCoefficients = {1.5, 2.0, 3.0, 4.0, 5.0};
            if (n >= aCoefficients.length) {
                throw new IllegalArgumentException("Coefficient index must be less than the array length");
            }
            return aCoefficients[n];
        }

        @Override
        protected double getB(int n, double x) {
            double[] bCoefficients = {1.0, 1.5, 2.0, 2.5, 3.0};
            if (n >= bCoefficients.length) {
                throw new IllegalArgumentException("Coefficient index must be less than the array length");
            }
            return bCoefficients[n];
        }

    }

}