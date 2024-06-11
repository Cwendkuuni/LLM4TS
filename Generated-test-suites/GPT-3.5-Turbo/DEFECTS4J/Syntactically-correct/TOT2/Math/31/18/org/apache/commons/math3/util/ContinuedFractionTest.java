import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ContinuedFractionTest {
    
    @Test
    public void testGetA() {
        // Testing with n = 0 and x = 5
        ContinuedFraction cf = new ContinuedFractionTestImpl();
        assertEquals(1.0, cf.getA(0, 5.0), 0.001);
    }
    
    @Test
    public void testGetB() {
        // Testing with n = 1 and x = 5
        ContinuedFraction cf = new ContinuedFractionTestImpl();
        assertEquals(2.0, cf.getB(1, 5.0), 0.001);
    }
    
    @Test
    public void testEvaluateDefaultEpsilon() {
        // Typical use case with x = 2.0
        ContinuedFraction cf = new ContinuedFractionTestImpl();
        assertEquals(0.5, cf.evaluate(2.0), 0.001);
    }
    
    @Test
    public void testEvaluateCustomEpsilon() {
        // Testing with epsilon = 0.1 and x = 3.0
        ContinuedFraction cf = new ContinuedFractionTestImpl();
        assertEquals(0.6, cf.evaluate(3.0, 0.1), 0.001);
    }
    
    @Test
    public void testEvaluateMaxIterations() {
        // Testing with maxIterations = 5 and x = 4.0
        ContinuedFraction cf = new ContinuedFractionTestImpl();
        assertEquals(0.7, cf.evaluate(4.0, 5), 0.001);
    }
    
    @Test
    public void testEvaluateAllParams() {
        // Testing with all parameters specified
        ContinuedFraction cf = new ContinuedFractionTestImpl();
        assertEquals(0.8, cf.evaluate(5.0, 0.01, 10), 0.001);
    }
    
    @Test
    public void testEvaluateConvergenceException() {
        // Testing convergence exception with x = 0.0
        ContinuedFraction cf = new ContinuedFractionTestImpl();
        try {
            cf.evaluate(0.0);
            fail("ConvergenceException should have been thrown");
        } catch (ConvergenceException e) {
            // Success
        }
    }
    
    @Test
    public void testEvaluateMaxIterationsException() {
        // Testing max iterations exception with x = 1.0
        ContinuedFraction cf = new ContinuedFractionTestImpl();
        try {
            cf.evaluate(1.0, 0.001, 2);
            fail("MaxCountExceededException should have been thrown");
        } catch (MaxCountExceededException e) {
            // Success
        }
    }
    
    private class ContinuedFractionTestImpl extends ContinuedFraction {

        @Override
        protected double getA(int n, double x) {
            return n + x;
        }

        @Override
        protected double getB(int n, double x) {
            return n * x;
        }
    }
}