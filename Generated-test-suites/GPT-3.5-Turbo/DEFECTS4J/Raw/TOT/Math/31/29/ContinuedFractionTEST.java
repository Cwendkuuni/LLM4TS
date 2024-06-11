import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.ContinuedFraction;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContinuedFractionTest {

    private class TestContinuedFraction extends ContinuedFraction {

        @Override
        protected double getA(int n, double x) {
            // TODO: implementation specific to test
            return 0;
        }

        @Override
        protected double getB(int n, double x) {
            // TODO: implementation specific to test
            return 0;
        }
    }

    @Test
    public void testEvaluate_StandardCase() {
        ContinuedFraction cf = new TestContinuedFraction();

        double result = cf.evaluate(3.5);

        assertEquals(0, result, 0.001);
    }

    @Test
    public void testEvaluate_SmallEpsilon() {
        ContinuedFraction cf = new TestContinuedFraction();

        double result = cf.evaluate(3.5, 0.000001);

        assertEquals(0, result, 0.0001);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluate_ConvergenceException() {
        ContinuedFraction cf = new TestContinuedFraction();

        double result = cf.evaluate(3.5, 0.000001, 10);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluate_MaxCountExceededException() {
        ContinuedFraction cf = new TestContinuedFraction();

        double result = cf.evaluate(3.5, 0.000001, 1);
    }

}