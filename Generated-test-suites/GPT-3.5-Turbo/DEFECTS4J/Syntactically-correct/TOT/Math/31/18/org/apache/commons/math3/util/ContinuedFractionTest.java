import org.junit.Assert;
import org.junit.Test;

public class ContinuedFractionTest {

    // Test case for getA method
    @Test
    public void testGetA() {
        ContinuedFraction cf = new ContinuedFractionImpl();
        double result = cf.getA(0, 1.0);
        Assert.assertEquals(1.0, result, 0.0001);
    }

    // Test case for getB method
    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFractionImpl();
        double result = cf.getB(0, 1.0);
        Assert.assertEquals(2.0, result, 0.0001);
    }

    // Test case for evaluate method with default epsilon and max iterations
    @Test
    public void testEvaluateDefault() {
        ContinuedFraction cf = new ContinuedFractionImpl();
        double result = cf.evaluate(1.0);
        Assert.assertEquals(1.0, result, 0.0001);
    }

    // Test case for evaluate method with custom epsilon and max iterations
    @Test
    public void testEvaluateCustom() {
        ContinuedFraction cf = new ContinuedFractionImpl();
        double result = cf.evaluate(1.0, 0.0001, 10);
        Assert.assertEquals(1.0, result, 0.0001);
    }

    // Test case for evaluate method with max iterations exceeding limit
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxIterationsExceeded() {
        ContinuedFraction cf = new ContinuedFractionImpl();
        cf.evaluate(1.0, 0.0001, 5);
    }

    // Test case for evaluate method with infinite divergence
    @Test(expected = ConvergenceException.class)
    public void testEvaluateInfiniteDivergence() {
        ContinuedFraction cf = new ContinuedFractionImpl();
        cf.evaluate(1.0, 0.0001, Integer.MAX_VALUE);
    }

    // Test case for evaluate method with NaN divergence
    @Test(expected = ConvergenceException.class)
    public void testEvaluateNanDivergence() {
        ContinuedFraction cf = new ContinuedFractionImpl();
        cf.evaluate(Double.NaN, 0.0001, Integer.MAX_VALUE);
    }
}

class ContinuedFractionImpl extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return 1.0;
    }

    @Override
    protected double getB(int n, double x) {
        return 2.0;
    }
}