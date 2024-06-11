import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.optimization.direct.MultiDirectional;
import org.apache.commons.math.optimization.RealPointValuePair;

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
            fail("Exception thrown in iterateSimplex method");
        }
    }

    @Test
    public void testEvaluateNewSimplex() {
        MultiDirectional multiDirectional = new MultiDirectional();
        RealPointValuePair[] original = {new RealPointValuePair(new double[]{1.0, 2.0}, 0.0), new RealPointValuePair(new double[]{2.0, 3.0}, 0.0)};
        double coeff = 0.5;
        try {
            multiDirectional.evaluateNewSimplex(original, coeff, null);
        } catch (Exception e) {
            fail("Exception thrown in evaluateNewSimplex method");
        }
    }

}