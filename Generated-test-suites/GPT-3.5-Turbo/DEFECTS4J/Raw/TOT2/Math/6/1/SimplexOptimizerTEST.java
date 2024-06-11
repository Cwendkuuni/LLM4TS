package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testSimplexOptimizerConstruction() {
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return false;
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        assertEquals("Convergence checker should be set", checker, optimizer.getConvergenceChecker());
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimizeInternal(100, new MultivariateFunction() {
            public double value(double[] point) {
                return 0;
            }
        }, GoalType.MINIMIZE);
    }

    @Test
    public void testOptimizeInternal() {
        AbstractSimplex mockedSimplex = mock(AbstractSimplex.class);
        SimplexOptimizer optimizer = new SimplexOptimizer();

        optimizer.setSimplex(mockedSimplex);

        // Mock the objective function
        MultivariateFunction objectiveFunction = mock(MultivariateFunction.class);
        when(objectiveFunction.value(new double[]{1.0})).thenReturn(5.0);

        PointValuePair result = optimizer.optimizeInternal(100, objectiveFunction, GoalType.MINIMIZE);

        assertEquals(1.0, result.getPoint()[0], 0.0001);
        assertEquals(5.0, result.getValue(), 0.0001);
    }

    @Test
    public void testOptimizeInternalMaxIterationsReached() {
        AbstractSimplex mockedSimplex = mock(AbstractSimplex.class);
        SimplexOptimizer optimizer = new SimplexOptimizer();

        optimizer.setSimplex(mockedSimplex);

        // Mock the objective function
        MultivariateFunction objectiveFunction = mock(MultivariateFunction.class);
        when(objectiveFunction.value(new double[]{1.0})).thenReturn(5.0);

        PointValuePair result = optimizer.optimizeInternal(1, objectiveFunction, GoalType.MINIMIZE);

        assertEquals(1.0, result.getPoint()[0], 0.0001);
        assertEquals(5.0, result.getValue(), 0.0001);
    }
}