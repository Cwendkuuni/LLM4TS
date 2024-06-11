package org.apache.commons.cli.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.ValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.junit.Test;

public class SimplexOptimizerTest {

    private AbstractSimplex simplex = mock(AbstractSimplex.class);
    private MultivariateFunction function = new MultivariateFunction() {
        @Override
        public double value(double[] x) {
            return FunctionUtils.pow(x[0], 2) + FunctionUtils.pow(x[1], 2);
        }
    };

    private SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));

    @Test(expected = NullArgumentException.class)
    public void testNullAbstractSimplex() {
        optimizer.setSimplex(null);
        optimizer.optimize(1, function, GoalType.MINIMIZE, new Object[0]);
    }

    @Test
    public void testOptimize() throws MaxEvalException {
        when(simplex.getSize()).thenReturn(2);
        when(simplex.getPoint(0)).thenReturn(new double[]{1.0, 1.0});
        when(simplex.getPoint(1)).thenReturn(new double[]{1.5, 0.5});
        when(simplex.getPoint(2)).thenReturn(new double[]{0.5, 1.5});

        double[] startPoint = new double[]{2.0, 2.0};
        PointValuePair result = optimizer.optimize(10, function, GoalType.MINIMIZE, new Object[]{startPoint, simplex});

        PointValuePair expectedResult = new PointValuePair(new double[]{1.0, 1.0}, 3.0);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testOptimizeWithMaxEvalReached() throws MaxEvalException {
        when(simplex.getSize()).thenReturn(2);
        when(simplex.getPoint(0)).thenReturn(new double[]{1.0, 1.0});
        when(simplex.getPoint(1)).thenReturn(new double[]{1.5, 0.5});
        when(simplex.getPoint(2)).thenReturn(new double[]{0.5, 1.5});

        double[] startPoint = new double[]{0.0, 0.0};
        PointValuePair result = optimizer.optimize(1, function, GoalType.MINIMIZE, new Object[]{startPoint, simplex});

        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSimplexAndOptimizeWithWrongArguments() {
        optimizer.setSimplex("not an AbstractSimplex");
        optimizer.optimize(1, function, GoalType.MINIMIZE, new Object[0]);
    }
}