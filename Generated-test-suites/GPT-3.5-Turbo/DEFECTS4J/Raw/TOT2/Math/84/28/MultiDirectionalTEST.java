import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Comparator;

public class MultiDirectionalTest {

    @Test
    public void testDefaultConstructor() {
        MultiDirectional multiDirectional = new MultiDirectional();
        assertNotNull(multiDirectional);
        assertEquals(2.0, multiDirectional.getKhi(), 0.0);
        assertEquals(0.5, multiDirectional.getGamma(), 0.0);
    }

    @Test
    public void testCustomConstructor() {
        MultiDirectional multiDirectional = new MultiDirectional(3.0, 0.3);
        assertNotNull(multiDirectional);
        assertEquals(3.0, multiDirectional.getKhi(), 0.0);
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
        try {
            multiDirectional.iterateSimplex(comparator);
        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            fail("Exception thrown during simplex iteration");
        }
    }

    @Test
    public void testEvaluateNewSimplex() {
        MultiDirectional multiDirectional = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };
        RealPointValuePair[] original = {new RealPointValuePair(new double[] {1.0, 2.0}, 5.0), new RealPointValuePair(new double[] {2.0, 3.0}, 6.0)};
        try {
            RealPointValuePair best = multiDirectional.evaluateNewSimplex(original, 0.5, comparator);
            assertNotNull(best);
        } catch (FunctionEvaluationException | OptimizationException e) {
            fail("Exception thrown during evaluating new simplex");
        }
    }
}