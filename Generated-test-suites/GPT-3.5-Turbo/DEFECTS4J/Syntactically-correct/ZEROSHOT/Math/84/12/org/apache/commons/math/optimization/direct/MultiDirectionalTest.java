import static org.junit.Assert.*;
import org.junit.*;

public class MultiDirectionalTest {

    private MultiDirectional multiDirectional;

    @Before
    public void setUp() {
        multiDirectional = new MultiDirectional();
    }

    @Test
    public void testIterateSimplex() {
        try {
            multiDirectional.iterateSimplex(null);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testEvaluateNewSimplex() {
        RealPointValuePair[] original = new RealPointValuePair[1];
        original[0] = new RealPointValuePair(new double[]{0.0}, 0.0);
        try {
            RealPointValuePair result = multiDirectional.evaluateNewSimplex(original, 1.0, null);
            assertNotNull(result);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @After
    public void tearDown() {
        multiDirectional = null;
    }

}