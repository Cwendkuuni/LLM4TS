
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

    @Test
    public void testConstructor() {
        MultiDirectional multiDirectional = new MultiDirectional();
        assertNotNull(multiDirectional);
        assertEquals(2.0, multiDirectional.khi, 0.0);
        assertEquals(0.5, multiDirectional.gamma, 0.0);
    }

    @Test
    public void testConstructorWithCoefficients() {
        MultiDirectional multiDirectional = new MultiDirectional(3.0, 0.25);
        assertNotNull(multiDirectional);
        assertEquals(3.0, multiDirectional.khi, 0.0);
        assertEquals(0.25, multiDirectional.gamma, 0.0);
    }

    @Test
    public void testIterateSimplex() {
        // TODO: Add test case for iterateSimplex method
    }

    @Test
    public void testEvaluateNewSimplex() {
        // TODO: Add test case for evaluateNewSimplex method
    }
}
