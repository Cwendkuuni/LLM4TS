package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.univariate.BracketFinder;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.goaltype.GoalType;
import org.apache.commons.math3.optimization.nonlinear.NonlinearConstraint;
import org.apache.commons.math3.optimization.nonlinear.NonlinearConstraintSet;
import org.apache.commons.math3.optimization.nonlinear.NonlinearFunction;
import org.apache.commons.math3.optimization.nonlinear.NonlinearPointValuePair;
import org.apache.commons.math3.optimization.nonlinear.SimpleBounds;
import org.apache.commons.math3.optimization.nonlinear.SimpleNonlinearConstraint;
import org.apache.commons.math3.optimization.nonlinear.SimpleNonlinearConstraintSet;
import org.apache.commons.math3.optimization.nonlinear.SimpleNonlinearObjectiveFunction;
import org.apache.commons.math3.optimization.nonlinear.NonlinearOptimizer;
import org.junit.Test;

import java.util.Arrays;

public class PowellOptimizerTest {

    @Test
    public void testConstructor1() {
        // Test default constructor
        PowellOptimizer optimizer = new PowellOptimizer();
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertNull(optimizer.getConvergenceChecker());
        assertEquals(0.004, optimizer.getLineSearchRelativeTolerance(), 0.001);
        assertEquals(0.04, optimizer.getLineSearchAbsoluteTolerance(), 0.01);
    }

