import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Test;

public class ContinuedFractionTest {

    @Test
    public void testEvaluate_defaultCase() {
        ContinuedFractionImpl cf = new ContinuedFractionImpl();
        double result = cf.evaluate(1.0);
        assertEquals(0.5, result, 0.000001);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluate_nonConvergentCase() {
        ContinuedFractionImpl cf = new ContinuedFractionImpl();
        cf.evaluate(0.0, 1e-9, 100);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluate_maxCountExceeded() {
        ContinuedFractionImpl cf = new ContinuedFractionImpl();
        cf.evaluate(1.0, 1e-9, 10);
    }

    @Test
    public void testGetA() {
        ContinuedFractionImpl cf = new ContinuedFractionImpl();
        double result = cf.getA(0, 1.0);
        assertEquals(1.0, result, 0.000001);
    }

    @Test
    public void testGetB() {
        ContinuedFractionImpl cf = new ContinuedFractionImpl();
        double result = cf.getB(0, 1.0);
        assertEquals(1.0, result, 0.000001);
    }

    @Test
    public void testEvaluate_epsilonChange() {
        ContinuedFractionImpl cf = new ContinuedFractionImpl();
        double result1 = cf.evaluate(1.0, 1e-9, 100);
        double result2 = cf.evaluate(1.0, 1e-6, 100);
        assertEquals(result1, result2, 0.000001);
    }
}