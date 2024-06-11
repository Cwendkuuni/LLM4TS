import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectional() {
        MultiDirectional multiDirectional = new MultiDirectional();
        assertNotNull(multiDirectional);
    }

    @Test
    public void testMultiDirectionalWithCoefficients() {
        MultiDirectional multiDirectional = new MultiDirectional(2.0, 0.5);
        assertNotNull(multiDirectional);
    }

    @Test
    public void testIterateSimplex() {
        MultiDirectional multiDirectional = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        try {
            multiDirectional.iterateSimplex(comparator);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testEvaluateNewSimplex() {
        MultiDirectional multiDirectional = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[2];
        original[0] = new RealPointValuePair(new double[]{0.0, 0.0}, 0.0, false);
        original[1] = new RealPointValuePair(new double[]{1.0, 1.0}, 1.0, false);
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        try {
            multiDirectional.evaluateNewSimplex(original, 1.0, comparator);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}