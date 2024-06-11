import static org.junit.Assert.*;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testDefaultConstructor() {
        MultiDirectional multiDirectional = new MultiDirectional();
        assertNotNull(multiDirectional);
    }

    @Test
    public void testCustomConstructor() {
        MultiDirectional multiDirectional = new MultiDirectional(2.5, 0.3);
        assertNotNull(multiDirectional);
    }

    @Test
    public void testIterateSimplex() {
        MultiDirectional multiDirectional = new MultiDirectional();
        try {
            multiDirectional.iterateSimplex(null);
        } catch (Exception e) {
            fail("Exception thrown when it shouldn't have.");
        }
    }

    @Test
    public void testEvaluateNewSimplex() {
        MultiDirectional multiDirectional = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[2];
        original[0] = new RealPointValuePair(new double[2], 0.0, true);
        original[1] = new RealPointValuePair(new double[2], 0.0, true);
        try {
            multiDirectional.evaluateNewSimplex(original, 1.5, null);
        } catch (Exception e) {
            fail("Exception thrown when it shouldn't have.");
        }
    }
}