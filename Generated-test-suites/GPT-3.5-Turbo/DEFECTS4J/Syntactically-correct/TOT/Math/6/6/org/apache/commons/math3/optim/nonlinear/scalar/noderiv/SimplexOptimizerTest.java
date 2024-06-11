import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Assert;
import org.junit.Test;

public class SimplexOptimizerTest {

    private static final double EPSILON = 1e-6;

    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();

        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1]; // Minimize x^2 + y^2
            }
        };
        
        double[] startPoint = {0, 0};
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.optimizeInternal(100, function, goalType, new InitialGuess(startPoint));

        Assert.assertEquals(0, result.getPoint()[0], EPSILON);
        Assert.assertEquals(0, result.getPoint()[1], EPSILON);
        Assert.assertEquals(0, result.getValue(), EPSILON);
    }

    @Test
    public void testOptimizeInternalWithMaxEvaluations() {
        SimplexOptimizer optimizer = new SimplexOptimizer();

        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1]; // Minimize x^2 + y^2
            }
        };
        
        double[] startPoint = {0, 0};
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.optimizeInternal(10, function, goalType, new InitialGuess(startPoint));

        Assert.assertEquals(0, result.getPoint()[0], EPSILON);
        Assert.assertEquals(0, result.getPoint()[1], EPSILON);
        Assert.assertEquals(0, result.getValue(), EPSILON);
    }

    @Test
    public void testOptimizeInternalWithConvergenceChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer();

        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1]; // Minimize x^2 + y^2
            }
        };
        
        double[] startPoint = {0, 0};
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.optimizeInternal(100, function, goalType, new InitialGuess(startPoint), new SimpleValueChecker(1e-4, 1e-4));

        Assert.assertEquals(0, result.getPoint()[0], EPSILON);
        Assert.assertEquals(0, result.getPoint()[1], EPSILON);
        Assert.assertEquals(0, result.getValue(), EPSILON);
    }

    @Test
    public void testOptimizeInternalWithDifferentStartPoint() {
        SimplexOptimizer optimizer = new SimplexOptimizer();

        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1]; // Minimize x^2 + y^2
            }
        };
        
        double[] startPoint = {1, 1};
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.optimizeInternal(100, function, goalType, new InitialGuess(startPoint));

        Assert.assertEquals(0, result.getPoint()[0], EPSILON);
        Assert.assertEquals(0, result.getPoint()[1], EPSILON);
        Assert.assertEquals(0, result.getValue(), EPSILON);
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimizeInternalWithNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();

        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1]; // Minimize x^2 + y^2
            }
        };
        
        double[] startPoint = {0, 0};
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.optimizeInternal(100, function, goalType);

        Assert.assertEquals(0, result.getPoint()[0], EPSILON);
        Assert.assertEquals(0, result.getPoint()[1], EPSILON);
        Assert.assertEquals(0, result.getValue(), EPSILON);
    }

    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();

        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1]; // Minimize x^2 + y^2
            }
        };
        
        double[] startPoint = {0, 0};
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.doOptimize(100, function, startPoint, goalType);

        Assert.assertEquals(0, result.getPoint()[0], EPSILON);
        Assert.assertEquals(0, result.getPoint()[1], EPSILON);
        Assert.assertEquals(0, result.getValue(), EPSILON);
    }

    @Test
    public void testDoOptimizeWithMaxEvaluations() {
        SimplexOptimizer optimizer = new SimplexOptimizer();

        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1]; // Minimize x^2 + y^2
            }
        };
        
        double[] startPoint = {0, 0};
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.doOptimize(10, function, startPoint, goalType);

        Assert.assertEquals(0, result.getPoint()[0], EPSILON);
        Assert.assertEquals(0, result.getPoint()[1], EPSILON);
        Assert.assertEquals(0, result.getValue(), EPSILON);
    }

    @Test
    public void testDoOptimizeWithDifferentStartPoint() {
        SimplexOptimizer optimizer = new SimplexOptimizer();

        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1]; // Minimize x^2 + y^2
            }
        };
        
        double[] startPoint = {1, 1};
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.doOptimize(100, function, startPoint, goalType);

        Assert.assertEquals(0, result.getPoint()[0], EPSILON);
        Assert.assertEquals(0, result.getPoint()[1], EPSILON);
        Assert.assertEquals(0, result.getValue(), EPSILON);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeWithNullStartPoint() {
        SimplexOptimizer optimizer = new SimplexOptimizer();

        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1]; // Minimize x^2 + y^2
            }
        };
        
        double[] startPoint = null;
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.doOptimize(100, function, startPoint, goalType);

        Assert.assertEquals(0, result.getPoint()[0], EPSILON);
        Assert.assertEquals(0, result.getPoint()[1], EPSILON);
        Assert.assertEquals(0, result.getValue(), EPSILON);
    }

}