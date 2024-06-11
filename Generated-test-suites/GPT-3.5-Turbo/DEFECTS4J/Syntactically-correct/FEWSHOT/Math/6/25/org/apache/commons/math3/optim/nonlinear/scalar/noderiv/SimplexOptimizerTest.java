import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.direct.AbstractSimplex;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;
import org.junit.Before;
import org.junit.Test;

public class SimplexOptimizerTest {
    private SimplexOptimizer optimizer;
    private MultivariateFunction function;
    private GoalType goalType;
    private AbstractSimplex simplex;

    @Before
    public void setup() {
        optimizer = new SimplexOptimizer();
        function = mock(MultivariateFunction.class);
        goalType = GoalType.MINIMIZE;
        simplex = mock(AbstractSimplex.class);
    }

    @Test
    public void testOptimizeInternal() {
        // Mock optimization data
        OptimizationData[] optData = {simplex};

        // Mock optimizeInternal
        PointValuePair result = new PointValuePair(new double[]{1.0, 2.0}, 3.0);
        when(optimizer.optimizeInternal(100, function, goalType, optData)).thenReturn(result);

        // Test optimizeInternal
        PointValuePair actualResult = optimizer.optimizeInternal(100, function, goalType, optData);
        assertEquals(result, actualResult);
    }
    
    @Test
    public void testParseOptimizationData() {
        // Mock optimization data
        OptimizationData[] optData = {simplex};

        // Test parseOptimizationData
        optimizer.parseOptimizationData(optData);

        // Check if simplex is set correctly
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_NullSimplex() {
        // Test doOptimize with null simplex
        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimize_Converged() {
        // Mock simplex
        when(simplex.getSize()).thenReturn(2);
        PointValuePair[] points = {new PointValuePair(new double[]{1.0, 2.0}, 3.0), new PointValuePair(new double[]{4.0, 5.0}, 6.0)};
        when(simplex.getPoints()).thenReturn(points);
        when(simplex.getPoint(0)).thenReturn(points[0]);
        when(simplex.getPoint(1)).thenReturn(points[1]);
        when(simplex.evaluate(function, optimizer.getComparator())).thenReturn(points);

        // Test doOptimize with converged points
        PointValuePair result = optimizer.doOptimize();
        assertEquals(points[0], result);
    }

    @Test
    public void testDoOptimize_NotConverged() {
        // Mock simplex
        when(simplex.getSize()).thenReturn(2);
        PointValuePair[] previous = {new PointValuePair(new double[]{5.0, 6.0}, 7.0), new PointValuePair(new double[]{8.0, 9.0}, 10.0)};
        PointValuePair[] current = {new PointValuePair(new double[]{1.0, 2.0}, 3.0), new PointValuePair(new double[]{4.0, 5.0}, 6.0)};
        when(simplex.getPoints()).thenReturn(current);
        when(simplex.getPoint(0)).thenReturn(current[0]);
        when(simplex.getPoint(1)).thenReturn(current[1]);
        when(simplex.evaluate(function, optimizer.getComparator())).thenReturn(current);

        // Mock convergence checker
        optimizer.setConvergenceChecker((iteration, previousOptimal, currentOptimal) -> false);

        // Test doOptimize with not converged points
        PointValuePair result = optimizer.doOptimize();
        assertNull(result);
    }
}