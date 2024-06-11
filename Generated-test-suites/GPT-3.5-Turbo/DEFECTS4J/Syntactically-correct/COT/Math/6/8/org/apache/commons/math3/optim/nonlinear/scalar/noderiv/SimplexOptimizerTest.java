
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new Simplex();
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        int maxEval = 1000;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = new OptimizationData[0];
        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType, optData);
        assertEquals(0.0, result.getPoint()[0], 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }

    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new Simplex();
        optimizer.setSimplex(simplex);
        double[] startPoint = new double[]{0.0};
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MINIMIZE);
        PointValuePair result = optimizer.doOptimize();
        assertEquals(0.0, result.getPoint()[0], 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }
}

