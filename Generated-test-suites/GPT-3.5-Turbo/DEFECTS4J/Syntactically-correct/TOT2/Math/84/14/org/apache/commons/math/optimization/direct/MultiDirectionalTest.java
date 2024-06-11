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
        MultiDirectional multiDirectional = new MultiDirectional(1.0, 0.2);
        assertNotNull(multiDirectional);
    }

    @Test
    public void testIterateSimplex() {
        MultiDirectional multiDirectional = new MultiDirectional();
        try {
            multiDirectional.iterateSimplex((a, b) -> a.getValue().compareTo(b.getValue()));
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testEvaluateNewSimplex() {
        MultiDirectional multiDirectional = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[2];
        original[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 5.0);
        original[1] = new RealPointValuePair(new double[]{2.0, 3.0}, 6.0);
        try {
            multiDirectional.evaluateNewSimplex(original, 0.5, (a, b) -> a.getValue().compareTo(b.getValue()));
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
}