import static org.junit.Assert.*;
import org.junit.Test;

public class ContinuedFractionTest {

    @Test
    public void testGetA() {
        // Create an instance of ContinuedFraction subclass for testing
        ContinuedFractionImpl cf = new ContinuedFractionImpl();

        // Testing getA method with a known input
        double result = cf.getA(1, 0.5);
        assertEquals(2.5, result, 0.0001);
    }

    @Test
    public void testGetB() {
        // Create an instance of ContinuedFraction subclass for testing
        ContinuedFractionImpl cf = new ContinuedFractionImpl();

        // Testing getB method with a known input
        double result = cf.getB(1, 0.5);
        assertEquals(3.5, result, 0.0001);
    }

    @Test
    public void testEvaluate() {
        // Create an instance of ContinuedFraction subclass for testing
        ContinuedFractionImpl cf = new ContinuedFractionImpl();

        // Testing evaluate method with a known input
        double result = cf.evaluate(0.5);
        assertEquals(0.625, result, 0.0001);
    }

    // Test edge case where cN and q2 are infinite
    @Test(expected = ConvergenceException.class)
    public void testEvaluateEdgeCaseInfinite() {
        // Create an instance of ContinuedFraction subclass for testing
        ContinuedFractionImpl cf = new ContinuedFractionImpl();

        cf.evaluate(2.0);
    }

    // Test edge case where deltaN approaches 1
    @Test
    public void testEvaluateEdgeCaseDeltaN() {
        // Create an instance of ContinuedFraction subclass for testing
        ContinuedFractionImpl cf = new ContinuedFractionImpl();

        double result = cf.evaluate(0.1);
        assertEquals(0.748, result, 0.001);
    }
}