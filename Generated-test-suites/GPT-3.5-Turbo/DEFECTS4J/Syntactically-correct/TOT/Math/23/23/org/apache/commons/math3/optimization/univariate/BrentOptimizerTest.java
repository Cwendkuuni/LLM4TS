import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test case 1: minimum value is at the middle of the interval
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8);
        double[] inputs1 = {1, 3, 5};
        double[] expected1 = {2};
        UnivariatePointValuePair result1 = optimizer.optimize(inputs1, GoalType.MINIMIZE);
        assertEquals(expected1[0], result1.getPoint(), 1e-8);

        // Test case 2: minimum value is at the boundaries of the interval
        double[] inputs2 = {-5, 0, 10};
        double[] expected2 = {0};
        UnivariatePointValuePair result2 = optimizer.optimize(inputs2, GoalType.MINIMIZE);
        assertEquals(expected2[0], result2.getPoint(), 1e-8);

        // Test case 3: maximum value is at the middle of the interval
        double[] inputs3 = {2, 4, 8};
        double[] expected3 = {6};
        UnivariatePointValuePair result3 = optimizer.optimize(inputs3, GoalType.MAXIMIZE);
        assertEquals(expected3[0], result3.getPoint(), 1e-8);

        // Test case 4: maximum value is at the boundaries of the interval
        double[] inputs4 = {-10, -5, -2};
        double[] expected4 = {-2};
        UnivariatePointValuePair result4 = optimizer.optimize(inputs4, GoalType.MAXIMIZE);
        assertEquals(expected4[0], result4.getPoint(), 1e-8);

        // Test case 5: function has multiple local minima
        double[] inputs5 = {1, 2, 3, 4, 5};
        double[] expected5 = {1};
        UnivariatePointValuePair result5 = optimizer.optimize(inputs5, GoalType.MINIMIZE);
        assertEquals(expected5[0], result5.getPoint(), 1e-8);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeAbs() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, -1e-8);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorSmallRel() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-18, 1e-8);
    }

    @Test
    public void testConvergenceChecker() {
        // Test case 1: custom convergence checker returning true
        ConvergenceChecker<UnivariatePointValuePair> checker1 = new ConvergenceChecker<UnivariatePointValuePair>() {
            public boolean converged(int iteration, UnivariatePointValuePair previousResult, UnivariatePointValuePair currentResult) {
                return true;
            }
        };
        BrentOptimizer optimizer1 = new BrentOptimizer(1e-8, 1e-8, checker1);
        double[] inputs1 = {1, 3, 5};
        double[] expected1 = {2};
        UnivariatePointValuePair result1 = optimizer1.optimize(inputs1, GoalType.MINIMIZE);
        assertEquals(expected1[0], result1.getPoint(), 1e-8);

        // Test case 2: custom convergence checker returning false
        ConvergenceChecker<UnivariatePointValuePair> checker2 = new ConvergenceChecker<UnivariatePointValuePair>() {
            public boolean converged(int iteration, UnivariatePointValuePair previousResult, UnivariatePointValuePair currentResult) {
                return false;
            }
        };
        BrentOptimizer optimizer2 = new BrentOptimizer(1e-8, 1e-8, checker2);
        double[] inputs2 = {1, 3, 5};
        double[] expected2 = {2};
        UnivariatePointValuePair result2 = optimizer2.optimize(inputs2, GoalType.MINIMIZE);
        assertEquals(expected2[0], result2.getPoint(), 1e-8);
    }

    @Test
    public void testBest() {
        // Test case 1: a has lower value than b
        double[] inputs1 = {1, 3, 5};
        double[] values1 = {2, 4};
        UnivariatePointValuePair a1 = new UnivariatePointValuePair(inputs1[0], values1[0]);
        UnivariatePointValuePair b1 = new UnivariatePointValuePair(inputs1[1], values1[1]);
        BrentOptimizer optimizer1 = new BrentOptimizer(1e-8, 1e-8);
        UnivariatePointValuePair result1 = optimizer1.best(a1, b1, true);
        assertEquals(a1.getPoint(), result1.getPoint(), 1e-8);

        // Test case 2: a has higher value than b
        double[] inputs2 = {1, 3, 5};
        double[] values2 = {2, 4};
        UnivariatePointValuePair a2 = new UnivariatePointValuePair(inputs2[0], values2[0]);
        UnivariatePointValuePair b2 = new UnivariatePointValuePair(inputs2[1], values2[1]);
        BrentOptimizer optimizer2 = new BrentOptimizer(1e-8, 1e-8);
        UnivariatePointValuePair result2 = optimizer2.best(a2, b2, false);
        assertEquals(a2.getPoint(), result2.getPoint(), 1e-8);
    }

}