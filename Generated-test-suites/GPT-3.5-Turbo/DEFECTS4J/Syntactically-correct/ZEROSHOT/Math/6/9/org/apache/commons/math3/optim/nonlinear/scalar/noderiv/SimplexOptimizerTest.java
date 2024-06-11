import org.junit.*;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex();
        optimizer.setSimplex(simplex);

        assertSame(simplex, optimizer.simplex);
    }

    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        int maxEval = 1000;
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = new OptimizationData[1];
        optData[0] = new AbstractSimplex();

        PointValuePair result = optimizer.optimizeInternal(maxEval, function, goalType, optData);

        assertNotNull(result);
    }

    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex();
        optimizer.simplex = simplex;
        double[] startPoint = new double[]{1.0, 2.0};
        optimizer.setStartPoint(startPoint);

        PointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
    }
}