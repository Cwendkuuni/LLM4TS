import static org.junit.Assert.*;
import org.junit.*;
import java.util.Comparator;

public class MultiDirectionalTest {

    @Test
    public void testConstructor() {
        MultiDirectional multiDirectional = new MultiDirectional();
        assertNotNull(multiDirectional);
        assertEquals(2.0, multiDirectional.getKhi(), 0.0);
        assertEquals(0.5, multiDirectional.getGamma(), 0.0);
    }

    @Test
    public void testConstructorWithCoefficients() {
        MultiDirectional multiDirectional = new MultiDirectional(1.5, 0.3);
        assertNotNull(multiDirectional);
        assertEquals(1.5, multiDirectional.getKhi(), 0.0);
        assertEquals(0.3, multiDirectional.getGamma(), 0.0);
    }

    @Test
    public void testIterateSimplex() {
        MultiDirectional multiDirectional = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };
        multiDirectional.iterateSimplex(comparator);
        // Additional assertions
    }

    @Test
    public void testEvaluateNewSimplex() {
        MultiDirectional multiDirectional = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };
        multiDirectional.evaluateNewSimplex(new RealPointValuePair[0], 1.0, comparator);
        // Additional assertions
    }

}