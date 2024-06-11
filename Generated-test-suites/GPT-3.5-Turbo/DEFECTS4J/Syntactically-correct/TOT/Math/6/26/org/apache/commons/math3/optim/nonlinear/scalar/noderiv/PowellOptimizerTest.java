import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class PowellOptimizerTest {

    private PowellOptimizer powellOptimizer;

    @Before
    public void setup() {
        powellOptimizer = new PowellOptimizer(1e-10, 1e-10);
    }

    @Test
    public void testDoOptimize_MinimizeGoal() {
        double[] startPoint = {1.0, 2.0, 3.0};
        double[] expectedPoint = {0.0, 0.0, 0.0};
        PointValuePair result = powellOptimizer.optimize(new ObjectiveFunction(), GoalType.MINIMIZE, startPoint);
        
        Assert.assertEquals(expectedPoint[0], result.getPoint()[0], 1e-10);
        Assert.assertEquals(expectedPoint[1], result.getPoint()[1], 1e-10);
        Assert.assertEquals(expectedPoint[2], result.getPoint()[2], 1e-10);
    }

    @Test
    public void testDoOptimize_MaximizeGoal() {
        double[] startPoint = {1.0, 2.0, 3.0};
        double[] expectedPoint = {0.0, 0.0, 0.0};
        PointValuePair result = powellOptimizer.optimize(new ObjectiveFunction(), GoalType.MAXIMIZE, startPoint);
        
        Assert.assertEquals(expectedPoint[0], result.getPoint()[0], 1e-10);
        Assert.assertEquals(expectedPoint[1], result.getPoint()[1], 1e-10);
        Assert.assertEquals(expectedPoint[2], result.getPoint()[2], 1e-10);
    }

    @Test
    public void testDoOptimize_CustomConvergenceChecker() {
        double[] startPoint = {1.0, 2.0, 3.0};
        double[] expectedPoint = {0.0, 0.0, 0.0};
        ConvergenceChecker<PointValuePair> customChecker = new CustomConvergenceChecker();
        powellOptimizer.setConvergenceChecker(customChecker);
        PointValuePair result = powellOptimizer.optimize(new ObjectiveFunction(), GoalType.MINIMIZE, startPoint);
        
        Assert.assertEquals(expectedPoint[0], result.getPoint()[0], 1e-10);
        Assert.assertEquals(expectedPoint[1], result.getPoint()[1], 1e-10);
        Assert.assertEquals(expectedPoint[2], result.getPoint()[2], 1e-10);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_RelativeThresholdTooSmall() {
        powellOptimizer = new PowellOptimizer(1e-20, 1e-10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_AbsoluteThresholdNotStrictlyPositive() {
        powellOptimizer = new PowellOptimizer(1e-10, -1.0);
    }

    private class ObjectiveFunction implements MultivariateFunction {
        public double value(double[] point) {
            double sum = 0.0;
            for (double x : point) {
                sum += x*x;
            }
            return sum;
        }
    }

    private class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            double[] prevPoint = previous.getPoint();
            double[] currPoint = current.getPoint();
            
            double distance = 0.0;
            for (int i = 0; i < prevPoint.length; i++) {
                distance += Math.abs(prevPoint[i] - currPoint[i]);
            }
            
            return distance < 1e-5;
        }
    }
}