    @Test
    public void testConstructor2() {
        // Test constructor with custom convergence checker
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                // Custom convergence check
                return (previous.getValue() - current.getValue()) < 0.001;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, convergenceChecker);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(0.01, optimizer.getRelativeThreshold(), 0.001);
        assertEquals(0.001, optimizer.getAbsoluteThreshold(), 0.001);
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testConstructor3() {
        // Test constructor with custom convergence checker and line search tolerances
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                // Custom convergence check
                return (previous.getValue() - current.getValue()) < 0.001;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, 0.002, 0.02, convergenceChecker);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(0.01, optimizer.getRelativeThreshold(), 0.001);
        assertEquals(0.001, optimizer.getAbsoluteThreshold(), 0.001);
        assertEquals(0.002, optimizer.getLineSearchRelativeTolerance(), 0.001);
        assertEquals(0.02, optimizer.getLineSearchAbsoluteTolerance(), 0.01);
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testConstructor4() {
        // Test constructor with custom convergence checker, line search tolerances, and optimization goal
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                // Custom convergence check
                return (previous.getValue() - current.getValue()) < 0.001;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, 0.002, 0.02, convergenceChecker, GoalType.MAXIMIZE);
        assertEquals(GoalType.MAXIMIZE, optimizer.getGoalType());
        assertEquals(0.01, optimizer.getRelativeThreshold(), 0.001);
        assertEquals(0.001, optimizer.getAbsoluteThreshold(), 0.001);
        assertEquals(0.002, optimizer.getLineSearchRelativeTolerance(), 0.001);
        assertEquals(0.02, optimizer.getLineSearchAbsoluteTolerance(), 0.01);
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testOptimizeFunction1() {
        // Test optimizing a simple function
        PowellOptimizer optimizer = new PowellOptimizer();
        double[] startPoint = new double[]{1.0, 1.0};
        NonlinearFunction function = new SimpleNonlinearObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.pow(x[0], 2) + Math.pow(x[1], 2), 0.5);
            }
        };
        PointValuePair result = optimizer.optimize(function, startPoint);
        double[] expectedResult = new double[]{1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)};
        assertArrayEquals(expectedResult, result.getPoint(), 0.001);
        assertEquals(Math.pow(Math.E, -1.0), result.getValue(), 0.001);
    }

    @Test
    public void testOptimizeFunction2() {
        // Test optimizing a function with constraints
        PowellOptimizer optimizer = new PowellOptimizer();
        double[] startPoint = new double[]{1.0, 1.0};
        NonlinearFunction function = new SimpleNonlinearObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.pow(x[0], 2) + Math.pow(x[1], 2), 0.5) - 2.0;
            }
        };
        NonlinearConstraintSet constraints = new SimpleNonlinearConstraintSet();
        constraints.add(new SimpleNonlinearConstraint(new SimpleNonlinearFunction() {
            @Override
            public double value(double[] x) {
                return x[0] - 1.0;
            }
        }, 0.0));
        constraints.add(new SimpleNonlinearConstraint(new SimpleNonlinearFunction() {
            @Override
            public double value(double[] x) {
                return x[1] + 1.0;
            }
        }, 0.0));
        PointValuePair result = optimizer.optimize(function, startPoint, constraints);
        double[] expectedResult = new double[]{1.0, 0.0};
        assertArrayEquals(expectedResult, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    @Test
    public void testOptimizeFunction3() {
        // Test optimizing a function with a custom convergence checker
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.001, new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return (Math.abs(previous.getValue() - current.getValue()) < 0.0001);
            }
        });
        double[] startPoint = new double[]{1.0, 1.0};
        NonlinearFunction function = new SimpleNonlinearObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.pow(x[0], 2) + Math.pow(x[1], 2), 0.5);
            }
        };
        PointValuePair result = optimizer.optimize(function, startPoint);
        double[] expectedResult = new double[]{1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)};
        assertArrayEquals(expectedResult, result.getPoint(), 0.001);
        assertEquals(Math.pow(Math.E, -1.0), result.getValue(), 0.001);
    }

    @Test
    public void testOptimizeFunction4() {
        // Test optimizing a function with custom convergence checker and custom line search tolerances
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.001, 0.0001, 0.001, new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return (Math.abs(previous.getValue() - current.getValue()) < 0.0001);
            }
        });
        double[] startPoint = new double[]{1.0, 1.0};
        NonlinearFunction function = new SimpleNonlinearObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.pow(x[0], 2) + Math.pow(x[1], 2), 0.5);
            }
        };
        PointValuePair result = optimizer.optimize(function, startPoint);
        double[] expectedResult = new double[]{1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)};
        assertArrayEquals(expectedResult, result.getPoint(), 0.001);
        assertEquals(Math.pow(Math.E, -1.0), result.getValue(), 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsoluteThreshold() {
        new PowellOptimizer(-0.01, 0.01);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeRelativeThreshold() {
        new PowellOptimizer(0.01, -0.01);
    }

    @Test
    public void testSearchLine1() {
        PowellOptimizer optimizer = new PowellOptimizer();
        double[] startPoint = new double[]{0.0};
        double[] searchDirection = new double[]{1.0};
        double optimum = 1.0;

        UnivariatePointValuePair result = optimizer.getLineSearch().search(startPoint, searchDirection);

        assertEquals(optimum, result.getValue(), 0.001);
        assertEquals(optimum, result.getPoint(), 0.001);
    }

    @Test
    public void testSearchLine2() {
        PowellOptimizer optimizer = new PowellOptimizer();
        double[] startPoint = new double[]{0.0};
        double[] searchDirection = new double[]{-1.0};
        double optimum = -1.0;

        UnivariatePointValuePair result = optimizer.getLineSearch().search(startPoint, searchDirection);

        assertEquals(optimum, result.getValue(), 0.001);
        assertEquals(optimum, result.getPoint(), 0.001);
    }

    @Test
    public void testSearchLine3() {
        PowellOptimizer optimizer = new PowellOptimizer();
        double[] startPoint = new double[]{0.0};
        double[] searchDirection = new double[]{1.0};
        double optimum = 3.0;

        UnivariatePointValuePair result = optimizer.getLineSearch().search(startPoint, searchDirection);

        assertEquals(optimum, result.getValue(), 0.001);
        assertEquals(optimum, result.getPoint() * Math.signum(Math.abs(searchDirection[0])), 0.001);
    }
}