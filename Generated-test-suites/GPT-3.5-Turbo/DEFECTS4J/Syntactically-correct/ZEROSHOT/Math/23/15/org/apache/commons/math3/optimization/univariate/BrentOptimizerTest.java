import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testConstructor1() {
        double rel = 0.01;
        double abs = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructor2() {
        double rel = 0.01;
        double abs = 0.001;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimize() {
        double rel = 0.01;
        double abs = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        // Set the minimum, maximum, and start values
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);

        // Set the goal type
        optimizer.setGoalType(GoalType.MINIMIZE);

        // Set the convergence checker
        optimizer.setConvergenceChecker(null);

        // Optimize the function
        UnivariatePointValuePair result = optimizer.optimize();

        // Check the result
        assertEquals(0.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBest() {
        boolean isMinim = true;

        UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 0.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 1.0);

        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertNotNull(result);
        assertEquals(a, result);
    }
}