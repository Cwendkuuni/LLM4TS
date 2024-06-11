import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.IllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.ContinuedFraction;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ContinuedFractionTest {

    @Test
    public void testEvaluate() {
        ContinuedFraction testObject = new MyContinuedFraction();
        double result = testObject.evaluate(1.0);
        double expected = 1.0;
        assertEquals(expected, result, 1e-9);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateConvergenceException() {
        ContinuedFraction testObject = new MyContinuedFraction();
        testObject.evaluate(Double.POSITIVE_INFINITY, 1e-9, Integer.MAX_VALUE);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxIterations() {
        ContinuedFraction testObject = new MyContinuedFraction();
        testObject.evaluate(0.0, 1e-9, 0);
    }

    @Test
    public void testEvaluateWithNegativeX() {
        ContinuedFraction testObject = new MyContinuedFraction();
        double result = testObject.evaluate(-1.0, 1e-9, Integer.MAX_VALUE);
        double expected = -(testObject.evaluate(1.0, 1e-9, Integer.MAX_VALUE));
        assertEquals(expected, result, 1e-9);
    }

    @Test
    public void testEvaluateWithZeroX() {
        ContinuedFraction testObject = new MyContinuedFraction();
        double result = testObject.evaluate(0.0, 1e-9, Integer.MAX_VALUE);
        double expected = 0.0;
        assertEquals(expected, result, 1e-9);
    }

    @Test
    public void testEvaluateWithNaN() {
        ContinuedFraction testObject = new MyContinuedFraction();
        double result = testObject.evaluate(Double.NaN, 1e-9, Integer.MAX_VALUE);
        double expected = Double.NaN;
        assertEquals(expected, result, 1e-9);
    }

    @Test
    public void testEvaluateWithInf() {
        ContinuedFraction testObject = new MyContinuedFraction();
        double result = testObject.evaluate(Double.POSITIVE_INFINITY, 1e-9, Integer.MAX_VALUE);
        double expected = Double.POSITIVE_INFINITY;
        assertEquals(expected, result, 1e-9);
    }

    @Test
    public void testGetA() {
        ContinuedFraction testObject = new MyContinuedFraction();
        double result = testObject.getA(1, 1.0);
        double expected = 1.0;
        assertEquals(expected, result, 1e-9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAWithInvalidN() {
        ContinuedFraction testObject = new MyContinuedFraction();
        testObject.getA(-1, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAWithInvalidX() {
        ContinuedFraction testObject = new MyContinuedFraction();
        testObject.getA(1, Double.NaN);
    }

    @Test
    public void testGetB() {
        ContinuedFraction testObject = new MyContinuedFraction();
        double result = testObject.getB(1, 1.0);
        double expected = 1.0;
        assertEquals(expected, result, 1e-9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBWithInvalidN() {
        ContinuedFraction testObject = new MyContinuedFraction();
        testObject.getB(-1, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBWithInvalidX() {
        ContinuedFraction testObject = new MyContinuedFraction();
        testObject.getB(1, Double.NaN);
    }

}