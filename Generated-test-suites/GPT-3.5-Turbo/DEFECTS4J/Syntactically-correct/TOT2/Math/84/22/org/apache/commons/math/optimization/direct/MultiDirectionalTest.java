import static org.junit.Assert.*;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testDefaultConstructor() {
        MultiDirectional multiDirectional = new MultiDirectional();
        assertNotNull(multiDirectional);
        assertEquals(2.0, multiDirectional.khi, 0.0);
        assertEquals(0.5, multiDirectional.gamma, 0.0);
    }

    @Test
    public void testCustomConstructor() {
        MultiDirectional multiDirectional = new MultiDirectional(3.0, 0.7);
        assertNotNull(multiDirectional);
        assertEquals(3.0, multiDirectional.khi, 0.0);
        assertEquals(0.7, multiDirectional.gamma, 0.0);
    }

    @Test
    public void testIterateSimplex() {
        // Add test cases for iterateSimplex method
    }

    @Test
    public void testEvaluateNewSimplex() {
        // Add test cases for evaluateNewSimplex method
    }

    // Add more test cases for other methods in the MultiDirectional class

}