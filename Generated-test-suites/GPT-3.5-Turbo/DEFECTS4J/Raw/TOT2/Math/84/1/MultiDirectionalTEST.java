import static org.junit.Assert.*;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testDefaultConstructor() {
        MultiDirectional md = new MultiDirectional();
        assertNotNull(md);
        assertEquals(2.0, md.getKhi(), 0.0);
        assertEquals(0.5, md.getGamma(), 0.0);
    }

    @Test
    public void testCustomConstructor() {
        MultiDirectional md = new MultiDirectional(3.0, 0.7);
        assertNotNull(md);
        assertEquals(3.0, md.getKhi(), 0.0);
        assertEquals(0.7, md.getGamma(), 0.0);
    }

    @Test
    public void testIterateSimplex() {
        // Test for normal functionality
        // Create a test scenario and check the results
    }

    @Test
    public void testEvaluateNewSimplex() {
        // Test for edge cases and exception handling
        // Create test scenarios for boundary values and check if exceptions are properly handled
        // Test for cases where functions cannot be evaluated
    }

}