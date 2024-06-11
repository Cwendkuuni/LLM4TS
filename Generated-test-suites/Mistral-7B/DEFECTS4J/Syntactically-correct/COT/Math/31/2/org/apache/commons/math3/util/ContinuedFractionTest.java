import org.apache.commons.math3.util.ContinuedFraction;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.ConvergenceException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the ContinuedFraction class.
 */
public class ContinuedFractionTest {

    @Test
    public void testGetA() {
        ContinuedFraction continuedFraction = new DummyContinuedFraction();
        double result;
        double x = 0.1;

        for (int i = 0; i <= 10; i++) {
            result = continuedFraction.getA(i, x);
            assertEquals("Invalid result for getA(i, x) for i = " + i, i + 1.0, result, 0.001);
        }
    }

    @Test
    public void testGetB() {
        ContinuedFraction continuedFraction = new DummyContinuedFraction();
        double result;
        double x = 0.1;

        for (int i = 0; i <= 10; i++) {
            result = continuedFraction.getB(i, x);
            assertEquals("Invalid result for getB(i, x) for i = " + i, i, result, 0.001);
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithInfiniteResult() {
        ContinuedFraction continuedFraction = new DummyContinuedFraction();
        continuedFraction.evaluate(0.1, 0.0, Integer.MAX_VALUE);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithNaNResult() {
        ContinuedFraction continuedFraction = new DummyContinuedFraction();
        continuedFraction.evaluate(0.1, Double.NaN, Integer.MAX_VALUE);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction continuedFraction = new DummyContinuedFraction();
        continuedFraction.evaluate(0.1, 1e-10, 1);
    }

    @Test
    public void testEvaluate() {
        ContinuedFraction continuedFraction = new DummyContinuedFraction();
        double result;
        double x = 0.1;

        result = continuedFraction.evaluate(x);
        assertEquals("Invalid result for evaluate(x)", 2.602499565009323, result, 0.001);

        result = continuedFraction.evaluate(x, 1e-10);
        assertEquals("Invalid result for evaluate(x, epsilon)", 2.602499565009323, result, 0.001);

        result = continuedFraction.evaluate(x, 1e-10, 5);
        assertEquals("Invalid result for evaluate(x, epsilon, maxIterations)", 2.602499565009323, result, 0.001);
    }
}

class DummyContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return n + 1.0;
    }

    @Override
    protected double getB(int n, double x) {
        return n;
    }
}