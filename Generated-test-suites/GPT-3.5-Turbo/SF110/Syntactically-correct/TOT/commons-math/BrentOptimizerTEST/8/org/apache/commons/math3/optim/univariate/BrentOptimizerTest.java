package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize_minimization() {
        // Typical use case - minimization
        double rel = 1e-10;
        double abs = 1e-12;
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10);
        assertEquals("Incorrect optimal value", 0.0, result.getPoint(), abs);
        assertEquals("Incorrect optimal value", 0.0, result.getValue(), abs);
    }
    
    @Test
    public void testDoOptimize_maximization() {
        // Typical use case - maximization
        double rel = 1e-10;
        double abs = 1e-12;
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return -x * x;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, -10, 10);
        assertEquals("Incorrect optimal value", 0.0, result.getPoint(), abs);
        assertEquals("Incorrect optimal value", 0.0, result.getValue(), abs);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_relativeToleranceTooSmall() {
        // Error scenario - relative tolerance too small
        double rel = -1e-10;
        double abs = 1e-12;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_absoluteToleranceNotPositive() {
        // Error scenario - absolute tolerance not positive
        double rel = 1e-10;
        double abs = 0.0;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }
    
    @Test
    public void testConstructor_withConvergenceChecker() {
        // Typical use case - with convergence checker
        double rel = 1e-10;
        double abs = 1e-12;
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return iteration > 10;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        assertNotNull("Convergence checker should not be null", optimizer.getConvergenceChecker());
    }

}