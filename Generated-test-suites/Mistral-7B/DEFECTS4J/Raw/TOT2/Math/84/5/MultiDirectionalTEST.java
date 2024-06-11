import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleGoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Amoeba;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalOptimizer() throws Exception {
        MultiDirectional optimizer = new MultiDirectional(2.0, 0.5);
        PointValuePair bestPoint = new PointValuePair(new double[]{-1.2, 1.0}, 2.5);
        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) + Math.cos(x[1]), 2.0);
            }

            @Override
            public GoalType getGoalType() {
                return SimpleGoalType.MINIMIZE;
            }
        };

        optimizer.optimize(function);
        PointValuePair result = optimizer.getBestSolution();
        assertEquals(bestPoint.getPoint(), result.getPoint(), 0.01);
        assertEquals(bestPoint.getValue(), result.getValue(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullComparisonFunction() {
        MultiDirectional optimizer = new MultiDirectional(2.0, 0.5);
        optimizer.iterateSimplex(null);
    }

    @Test(expected = OptimizationException.class)
    public void testMaxEvaluationsReached() throws Exception {
        MultiDirectional optimizer = new MultiDirectional(2.0, 0.5);
        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.sin(Math.PI * x[0]) * Math.cos(Math.PI * x[1]);
            }

            @Override
            public GoalType getGoalType() {
                return SimpleGoalType.MINIMIZE;
            }
        };
        optimizer.setMaxIterations(1);
        optimizer.optimize(function);
    }
}