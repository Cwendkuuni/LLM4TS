import static org.junit.Assert.*;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testConstructorWithDefaultCoefficients() {
        MultiDirectional multiDirectional = new MultiDirectional();
        assertNotNull(multiDirectional);
        assertEquals(2.0, multiDirectional.khi, 0.0001);
        assertEquals(0.5, multiDirectional.gamma, 0.0001);
    }

    @Test
    public void testConstructorWithSpecifiedCoefficients() {
        MultiDirectional multiDirectional = new MultiDirectional(1.5, 0.3);
        assertNotNull(multiDirectional);
        assertEquals(1.5, multiDirectional.khi, 0.0001);
        assertEquals(0.3, multiDirectional.gamma, 0.0001);
    }

    @Test
    public void testIterateSimplexWithReflection() {
        MultiDirectional multiDirectional = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        RealPointValuePair[] original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 0.0);
        original[1] = new RealPointValuePair(new double[]{2.0, 3.0}, 1.0);
        original[2] = new RealPointValuePair(new double[]{3.0, 4.0}, -1.0);
        multiDirectional.simplex = original;
        multiDirectional.iterateSimplex(comparator);
        assertEquals(1.5, multiDirectional.simplex[0].getPointRef()[0], 0.0001);
        assertEquals(2.5, multiDirectional.simplex[0].getPointRef()[1], 0.0001);
    }

    @Test
    public void testEvaluateNewSimplex() {
        MultiDirectional multiDirectional = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        RealPointValuePair[] original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 0.0);
        original[1] = new RealPointValuePair(new double[]{2.0, 3.0}, 1.0);
        original[2] = new RealPointValuePair(new double[]{3.0, 4.0}, -1.0);
        multiDirectional.simplex = original;
        try {
            multiDirectional.evaluateNewSimplex(original, 0.5, comparator);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        assertEquals(2.0, multiDirectional.simplex[0].getPointRef()[0], 0.0001);
        assertEquals(3.0, multiDirectional.simplex[0].getPointRef()[1], 0.0001);
    }

}