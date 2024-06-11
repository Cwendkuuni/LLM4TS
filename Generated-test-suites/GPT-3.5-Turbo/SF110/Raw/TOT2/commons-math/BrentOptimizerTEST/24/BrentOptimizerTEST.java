package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new SimplePointChecker<UnivariatePointValuePair>(1e-10, 1e-10);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, checker);
        
        assertNotNull(optimizer);
        assertEquals(1e-6, optimizer.getRelativeThreshold(), 1e-15);
        assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 1e-15);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testConstructorWithoutConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        
        assertNotNull(optimizer);
        assertEquals(1e-6, optimizer.getRelativeThreshold(), 1e-15);
        assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 1e-15);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.setMax(10.0);
        optimizer.setMin(-10.0);
        optimizer.setStartValue(0.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertNotNull(result);
        assertEquals(1.0, result.getPoint(), 1e-10);
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair pointA = new UnivariatePointValuePair(0.0, 1.0);
        UnivariatePointValuePair pointB = new UnivariatePointValuePair(1.0, 0.0);
        
        UnivariatePointValuePair result = optimizer.best(pointA, pointB, true);
        
        assertNotNull(result);
        assertEquals(1.0, result.getPoint(), 1e-15);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelTolerance() {
        new BrentOptimizer(1e-16, 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsTolerance() {
        new BrentOptimizer(1e-6, 0.0);
    }
}