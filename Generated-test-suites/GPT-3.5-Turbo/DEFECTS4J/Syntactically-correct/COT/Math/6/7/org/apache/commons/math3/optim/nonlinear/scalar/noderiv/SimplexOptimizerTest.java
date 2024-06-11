import org.junit.*;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new NelderMeadSimplex(2);
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        SimpleValueChecker checker = new SimpleValueChecker(0, 0);
        optimizer.setConvergenceChecker(checker);
        int maxEval = 100;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = new OptimizationData[1];
        optData[0] = new InitialGuess(new double[]{1, 1});
        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType, optData);
        assertEquals(0.0, result.getValue(), 0.0);
        double[] expectedPoint = new double[]{0.0, 0.0};
        assertArrayEquals(expectedPoint, result.getPoint(), 0.0);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_NullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new NelderMeadSimplex(2);
        simplex.build(new double[]{1, 1});
        optimizer.setSimplex(simplex);
        optimizer.setStartPoint(new double[]{1, 1});
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.doOptimize();
        assertEquals(2.0, result.getValue(), 0.0);
        double[] expectedPoint = new double[]{1.0, 1.0};
        assertArrayEquals(expectedPoint, result.getPoint(), 0.0);    
    }

}