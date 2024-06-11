import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

    // Test cases for public methods in MultiDirectional class

    @Test
    public void testConstructorDefault() {
        MultiDirectional md = new MultiDirectional();
        assertEquals(2.0, md.getKhi(), 0.0);
        assertEquals(0.5, md.getGamma(), 0.0);
    }

    @Test
    public void testConstructorWithCoefficients() {
        MultiDirectional md = new MultiDirectional(3.0, 0.7);
        assertEquals(3.0, md.getKhi(), 0.0);
        assertEquals(0.7, md.getGamma(), 0.0);
    }

    // Additional test cases for edge cases and exceptions

    @Test
    public void testEvaluateNewSimplex() {
        RealPointValuePair[] original = {new RealPointValuePair(new double[]{1.0, 1.0}, 0.0),
                                         new RealPointValuePair(new double[]{2.0, 2.0}, 0.0)};
        MultiDirectional md = new MultiDirectional();
        try {
            RealPointValuePair result = md.evaluateNewSimplex(original, 0.5, comparator);
            assertEquals(1.0, result.getPointRef()[0], 0.0);
            assertEquals(1.0, result.getPointRef()[1], 0.0);
        } catch (FunctionEvaluationException e) {
            fail("Function evaluation exception should not occur");
        } catch (OptimizationException e) {
            fail("Optimization exception should not occur");
        }
    }

    // Other test cases for remaining methods would go here

}