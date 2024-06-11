package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.function.Exp;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.junit.Before;
import org.junit.Test;

public class SimplexOptimizerTest {
    private SimplexOptimizer optimizer;

    @Before
    public void setup() {
        optimizer = new SimplexOptimizer();
    }

    @Test
    public void testSetSimplex() {
        AbstractSimplex simplex = mock(AbstractSimplex.class);
        optimizer.setSimplex(simplex);

        assertEquals(simplex, optimizer.simplex);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetSimplexNull() {
        optimizer.setSimplex(null);
    }

    @Test
    public void testOptimizeInternal() {
        double[] startPoint = new double[] {1.0, 2.0, 3.0};
        double[] point = new double[] {2.0, 3.0, 4.0};
        double value = 5.0;

        AbstractSimplex simplex = mock(AbstractSimplex.class);
        when(simplex.getPoint(0)).thenReturn(new PointValuePair(startPoint, 0.0));
        when(simplex.getPoint(1)).thenReturn(new PointValuePair(point, value));

        MultivariateFunction f = mock(MultivariateFunction.class);
        when(f.value(point)).thenReturn(value);

        GoalType goalType = GoalType.MINIMIZE;

        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();

        PointValuePair result = optimizer.optimizeInternal(100, f, goalType, simplex, checker);
        
        assertEquals(new PointValuePair(point, value), result);
    }

    @Test
    public void testDoOptimize() {
        double[] startPoint = new double[] {1.0, 2.0, 3.0};
        double[] point = new double[] {2.0, 3.0, 4.0};
        double value = 5.0;

        AbstractSimplex simplex = mock(AbstractSimplex.class);
        when(simplex.getPoint(0)).thenReturn(new PointValuePair(startPoint, 0.0));
        when(simplex.getPoint(1)).thenReturn(new PointValuePair(point, value));
        when(simplex.getSize()).thenReturn(2);

        MultivariateFunction f = mock(MultivariateFunction.class);
        when(f.value(point)).thenReturn(value);

        GoalType goalType = GoalType.MINIMIZE;

        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();

        optimizer.simplex = simplex;

        PointValuePair result = optimizer.doOptimize();
        
        assertEquals(new PointValuePair(point, value), result);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullSimplex() {
        optimizer.doOptimize();
    }
}