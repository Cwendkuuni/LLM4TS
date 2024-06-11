package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithConvergenceChecker() {
        // Test constructor with Convergence Checker
        ConvergenceChecker<UnivariatePointValuePair> checker = new UnivariatePointValuePairChecker();
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001, checker);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithoutConvergenceChecker() {
        // Test constructor without Convergence Checker
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        assertNotNull(optimizer);
    }

    @Test
    public void testBest() {
        // Test best method
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertEquals(result, b);
    }

    @Test
    public void testDoOptimize() {
        // Test doOptimize method
        ConvergenceChecker<UnivariatePointValuePair> checker = new UnivariatePointValuePairChecker();
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001, checker);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        UnivariatePointValuePair result = optimizer.optimize(100, null);
        assertNotNull(result);
    }

    // Add more test cases for other public methods

}