package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.PointValueFunction;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunctionSet;
import org.apache.commons.math3.optim.linear.LinearConstraintSet.ConstraintType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValueFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.ValueType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraintSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;

/**
 * Test cases for {@link PowellOptimizer}.
 */
@RunWith(Parameterized.class)
public class PowellOptimizerTest {

    /**
     * The Powell optimizer instance.
     */
    private PowellOptimizer optimizer;

    /**
     * Constructor.
     *
     * @param relativeThreshold The relative tolerance.
     * @param absoluteThreshold The absolute tolerance.
     */
    public PowellOptimizerTest(double relativeThreshold, double absoluteThreshold) {
        optimizer = new PowellOptimizer(relativeThreshold, absoluteThreshold);
    }

    /**
     * Test the default constructor with default tolerance values.
     */
    @Parameterized.Parameters(name = "{index}: PowellOptimizer(default)")
    public static Collection<Object[]> defaultConstructor() {
        return Arrays.asList(new Object[][]{
                {2 * FastMath.ulp(1d), 0},
                {2 * FastMath.ulp(1d), FastMath.ulp(1d)},
                {FastMath.ulp(1d), 2 * FastMath.ulp(1d)},
                {FastMath.ulp(1d), FastMath.ulp(1d)},
                {10 * FastMath.ulp(1d), 10 * FastMath.ulp(1d)},
                {1e-5, 1e-5},
                {1e-5, 1e-6},
                {1e-6, 1e-5},
                {1e-6, 1e-6}
        });
    }

    /**
     * Test the constructor with user-defined tolerance values.
     */
    @Parameterized.Parameters(name = "{index}: PowellOptimizer({relativeThreshold={0}}, {absoluteThreshold={1}})")
    public static Collection<Object[]> customConstructor() {
        return Arrays.asList(new Object[][]{
                {0.01, 0.001},
                {0.001, 1e-5},
                {1e-6, 1e-6},
                {1e-5, 1e-5},
                {1e-4, 1e-6},
                {1e-3, 1e-6}
        });
    }

    /**
     * Test the optimization of a univariate function.
     */
    @Test
    public void testUnivariateFunctionOptimization() {
        final UnivariateFunction f = new PointValueFunction(Math.exp) {
            @Override
            public PointValuePair value(double x) {
                return new PointValuePair(x, Math.exp(x));
            }
        };

        final PointValuePair pvp = optimizer.optimize(f, GoalType.MINIMIZE, new PointValuePair(1d, 2.718281828));
        assertEquals(0d, pvp.getValue1(), 1e-7);
    }

    /**
     * Test the optimization of a multivariate function with linear equality
     * constraints.
     */
    @Test
    public void testMultivariateFunctionOptimizationWithLinearConstraints() {
        final LinearObjectiveFunction f = new LinearObjectiveFunctionSet(
                new LinearObjectiveFunction[] {
                        new LinearObjectiveFunction(new double[][]{{1.0}, {-2.0}}, 1.0),
                        new LinearObjectiveFunction(new double[][]{{1.0}, {1.0}}, -1.0),
                        new LinearObjectiveFunction(new double[][]{{1.0}, {2.0}}, 3.0)
                });

        final LinearConstraintSet constraints = new LinearConstraintSet(
                new LinearConstraint[] {
                        new LinearConstraint(new double[][]{{1.0}, {-1.0}}, ConstraintType.EQUALITY),
                        new LinearConstraint(new double[][]{{1.0}, {1.0}}, ConstraintType.EQUALITY),
                        new LinearConstraint(new double[][]{{1.0}, {3.0}}, ConstraintType.EQUALITY)
                });

        final PointValuePair pvp = optimizer.optimize(
                new PointValueFunction() {
                    @Override
                    public PointValuePair value(double[] x) {
                        return new PointValuePair(x, f.value(x));
                    }
                },
                GoalType.MINIMIZE,
                new PointValuePair(new double[]{2.0, 1.0, 1.0}, 2.0),
                constraints);

        assertEquals(0d, pvp.getValue1()[0], 1e-7);
        assertEquals(1d, pvp.getValue1()[1], 1e-7);
        assertEquals(1d, pvp.getValue1()[2], 1e-7);
    }

    /**
     * Test the optimization of a multivariate function with linear inequality
     * constraints.
     */
    @Test
    public void testMultivariateFunctionOptimizationWithLinearInequalityConstraints() {
        final LinearObjectiveFunction f = new LinearObjectiveFunctionSet(
                new LinearObjectiveFunction[] {
                        new LinearObjectiveFunction(new double[][]{{1.0}, {-2.0}}, 1.0),
                        new LinearObjectiveFunction(new double[][]{{1.0}, {1.0}}, -1.0),
                        new LinearObjectiveFunction(new double[][]{{1.0}, {2.0}}, 3.0)
                });

        final LinearConstraintSet constraints = new LinearConstraintSet(
                new LinearConstraint[] {
                        new LinearConstraint(new double[][]{{1.0}, {-1.0}}, ConstraintType.LEQ),
                        new LinearConstraint(new double[][]{{1.0}, {1.0}}, ConstraintType.GEQ),
                        new LinearConstraint(new double[][]{{1.0}, {3.0}}, ConstraintType.LEQ)
                });

        final PointValuePair pvp = optimizer.optimize(
                new PointValueFunction() {
                    @Override
                    public PointValuePair value(double[] x) {
                        return new PointValuePair(x, f.value(x));
                    }
                },
                GoalType.MINIMIZE,
                new PointValuePair(new double[]{2.0, 1.0, 1.0}, 2.0),
                constraints);

        assertTrue(pvp.getValue1()[0] <= 1);
        assertTrue(pvp.getValue1()[1] >= 1);
        assertTrue(pvp.getValue1()[0] <= 3);
    }

    /**
     * Test the optimization of a multivariate function.
     */
    @Test
    public void testMultivariateFunctionOptimization() {
        final PointValueFunction f = new PointValueFunction() {
            @Override
            public PointValuePair value(double[] x) {
                return new PointValuePair(x, Math.pow(x[0], 2) + Math.pow(x[1], 2));
            }
        };

        final PointValuePair pvp = optimizer.optimize(f, GoalType.MINIMIZE, new PointValuePair(1d, 1d), new MaxEval(100));
        assertEquals(0d, pvp.getValue1()[0], 1e-7);
        assertEquals(0d, pvp.getValue1()[1], 1e-7);
        assertEquals(0d, pvp.getValue2(), 1e-7);
    }
}