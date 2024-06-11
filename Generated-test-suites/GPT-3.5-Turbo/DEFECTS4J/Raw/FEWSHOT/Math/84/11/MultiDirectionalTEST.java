import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MultiDirectionalTest {
    @Test
    public void testIterateSimplex() {
        MultiDirectional multiDirectional = new MultiDirectional();
        
        // Test case 1
        RealPointValuePair[] original1 = new RealPointValuePair[3];
        original1[0] = new RealPointValuePair(new double[] {1.0, 2.0, 3.0}, 0.0);
        original1[1] = new RealPointValuePair(new double[] {2.0, 3.0, 1.0}, 0.0);
        original1[2] = new RealPointValuePair(new double[] {3.0, 1.0, 2.0}, 0.0);
        multiDirectional.simplex = original1;
        Comparator<RealPointValuePair> comparator1 = (o1, o2) -> {
            return Double.compare(o1.getValue(), o2.getValue());
        };
        multiDirectional.iterateSimplex(comparator1);
        assertEquals(1.0, multiDirectional.simplex[0].getPointRef()[0], 0.000001);
        assertEquals(1.999999, multiDirectional.simplex[0].getPointRef()[1], 0.000001);
        assertEquals(2.999999, multiDirectional.simplex[0].getPointRef()[2], 0.000001);
        
        // Test case 2
        RealPointValuePair[] original2 = new RealPointValuePair[3];
        original2[0] = new RealPointValuePair(new double[] {2.0, 2.0, 2.0}, 0.0);
        original2[1] = new RealPointValuePair(new double[] {3.0, 3.0, 3.0}, 0.0);
        original2[2] = new RealPointValuePair(new double[] {1.0, 1.0, 1.0}, 0.0);
        multiDirectional.simplex = original2;
        Comparator<RealPointValuePair> comparator2 = (o1, o2) -> {
            return Double.compare(o1.getValue(), o2.getValue());
        };
        multiDirectional.iterateSimplex(comparator2);
        assertEquals(1.999999, multiDirectional.simplex[0].getPointRef()[0], 0.000001);
        assertEquals(1.999999, multiDirectional.simplex[0].getPointRef()[1], 0.000001);
        assertEquals(1.999999, multiDirectional.simplex[0].getPointRef()[2], 0.000001);
    }
}