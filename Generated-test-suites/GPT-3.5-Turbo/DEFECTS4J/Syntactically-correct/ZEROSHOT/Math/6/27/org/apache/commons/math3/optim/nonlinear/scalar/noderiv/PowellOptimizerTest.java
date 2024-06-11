import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize() {
        MultivariateFunction objectiveFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-10, 1e-10);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setConvergenceChecker(new ConvergenceChecker<PointValuePair>() {
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return (previous.getValue() - current.getValue()) < 1e-10;
            }
        });

        double[] startPoint = {1.0, 1.0};
        PointValuePair result = optimizer.optimize(objectiveFunction, startPoint);

        Assert.assertEquals(0.0, result.getValue(), 1e-10);
        Assert.assertEquals(0.0, result.getPoint()[0], 1e-10);
        Assert.assertEquals(0.0, result.getPoint()[1], 1e-10);
    }

}