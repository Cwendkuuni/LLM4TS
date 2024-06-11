
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultiDirectionalTest {

    private MultiDirectional multiDirectional;

    @Before
    public void setUp() {
        multiDirectional = new MultiDirectional();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(2.0, multiDirectional.getKhi(), 0.0001);
        assertEquals(0.5, multiDirectional.getGamma(), 0.0001);
    }

    @Test
    public void testParameterizedConstructor() {
        MultiDirectional md = new MultiDirectional(3.0, 0.75);
        assertEquals(3.0, md.getKhi(), 0.0001);
        assertEquals(0.75, md.getGamma(), 0.0001);
    }

    @Test
    public void testIterateSimplex() {
        // TODO: Test the iterateSimplex method with various inputs
    }

    @Test
    public void testEvaluateNewSimplex() {
        // TODO: Test the evaluateNewSimplex method with various inputs
    }

}
