package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MultivariateFunction;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.convergence.SimpleLineSearchConvergenceChecker;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinOpAdd;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.nonlinear.GoalTypeEnum;
import org.apache.commons.math3.optimization.nonlinear.LinearConstraintSet;
import org.apache.commons.math3.optimization.nonlinear.LinearObjectiveFunctionSet;
import org.apache.commons.math3.optimization.nonlinear.linear.AffineFunction;
import org.apache.commons.math3.optimization.nonlinear.linear.LinearConstraintSetFactory;
import org.apache.commons.math3.optimization.nonlinear.linear.LinearObjectiveFunctionSetFactory;
import org.apache.commons.math3.optimization.nonlinear.linear.LinearPointValuePair;
import org.apache.commons.math3.optimization.nonlinear.linear.LinearSolver;
import org.apache.commons.math3.optimization.nonlinear.linear.QPCG;
import org.apache.commons.math3.optimization.nonlinear.linear.QR;
import org.apache.commons.math3.optimization.nonlinear.linear.SingularValueDecomposition;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class PowellOptimizerTest {

    @Test
    public void testConstructor() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-5, 1e-6);
        Assert.assertNotNull(powellOptimizer);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithRelativelySmallAbsoluteThreshold() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-5, 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithRelativelySmallRelativeThreshold() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(0.0, 1e-5);
    }

    @Test
    public void testOptimizeWithSimpleFunction() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        PointValuePair pointValuePair = new PowellOptimizer(1e-5, 1e-6, new SimpleLineSearchConvergenceChecker(100))
                .optimize(function, GoalTypeEnum.MINIMIZE, new double[]{2.0, 2.0});
        Assert.assertEquals(0.0, pointValuePair.getValue(), 1e-5);
        Assert.assertArrayEquals(new double[]{0.0, 0.0}, pointValuePair.getPoint(), 1e-5);
    }

    @Test
    public void testOptimizeWithLinearConstraints() {
        LinearConstraint constraint1 = new LinearConstraint(new AffineFunction(1.0, 1.0, 2.0), 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new AffineFunction(-1.0, -1.0, 3.0), -1.0);
        LinearConstraintSet constraints = LinearConstraintSetFactory.create(Arrays.asList(constraint1, constraint2));
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunctionSetFactory()
                .create(new LinearObjectiveFunction[]{new LinearObjectiveFunction(new AffineFunction(1.0, 1.0, 0.0))});
        PointValuePair pointValuePair = new PowellOptimizer(1e-5, 1e-6, new SimpleLineSearchConvergenceChecker(100))
                .optimize(objectiveFunction, GoalTypeEnum.MINIMIZE, new double[]{2.0, 2.0}, constraints);
        Assert.assertEquals(0.0, pointValuePair.getValue(), 1e-5);
        Assert.assertArrayEquals(new double[]{1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)}, pointValuePair.getPoint(), 1e-5);
    }

    @Test
    public void testOptimizeWithSingularValueDecompositionSolver() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                SingularValueDecomposition svd = new SingularValueDecomposition(MathArrays.create(x));
                return svd.getSingularValues()[0] * svd.getSingularValues()[1];
            }
        };
        PointValuePair pointValuePair = new PowellOptimizer(1e-5, 1e-6, new SimpleLineSearchConvergenceChecker(100))
                .optimize(function, GoalTypeEnum.MINIMIZE, new double[]{1.0, 1.0});
        Assert.assertFalse(Double.isFinite(pointValuePair.getValue()));
        Assert.assertArrayEquals(new double[]{0.0, 0.0}, pointValuePair.getPoint(), 1e-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithNonLinearFunction() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.sin(Math.sqrt(Math.pow(x[0], 2.0) + Math.pow(x[1], 2.0)));
            }
        };
        new PowellOptimizer(1e-5, 1e-6)
                .optimize(function, GoalTypeEnum.MINIMIZE, new double[]{2.0, 2.0});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithNullFunction() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-5, 1e-6);
        powellOptimizer.optimize(null, GoalTypeEnum.MINIMIZE, new double[]{2.0, 2.0});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithNullPoint() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return 0.0;
            }
        };
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-5, 1e-6);
        powellOptimizer.optimize(function, GoalTypeEnum.MINIMIZE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithNullOrEmptyConstraints() {
        LinearObjectiveFunctionSet objectiveFunctionSet = new LinearObjectiveFunctionSetFactory()
                .create(Collections.emptyList());
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-5, 1e-6);
        powellOptimizer.optimize(objectiveFunctionSet, GoalTypeEnum.MINIMIZE, new double[]{2.0, 2.0});
    }
}