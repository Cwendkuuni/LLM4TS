import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MultiDirectionalTest {

    @Test
    public void testDefaultConstructor() {
        MultiDirectional multiDirectional = new MultiDirectional();
        assertEquals(2.0, multiDirectional.getKhi(), 0.0);
        assertEquals(0.5, multiDirectional.getGamma(), 0.0);
    }

    @Test
    public void testCustomConstructor() {
        MultiDirectional multiDirectional = new MultiDirectional(1.0, 0.2);
        assertEquals(1.0, multiDirectional.getKhi(), 0.0);
        assertEquals(0.2, multiDirectional.getGamma(), 0.0);
    }

    @Test
    public void testIterateSimplex() {
        // Implement test cases for iterateSimplex method
    }

    @Test
    public void testEvaluateNewSimplex() {
        // Implement test cases for evaluateNewSimplex method
    }

}