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
        MultiDirectional multiDirectional = new MultiDirectional(3.0, 0.2);
        assertNotNull(multiDirectional);
    }

    @Test
    public void testIterateSimplex() {
        // Test if iterateSimplex method runs without throwing exceptions
        MultiDirectional multiDirectional = new MultiDirectional();
        try {
            multiDirectional.iterateSimplex(null);
        } catch (Exception e) {
            fail("Exception thrown during iterateSimplex method");
        }
    }

    @Test
    public void testEvaluateNewSimplex() {
        // Test if evaluateNewSimplex method runs without throwing exceptions
        MultiDirectional multiDirectional = new MultiDirectional();
        try {
            multiDirectional.evaluateNewSimplex(new RealPointValuePair[1], 1.0, null);
        } catch (Exception e) {
            fail("Exception thrown during evaluateNewSimplex method");
        }
    }

    // Add more test cases for other public methods

}