import org.apache.commons.math3.MathException;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import static org.junit.Assert.*;

import org.junit.Test;

public class ContinuedFractionTest {

    @Test
    public void testEvaluateSquareRootOfTwo() {
        ContinuedFraction cf = new SquareRootContinuedFraction();
        try {
            double result = cf.evaluate(2.0);
            assertEquals(Math.sqrt(2), result, 1e-6);
        } catch (ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateSquareRootOfTwoLimit() {
        ContinuedFraction cf = new SquareRootContinuedFraction();
        cf.evaluate(2.0, 0.0, 0);
    }

    @Test
    public void testEvaluateE() {
        ContinuedFraction cf = new EContinuedFraction();
        try {
            double result = cf.evaluate(0.0);
            assertEquals(Math.E, result, 1e-6);
        } catch (ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateELimit() {
        ContinuedFraction cf = new EContinuedFraction();
        cf.evaluate(0.0, 0.0, 0);
    }

    @Test
    public void testEvaluateLogarithm2() {
        ContinuedFraction cf = new LogarithmBase2ContinuedFraction();
        try {
            double result = cf.evaluate(1.0 / Math.E);
            assertEquals(1.0 / Math.log(2), result, 1e-6);
        } catch (ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateLogarithm2Limit() {
        ContinuedFraction cf = new LogarithmBase2ContinuedFraction();
        cf.evaluate(1.0 / Math.E, 0.0, 0);
    }

    @Test
    public void testEvaluateLogarithmE() {
        ContinuedFraction cf = new LogarithmBaseEContinuedFraction();
        try {
            double result = cf.evaluate(1.0);
            assertEquals(1.0 / Math.E, result, 1e-6);
        } catch (ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateLogarithmELimit() {
        ContinuedFraction cf = new LogarithmBaseEContinuedFraction();
        cf.evaluate(1.0, 0.0, 0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateSine() {
        ContinuedFraction cf = new SineContinuedFraction();
        cf.evaluate(Math.PI, 1e-12, 10000);
    }

    @Test
    public void testEvaluateCosine() {
        ContinuedFraction cf = new CosineContinuedFraction();
        try {
            double result = cf.evaluate(Math.PI);
            assertEquals(Math.cos(Math.PI), result, 1e-6);
        } catch (ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateCosineLimit() {
        ContinuedFraction cf = new CosineContinuedFraction();
        cf.evaluate(Math.PI, 0.0, 0);
    }

    @Test
    public void testEvaluateTan() {
        ContinuedFraction cf = new TanContinuedFraction();
        try {
            double result = cf.evaluate(Math.PI / 4);
            assertEquals(Math.tan(Math.PI / 4), result, 1e-6);
        } catch (ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateTanLimit() {
        ContinuedFraction cf = new TanContinuedFraction();
        cf.evaluate(Math.PI / 4, 0.0, 0);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateCot() {
        ContinuedFraction cf = new CotContinuedFraction();
        cf.evaluate(Math.PI / 4, 0.0, 0);
    }

    @Test
    public void testEvaluateArctan() {
        ContinuedFraction cf = new ArctanContinuedFraction();
        try {
            double result = cf.evaluate(Math.PI / 4);
            assertEquals(Math.PI / 4, result, 1e-6);
        } catch (ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateArctanLimit() {
        ContinuedFraction cf = new ArctanContinuedFraction();
        cf.evaluate(Math.PI / 4, 0.0, 0);
    }

    @Test
    public void testEvaluateArccot() {
        ContinuedFraction cf = new ArccotContinuedFraction();
        try {
            double result = cf.evaluate(1.0 / 3.0);
            assertEquals(Math.PI / 3.0, result, 1e-6);
        } catch (ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateArccotLimit() {
        ContinuedFraction cf = new ArccotContinuedFraction();
        cf.evaluate(1.0 / 3.0, 0.0, 0);
    }

    @Test
    public void testEvaluateArcsin() {
        ContinuedFraction cf = new ArcsinContinuedFraction();
        try {
            double result = cf.evaluate(1.0 / Math.sqrt(2));
            assertEquals(Math.PI / 4, result, 1e-6);
        } catch (ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateArcsinLimit() {
        ContinuedFraction cf = new ArcsinContinuedFraction();
        cf.evaluate(1.0 / Math.sqrt(2), 0.0, 0);
    }

    @Test
    public void testEvaluateArccsc() {
        ContinuedFraction cf = new ArccscContinuedFraction();
        try {
            double result = cf.evaluate(Math.sqrt(2) / 2);
            assertEquals(Math.PI / 6, result, 1e-6);
        } catch (ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateArccscLimit() {
        ContinuedFraction cf = new ArccscContinuedFraction();
        cf.evaluate(Math.sqrt(2) / 2, 0.0, 0);
    }
}