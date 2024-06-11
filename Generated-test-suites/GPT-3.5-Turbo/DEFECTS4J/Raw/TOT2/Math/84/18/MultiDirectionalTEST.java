import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Comparator;

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
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        try {
            multiDirectional.iterateSimplex(comparator);
        } catch (Exception e) {
            fail("Exception thrown during iterateSimplex");
        }
    }

    @Test
    public void testEvaluateNewSimplex() {
        MultiDirectional multiDirectional = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        try {
            RealPointValuePair[] original = new RealPointValuePair[2];
            original[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 0.0);
            original[1] = new RealPointValuePair(new double[]{3.0, 4.0}, 0.0);
            double coeff = 0.5;
            RealPointValuePair result = multiDirectional.evaluateNewSimplex(original, coeff, comparator);
            assertNotNull(result);
        } catch (Exception e) {
            fail("Exception thrown during evaluateNewSimplex");
        }
    }

}