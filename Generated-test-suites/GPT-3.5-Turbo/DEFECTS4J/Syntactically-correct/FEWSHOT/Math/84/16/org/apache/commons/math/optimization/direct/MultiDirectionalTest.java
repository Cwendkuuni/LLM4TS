import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {
    private DirectSearchOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
    }

    @Test
    public void testIterateSimplex() throws Exception {
        RealPointValuePair[] simplex = new RealPointValuePair[5];
        for (int i = 0; i < simplex.length; i++) {
            double[] point = new double[] { i, i + 1, i + 2 };
            simplex[i] = new RealPointValuePair(point, i);
        }

        Comparator<RealPointValuePair> comparator = mock(Comparator.class);
        when(comparator.compare(simplex[3], simplex[0])).thenReturn(-1);
        when(comparator.compare(simplex[2], simplex[3])).thenReturn(1);
        when(comparator.compare(simplex[1], simplex[0])).thenReturn(-1);
        when(comparator.compare(simplex[4], simplex[0])).thenReturn(1);

        optimizer.setSimplex(simplex);
        optimizer.iterateSimplex(comparator);

        RealPointValuePair[] newSimplex = optimizer.getSimplex();
        assertEquals(simplex[3], newSimplex[0]);
        assertEquals(simplex[0], newSimplex[1]);
        assertEquals(simplex[4], newSimplex[2]);
        assertEquals(simplex[1], newSimplex[3]);
        assertEquals(simplex[2], newSimplex[4]);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testEvaluateNewSimplex() throws Exception {
        Comparator<RealPointValuePair> comparator = mock(Comparator.class);
        when(comparator.compare(new RealPointValuePair(new double[] { 1, 2, 3 }, 4), new RealPointValuePair(new double[] { 5, 6, 7 }, 8))).thenReturn(-1);
        when(comparator.compare(new RealPointValuePair(new double[] { 9, 10, 11 }, 12), new RealPointValuePair(new double[] { 13, 14, 15 }, 16))).thenReturn(1);

        optimizer.setSimplex(new RealPointValuePair[] { new RealPointValuePair(new double[] { 1, 2, 3 }, 4),
                new RealPointValuePair(new double[] { 5, 6, 7 }, 8) });

        RealPointValuePair best = optimizer.evaluateNewSimplex(optimizer.getSimplex(), 0.5, comparator);

        assertEquals(new RealPointValuePair(new double[] { 3, 4, 5 }, 6), best);
    }
}