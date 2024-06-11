import static org.junit.Assert.*;
import org.junit.Test;

public class ContinuedFractionTest {

    @Test
    public void testGetA() {
        ContinuedFraction cf = new ContinuedFractionImpl();
        assertEquals(1.0, cf.getA(0, 1.0), 0.0);
    }

    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFractionImpl();
        assertEquals(2.0, cf.getB(0, 1.0), 0.0);
    }

    @Test
    public void testEvaluate() {
        ContinuedFraction cf = new ContinuedFractionImpl();
        assertEquals(0.3333333333333333, cf.evaluate(1.0), 1e-9);
    }

    @Test
    public void testEvaluateWithEpsilon() {
        ContinuedFraction cf = new ContinuedFractionImpl();
        assertEquals(0.3333333333333333, cf.evaluate(1.0, 1e-9), 1e-9);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction cf = new ContinuedFractionImpl();
        assertEquals(0.3333333333333333, cf.evaluate(1.0, 1e-9, 10000), 1e-9);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateConvergenceException1() {
        ContinuedFraction cf = new ContinuedFractionImpl();
        cf.evaluate(0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateConvergenceException2() {
        ContinuedFraction cf = new ContinuedFractionImpl();
        cf.evaluate(1.0, 1e-9, 1);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateConvergenceException3() {
        ContinuedFraction cf = new ContinuedFractionImpl();
        cf.evaluate(Double.POSITIVE_INFINITY, 1e-9, 10000);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxCountExceededException() {
        ContinuedFraction cf = new ContinuedFractionImpl();
        cf.evaluate(1.0, 1e-9, 1);
    }
}