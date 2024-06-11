package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LiangBorneOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.SimpleBound;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.SimpleSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.SimpleSimplexConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.SimpleSimplexOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.linearConstraintSet.ConstraintType;
import org.apache.commons.math3.special.VectorFunctions;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {

    private MultivariateFunction testFunction;
    private ConvergenceChecker<PointValuePair> convergenceChecker;
    private MultivariateOptimizer optimizer;
    private AbstractSimplex simplex;

    @Before
    public void setUp() {
        convergenceChecker = new SimpleValueChecker(1e-6, 1e-6);
        testFunction = new ObjectiveFunction() {
            public double value(double[] x) {
                return FunctionUtils.sum(VectorFunctions.multiply(x, x));
            }
        };
        simplex = new SimpleSimplex(3);
        optimizer = new SimplexOptimizer(convergenceChecker);
    }

    /**
     * Tests {@code SimplexOptimizer#setSimplex(AbstractSimplex)}.
     */
    @Test
    public void testSetSimplex() {
        optimizer.setSimplex(simplex);
        assertNotNull(optimizer.getSimplex());
    }

    /**
     * Tests {@code SimplexOptimizer#optimize(int,MultivariateFunction,GoalType,double[])}.
     */
    @Test
    public void testOptimize() {
        optimizer.optimize(100, testFunction, GoalType.MINIMIZE, new double[3]);
        PointValuePair result = optimizer.getOptimum();
        assertNotNull(result);
        assertArrayEquals(new double[]{-1.0, -1.0, -1.0}, result.getPoint(), 0.0);
        assertEquals(-3.0, result.getValue(), 0.0);
    }

    /**
     * Tests {@code SimplexOptimizer#optimize(int,MultivariateFunction,GoalType,double[])}
     * with an initial point outside the domain of the function.
     */
    @Test(expected = UnboundedDomainException.class)
    public void testOptimizeOutOfDomain() {
        optimizer.optimize(100, testFunction, GoalType.MINIMIZE, new double[]{1.0, 1.0, 1.0});
    }

    /**
     * Tests {@code SimplexOptimizer#optimize(int,MultivariateFunction,GoalType,double[])}
     * with a {@code null} optimization data array.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullData() {
        optimizer.optimize(100, testFunction, GoalType.MINIMIZE, null);
    }

    /**
     * Tests {@code SimplexOptimizer#optimize(int,MultivariateFunction,GoalType,double[])}
     * with an empty optimization data array.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeEmptyData() {
        optimizer.optimize(100, testFunction, GoalType.MINIMIZE, new OptimizationData[0]);
    }

    /**
     * Tests {@code SimplexOptimizer#optimize(int,MultivariateFunction,GoalType,double[])}
     * with a {@code null} optimization data.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeNullOptimizationData() {
        optimizer.optimize(100, testFunction, GoalType.MINIMIZE, new OptimizationData[]{null});
    }

    /**
     * Tests {@code SimplexOptimizer#optimize(int,MultivariateFunction,GoalType,double[])}
     * with a {@link LinearConstraintSet} in the optimization data.
     */
    @Test
    public void testOptimizeWithConstraints() {
        LinearObjectiveFunction linObjFunc = new LinearObjectiveFunction(new double[][]{{1.0, 1.0}, {2.0, -1.0}}, 3.0);
        LinearConstraintSet constraints = new SimpleSimplexConstraintSet(
            new LinearConstraint[] {
                new LinearConstraint(new double[]{-1.0, -1.0}, ConstraintType.LEQ, 0.0),
                new LinearConstraint(new double[]{1.0, 1.0}, ConstraintType.LEQ, 1.0),
                new LinearConstraint(new double[]{-1.0, 1.0}, ConstraintType.LEQ, 0.0),
                new LinearConstraint(new double[]{1.0, -1.0}, ConstraintType.LEQ, 0.0)
            });
        optimizer = new SimplexOptimizer(convergenceChecker);
        optimizer.optimize(100, linObjFunc, GoalType.MINIMIZE, new OptimizationData[]{
            new OptimizationData(){
                @Override
                public Object getParameter() {
                    return simplex;
                }
            },
            new OptimizationData(){
                @Override
                public Object getParameter() {
                    return constraints;
                }
            }
        });
        PointValuePair result = optimizer.getOptimum();
        assertNotNull(result);
        assertArrayEquals(new double[]{-1.0, -1.0}, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    /**
     * Tests {@code SimplexOptimizer#optimize(int,MultivariateFunction,GoalType,double[])}
     * with a {@link SimpleBound} in the optimization data.
     */
    @Test
    public void testOptimizeWithBounds() {
        LinearObjectiveFunction linObjFunc = new LinearObjectiveFunction(new double[][]{{1.0, 1.0}, {2.0, -1.0}}, 3.0);
        SimpleSimplex simplex = new SimpleSimplex(3);
        optimizer = new SimplexOptimizer(convergenceChecker);
        optimizer.optimize(100, linObjFunc, GoalType.MINIMIZE, new OptimizationData[]{
            new OptimizationData(){
                @Override
                public Object getParameter() {
                    return simplex;
                }
            },
            new OptimizationData(){
                @Override
                public Object getParameter() {
                    return new SimpleBound[]{
                        new SimpleBound(-1.0, Double.POSITIVE_INFINITY, 0),
                        new SimpleBound(-1.0, Double.POSITIVE_INFINITY, 1),
                        new SimpleBound(-1.0, Double.POSITIVE_INFINITY, 2)
                    };
                }
            }
        });
        PointValuePair result = optimizer.getOptimum();
        assertNotNull(result);
        assertArrayEquals(new double[]{-1.0, -1.0, -1.0}, result.getPoint(), 0.0);
        assertEquals(-3.0, result.getValue(), 0.0);
    }

    /**
     * Tests {@code SimplexOptimizer#optimize(int,MultivariateFunction,GoalType,double[])}
     * with a {@link LiangBorneOptimizer} in the optimization data.
     */
    @Test
    public void testOptimizeWithLiangBorner() {
        MultivariateFunction testFunction = new MultivariateFunction() {
            public double value(double[] x) {
                return FunctionUtils.sum(VectorFunctions.multiply(x, x)) + Math.sin(Math.PI * FunctionUtils.sum(VectorFunctions.multiply(x, x)));
            }
        };
        LiangBornerOptimizer liangBorner = new LiangBornerOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        optimizer = new SimplexOptimizer(liangBorner);
        optimizer.optimize(100, testFunction, GoalType.MINIMIZE, new double[3]);
        PointValuePair result = optimizer.getOptimum();
        assertNotNull(result);
        assertEquals(0.0, Math.PI / 2.0, Math.toRadians(result.getValue() * Math.PI / 180.0), 1e-6);
        assertArrayEquals(new double[]{0.226467686502276, 0.528322142316424, 0.821362526914752}, result.getPoint(), 1e-6);
    }
}