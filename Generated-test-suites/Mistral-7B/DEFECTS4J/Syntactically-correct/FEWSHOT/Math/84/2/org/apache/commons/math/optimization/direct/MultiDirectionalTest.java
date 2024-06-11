package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalDefaultConstructor() {
        MultiDirectional multiDirectional = new MultiDirectional();
        assertEquals(2.0, multiDirectional.khi, 0.001);
        assertEquals(0.5, multiDirectional.gamma, 0.001);
    }

    @Test
    public void testMultiDirectionalCustomConstructor() {
        MultiDirectional multiDirectional = new MultiDirectional(3.0, 0.7);
        assertEquals(3.0, multiDirectional.khi, 0.001);
        assertEquals(0.7, multiDirectional.gamma, 0.001);
    }

    @Test
    public void testIterateSimplex() throws Exception {
        MultiDirectional multiDirectional = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[] {
                new RealPointValuePair(new double[] {0.0, 0.0}, 1.0, false),
                new RealPointValuePair(new double[] {1.0, 1.0}, 2.0, false),
                new RealPointValuePair(new double[] {-1.0, -1.0}, 3.0, false),
                new RealPointValuePair(new double[] {1.0, -1.0}, 4.0, false),
                new RealPointValuePair(new double[] {-1.0, 1.0}, 5.0, false)
        };
        Comparator<RealPointValuePair> comparator = (p1, p2) -> Double.compare(p1.getValue(), p2.getValue());

        multiDirectional.iterateSimplex(comparator);

        // The order of the simplex may change after reflection or contraction
        PointValuePair best = null;
        for (RealPointValuePair point : multiDirectional.simplex) {
            if (best == null || point.getValue() < best.getValue()) {
                best = point;
            }
        }

        // Check that the best point is the one with the lowest function value
        assertEquals(0.0, best.getPointRef()[0], 0.001);
        assertEquals(0.0, best.getPointRef()[1], 0.001);
        assertEquals(1.0, best.getValue(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateSimplexInvalidComparator() {
        MultiDirectional multiDirectional = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = (p1, p2) -> p1.getValue().compareTo(p2.getPointRef()[0]);
        multiDirectional.iterateSimplex(comparator);
    }

}