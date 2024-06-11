import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.optimization.direct.MultiDirectional;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.RealConvergenceChecker;

public class MultiDirectionalTest {

    @Test
    public void testDefaultConstructor() {
        MultiDirectional multiDirectional = new MultiDirectional();
        assertNotNull(multiDirectional);
    }

    @Test
    public void testCustomConstructor() {
        MultiDirectional multiDirectional = new MultiDirectional(2.0, 0.5);
        assertNotNull(multiDirectional);
    }

    @Test
    public void testIterateSimplex() {
        MultiDirectional multiDirectional = new MultiDirectional();
        try {
            multiDirectional.iterateSimplex(null); // passing null comparator for testing
        } catch (Exception e) {
            fail("Exception should not be thrown in iterateSimplex method");
        }
    }

    @Test
    public void testEvaluateNewSimplex() {
        MultiDirectional multiDirectional = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[2];
        original[0] = new RealPointValuePair(new double[]{0.0}, 0.0);
        original[1] = new RealPointValuePair(new double[]{1.0}, 1.0);
        double coeff = 1.0;
        try {
            RealPointValuePair result = multiDirectional.evaluateNewSimplex(original, coeff, null); // passing null comparator for testing
            assertNotNull(result);
        } catch (Exception e) {
            fail("Exception should not be thrown in evaluateNewSimplex method");
        }
    }
}