package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithChecker() {
        // Arrange
        double rel = 0.001;
        double abs = 0.01;
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return false;
            }
        };

        // Act
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);

        // Assert
        assertNotNull(optimizer);
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testConstructorWithoutChecker() {
        // Arrange
        double rel = 0.001;
        double abs = 0.01;

        // Act
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        // Assert
        assertNotNull(optimizer);
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithRelOutOfLowerBound() {
        // Arrange
        double rel = -0.001;
        double abs = 0.01;

        // Act
        new BrentOptimizer(rel, abs);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithAbsZero() {
        // Arrange
        double rel = 0.001;
        double abs = 0.0;

        // Act
        new BrentOptimizer(rel, abs);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithAbsNegative() {
        // Arrange
        double rel = 0.001;
        double abs = -0.01;

        // Act
        new BrentOptimizer(rel, abs);
    }

    @Test
    public void testDoOptimize_minimization() {
        // Arrange
        double rel = 0.001;
        double abs = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        optimizer.setGoalType(GoalType.MINIMIZE);
        double min = 1.0;
        double start = 2.0;
        double max = 4.0;
        optimizer.setMin(min);
        optimizer.setStartValue(start);
        optimizer.setMax(max);
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return iteration >= 10;
            }
        };
        optimizer.setConvergenceChecker(checker);

        // Act
        UnivariatePointValuePair result = optimizer.doOptimize();

        // Assert
        assertNotNull(result);
        assertTrue(result.getPoint() >= min);
        assertTrue(result.getPoint() <= max);
    }

    @Test
    public void testDoOptimize_maximization() {
        // Arrange
        double rel = 0.001;
        double abs = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        double min = 1.0;
        double start = 2.0;
        double max = 4.0;
        optimizer.setMin(min);
        optimizer.setStartValue(start);
        optimizer.setMax(max);
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return iteration >= 10;
            }
        };
        optimizer.setConvergenceChecker(checker);

        // Act
        UnivariatePointValuePair result = optimizer.doOptimize();

        // Assert
        assertNotNull(result);
        assertTrue(result.getPoint() >= min);
        assertTrue(result.getPoint() <= max);
    }

    @Test
    public void testDoOptimize_withConvergenceChecker() {
        // Arrange
        double rel = 0.001;
        double abs = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        optimizer.setGoalType(GoalType.MINIMIZE);
        double min = 1.0;
        double start = 2.0;
        double max = 4.0;
        optimizer.setMin(min);
        optimizer.setStartValue(start);
        optimizer.setMax(max);
        int maxIterations = 5;
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return iteration >= maxIterations;
            }
        };
        optimizer.setConvergenceChecker(checker);

        // Act
        UnivariatePointValuePair result = optimizer.doOptimize();

        // Assert
        assertNotNull(result);
        assertTrue(result.getPoint() >= min);
        assertTrue(result.getPoint() <= max);
    }

    @Test
    public void testBest_minimization() {
        // Arrange
        double a = 1.0;
        double b = 2.0;
        boolean isMinim = true;
        UnivariatePointValuePair aValuePair = new UnivariatePointValuePair(a, 0.0);
        UnivariatePointValuePair bValuePair = new UnivariatePointValuePair(b, 0.0);

        // Act
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
        UnivariatePointValuePair result = optimizer.best(aValuePair, bValuePair, isMinim);

        // Assert
        assertEquals(aValuePair, result);
    }

    @Test
    public void testBest_maximization() {
        // Arrange
        double a = 1.0;
        double b = 2.0;
        boolean isMinim = false;
        UnivariatePointValuePair aValuePair = new UnivariatePointValuePair(a, 0.0);
        UnivariatePointValuePair bValuePair = new UnivariatePointValuePair(b, 0.0);

        // Act
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
        UnivariatePointValuePair result = optimizer.best(aValuePair, bValuePair, isMinim);

        // Assert
        assertEquals(bValuePair, result);
    }

}