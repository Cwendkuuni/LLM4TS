package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.NonUnaryOptimizer;
import org.apache.commons.math3.optim.PointValueConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test for PowellOptimizer class.
 */
public class PowellOptimizerTest {
    /**
     * Test for optimize method with a simple objective function.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-6, 1e-6);
        double[] startPoint = new double[] { 1.0, 2.0 };
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair solution = powellOptimizer.optimize(objectiveFunction, startPoint, goalType, null);
        assertEquals(0.0, solution.getValue(), 1e-6);
        assertArrayEquals(new double[] { 0.0, 0.0 }, solution.getPoint(), 1e-6);
    }

    /**
     * Test for optimize method with a simple objective function and bounds.
     */
    @Test
    public void testOptimizeSimpleFunctionWithBounds() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-6, 1e-6);
        double[] startPoint = new double[] { 1.0, 2.0 };
        double[] bounds = new double[] { 0.0, 10.0, 0.0, 10.0 };
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        List<PointValueConstraint<Double>> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new SimpleBounds(0.0, 10.0), null));
        constraints.add(new LinearConstraint(new SimpleBounds(0.0, 10.0), null));
        powellOptimizer.optimize(objectiveFunction, startPoint, goalType, null, constraints, new MaxEval(100));
        assertEquals(0.0, powellOptimizer.getBestValue(), 1e-6);
        assertArrayEquals(new double[] { 0.0, 0.0 }, powellOptimizer.getSolution().getPoint(), 1e-6);
    }

    /**
     * Test for optimize method with a non-simple objective function.
     */
    @Test
    public void testOptimizeNonSimpleFunction() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-6, 1e-6);
        double[] startPoint = new double[] { 1.0, 2.0 };
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) + Math.cos(x[1]), 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        powellOptimizer.optimize(objectiveFunction, startPoint, goalType, null);
        double bestValue = powellOptimizer.getBestValue();
        PointValuePair bestPoint = powellOptimizer.getSolution();
        double epsilon = 1e-6;
        assertTrue("Best value not good enough.", bestValue < 1.0 - epsilon);
        assertArrayEquals("Solution not good enough.", new double[] { Math.PI / 2.0, Math.PI / 4.0 }, bestPoint.getPoint(), epsilon);
    }

    /**
     * Test for optimize method with a non-simple objective function and bounds.
     */
    @Test
    public void testOptimizeNonSimpleFunctionWithBounds() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-6, 1e-6);
        double[] startPoint = new double[] { 1.0, 2.0 };
        double[] bounds = new double[] { -Math.PI, Math.PI, -Math.PI, Math.PI };
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) + Math.cos(x[1]), 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        powellOptimizer.optimize(objectiveFunction, startPoint, goalType, null, null, new MaxEval(100));
        double bestValue = powellOptimizer.getBestValue();
        PointValuePair bestPoint = powellOptimizer.getSolution();
        double epsilon = 1e-6;
        assertTrue("Best value not good enough.", bestValue < 1.0 - epsilon);
        assertArrayEquals("Solution not good enough.", new double[] { Math.PI / 2.0, Math.PI / 4.0 }, bestPoint.getPoint(), epsilon);
    }

    /**
     * Test for optimize method with a non-simple objective function and constraints.
     */
    @Test
    public void testOptimizeNonSimpleFunctionWithConstraints() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-6, 1e-6);
        double[] startPoint = new double[] { 1.0, 2.0 };
        double[] bounds = new double[] { -Math.PI, Math.PI, -Math.PI, Math.PI };
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) + Math.cos(x[1]), 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        List<PointValueConstraint<Double>> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new SimpleBounds(-Math.PI, Math.PI), 0));
        constraints.add(new LinearConstraint(new SimpleBounds(-Math.PI, Math.PI), 1));
        powellOptimizer.optimize(objectiveFunction, startPoint, goalType, null, constraints, new MaxEval(100));
        double bestValue = powellOptimizer.getBestValue();
        PointValuePair bestPoint = powellOptimizer.getSolution();
        double epsilon = 1e-6;
        assertTrue("Best value not good enough.", bestValue < 1.0 - epsilon);
        assertArrayEquals("Solution not good enough.", new double[] { Math.PI / 2.0, Math.PI / 4.0 }, bestPoint.getPoint(), epsilon);
    }

    /**
     * Test for optimize method with a non-simple objective function, bounds, and constraints.
     */
    @Test
    public void testOptimizeNonSimpleFunctionWithBoundsAndConstraints() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-6, 1e-6);
        double[] startPoint = new double[] { 1.0, 2.0 };
        double[] bounds = new double[] { -Math.PI, Math.PI, -Math.PI, Math.PI };
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) + Math.cos(x[1]), 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        List<PointValueConstraint<Double>> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new SimpleBounds(-Math.PI, Math.PI), 0));
        constraints.add(new LinearConstraint(new SimpleBounds(-Math.PI, Math.PI), 1));
        powellOptimizer.optimize(objectiveFunction, startPoint, goalType, null, constraints, new MaxEval(100));
        double bestValue = powellOptimizer.getBestValue();
        PointValuePair bestPoint = powellOptimizer.getSolution();
        double epsilon = 1e-6;
        assertTrue("Best value not good enough.", bestValue < 1.0 - epsilon);
        assertArrayEquals("Solution not good enough.", new double[] { Math.PI / 2.0, Math.PI / 4.0 }, bestPoint.getPoint(), epsilon);
    }

    /**
     * Test for optimize method with a non-simple objective function, bounds, and non-linear constraints.
     */
    @Test
    public void testOptimizeNonSimpleFunctionWithNonlinearConstraints() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-6, 1e-6);
        double[] startPoint = new double[] { 1.0, 2.0 };
        double[] bounds = new double[] { -Math.PI, Math.PI, -Math.PI, Math.PI };
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) + Math.cos(x[1]), 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        NonUnaryOptimizer nonlinearConstraint = new NonUnaryOptimizer() {
            @Override
            public PointValuePair value(double[] x) {
                return new PointValuePair(Math.pow(Math.sin(x[0]) + Math.cos(x[1]), 2) - 0.1, null);
            }

            @Override
            public PointValuePair constraint(double[] x) {
                return new PointValuePair(Math.pow(Math.sin(x[0]) + Math.cos(x[1]), 2) - 0.1, null);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };
        powellOptimizer.optimize(objectiveFunction, startPoint, goalType, null, null, nonlinearConstraint, new MaxEval(100));
        double bestValue = powellOptimizer.getBestValue();
        PointValuePair bestPoint = powellOptimizer.getSolution();
        double epsilon = 1e-6;
        assertTrue("Best value not good enough.", bestValue < 0.0 - epsilon);
        assertArrayEquals("Solution not good enough.", new double[] { Math.PI / 2.0 - 0.1, Math.PI / 4.0 }, bestPoint.getPoint(), epsilon);
    }

    /**
     * Test for optimize method with a non-simple objective function, bounds, and non-linear constraints, and constraints violation tolerance.
     */
    @Test
    public void testOptimizeNonSimpleFunctionWithNonlinearConstraintsAndTolerance() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-6, 1e-6);
        double[] startPoint = new double[] { 1.0, 2.0 };
        double[] bounds = new double[] { -Math.PI, Math.PI, -Math.PI, Math.PI };
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) + Math.cos(x[1]), 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        NonUnaryOptimizer nonlinearConstraint = new NonUnaryOptimizer() {
            @Override
            public PointValuePair value(double[] x) {
                return new PointValuePair(Math.pow(Math.sin(x[0]) + Math.cos(x[1]), 2) - 0.1, null);
            }

            @Override
            public PointValuePair constraint(double[] x) {
                return new PointValuePair(Math.pow(Math.sin(x[0]) + Math.cos(x[1]), 2) - 0.1, null);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };
        powellOptimizer.optimize(objectiveFunction, startPoint, goalType, null, null, nonlinearConstraint, new MaxEval(100), new SimpleBounds(-1.0, 1.0), new SimpleBounds(-1.0, 1.0));
        double bestValue = powellOptimizer.getBestValue();
        PointValuePair bestPoint = powellOptimizer.getSolution();
        double epsilon = 1e-6;
        assertTrue("Best value not good enough.", bestValue < 0.0 - epsilon);
        assertArrayEquals("Solution not good enough.", new double[] { Math.PI / 2.0 - 0.1, Math.PI / 4.0 }, bestPoint.getPoint(), epsilon);
    }
}