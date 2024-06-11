import static org.junit.Assert.*;
import org.junit.Test;

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
            multiDirectional.iterateSimplex(new Comparator<RealPointValuePair>() {
                public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                    return Double.compare(o1.getValue(), o2.getValue());
                }
            });
        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            fail("Exception thrown during iteration");
        }
    }

    @Test
    public void testEvaluateNewSimplex() {
        MultiDirectional multiDirectional = new MultiDirectional();
        RealPointValuePair[] original = { new RealPointValuePair(new double[]{1.0}, 1.0),
                                          new RealPointValuePair(new double[]{2.0}, 2.0) };
        try {
            RealPointValuePair result = multiDirectional.evaluateNewSimplex(original, 0.5, new Comparator<RealPointValuePair>() {
                public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                    return Double.compare(o1.getValue(), o2.getValue());
                }
            });
            assertNotNull(result);
        } catch (FunctionEvaluationException | OptimizationException e) {
            fail("Exception thrown while evaluating new simplex");
        }
    }

}