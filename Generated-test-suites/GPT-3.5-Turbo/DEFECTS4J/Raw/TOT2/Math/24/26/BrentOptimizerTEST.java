import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.optimize(100, x -> x * x - 4, GoalType.MINIMIZE, 0, 4, 2);

        assertEquals(2.0, result.getPoint(), 1e-5);
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        UnivariatePointValuePair pointA = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair pointB = new UnivariatePointValuePair(2.0, 5.0);

        UnivariatePointValuePair bestPoint = optimizer.best(pointA, pointB, true);

        assertEquals(2.0, bestPoint.getPoint(), 1e-5);
        assertEquals(5.0, bestPoint.getValue(), 1e-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidParameters() {
        new BrentOptimizer(1e-10, -1e-10);
    }
}