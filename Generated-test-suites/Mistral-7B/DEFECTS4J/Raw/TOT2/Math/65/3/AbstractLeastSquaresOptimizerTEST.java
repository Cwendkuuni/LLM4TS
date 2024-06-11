package org.apache.commons.math.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialFunction;
import org.apache.commons.math3.optim.VectorOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSet.ConstraintType;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction.Constraint;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.linear2D.LinearConstraint2D;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.linear2D.LinearObjectiveFunction2D;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.linear2D.LevenbergMarquardtOptimizer;
import org.junit.Test;

import org.apache.commons.math.optim.MaxEvaluationsExceededException;
import org.apache.commons.math.optim.MaxIterationsExceededException;
import org.apache.commons.math.optim.NoSolutionException;

import org.apache.commons.math.optimization.general.AbstractLeastSquaresOptimizer;
import org.apache.commons.math.optimization.general.AbstractLeastSquaresOptimizer.DifferentiableMultivariateVectorialOptimizer;
import org.apache.commons.math.optimization.general.GoalType;

/**
 * Test for {@link AbstractLeastSquaresOptimizer}.
 */
public class AbstractLeastSquaresOptimizerTest {

    /**
     * Test optimization of a simple 2D linear least squares problem.
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testSimpleLinearLeastSquaresOptimization() throws Exception {
        // prepare test data
        double[] x = new double[]{0.0, 1.0};
        double[] y = new double[]{2.0, 3.0};
        double[] weights = new double[]{1.0, 1.0};
        double[] initialPoint = new double[]{1.0, 1.0};
        double coefficient = 3.5;
        double intercept = 1.5;

        // create test function
        ObjectiveFunction objectiveFunction = new LinearObjectiveFunction2D(
                new double[][]{new double[]{-1.0, -1.0}, new double[]{1.0, 1.0}},
                new double[]{coefficient, intercept},
                new double[]{}, new double[]{1.0, 1.0},
                GoalType.MINIMIZE);

        // create test optimizer
        VectorOptimizer optimizer = new LevenbergMarquardtOptimizer(1e-6, 100);

        // perform optimization
        PointValuePair solution = optimizer.optimize(objectiveFunction, new double[]{y[0], y[1]}, null, initialPoint);

        // check solution
        double[] expectedSolution = new double[]{coefficient / 2.0, intercept / 2.0};
        assertEquals(expectedSolution[0], solution.getPoint()[0], 1e-6);
        assertEquals(expectedSolution[1], solution.getValue(), 1e-6);

        // test optimization with custom optimizer
        AbstractLeastSquaresOptimizer customOptimizer = new CustomLeastSquaresOptimizer();
        customOptimizer.setMaxIterations(100);
        customOptimizer.setMaxEvaluations(Integer.MAX_VALUE);
        customOptimizer.optimize(
                new SimpleVectorialFunction(objectiveFunction.value(new double[]{0.0, 0.0})) {
                    @Override
                    public int getDimension() {
                        return 2;
                    }
                },
                new double[]{y[0], y[1]},
                weights,
                initialPoint);

        // test optimization with constraints
        double[][] linearConstraints = new double[][]{{0.0, 0.0, 1.0, 1.0}, {1.0, 1.0, 0.0, 1.0}};
        LinearConstraintSet constraintSet = new LinearConstraintSet(linearConstraints);
        constraintSet.setType(ConstraintType.EQ);

        LinearObjectiveFunction2D linearObjectiveFunction = new LinearObjectiveFunction2D(
                new double[][]{new double[]{-1.0, -1.0}, new double[]{1.0, 1.0}},
                new double[]{coefficient, intercept},
                new double[]{}, new double[]{1.0, 1.0},
                GoalType.MINIMIZE,
                constraintSet);

        PointValuePair solutionWithConstraints = optimizer.optimize(linearObjectiveFunction, new double[]{y[0], y[1]}, null);

        assertEquals(expectedSolution[0], solutionWithConstraints.getPoint()[0], 1e-6);
        assertEquals(expectedSolution[1], solutionWithConstraints.getValue(), 1e-6);

    }

    /**
     * Test optimization of a non-linear least squares problem.
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testNonlinearLeastSquaresOptimization() throws Exception {
        // prepare test data
        double[] x = new double[]{0.0, 1.0};
        double[] y = new double[]{Math.exp(1.0), Math.exp(2.0)};
        double[] weights = new double[]{1.0, 1.0};
        double[] initialPoint = new double[]{0.5, 0.5};
        double coefficient = 1.5;
        double intercept = -1.0;

        // create test function
        ObjectiveFunction objectiveFunction = new SimpleVectorialFunction(new DifferentiableMultivariateVectorialFunction() {
            @Override
            public int getNumberOfVariables() {
                return 2;
            }

            @Override
            public int getNumberOfObjectives() {
                return 1;
            }

            @Override
            public double value(double[] x) {
                return Math.pow(Math.E, x[0]) + Math.pow(Math.E, x[1]) - objectiveFunction.value(new double[]{0.0, 0.0});
            }

            @Override
            public double[] gradient(double[] x) {
                double[] g = new double[2];
                g[0] = Math.E * Math.exp(x[0]);
                g[1] = Math.E * Math.exp(x[1]);
                return g;
            }
        }) {
            @Override
            public int getDimension() {
                return 2;
            }
        };

        // perform optimization
        PointValuePair solution = new LevenbergMarquardtOptimizer().optimize(objectiveFunction, new double[]{y[0], y[1]}, null, initialPoint);

        // check solution
        double[] expectedSolution = new double[]{coefficient / 2.0, intercept / 2.0};
        assertEquals(expectedSolution[0], solution.getPoint()[0], 1e-6);
        assertEquals(expectedSolution[1], solution.getValue(), 1e-6);

        // test optimization with custom optimizer
        AbstractLeastSquaresOptimizer customOptimizer = new CustomLeastSquaresOptimizer();
        customOptimizer.setMaxIterations(100);
        customOptimizer.setMaxEvaluations(Integer.MAX_VALUE);
        customOptimizer.optimize(objectiveFunction, new double[]{y[0], y[1]}, weights, initialPoint);

    }

    /**
     * Test optimization with illegal arguments.
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testIllegalArguments() throws Exception {
        // test optimization with incorrect target values
        assertThrows(IllegalArgumentException.class, () -> {
            new AbstractLeastSquaresOptimizer().optimize(new SimpleVectorialFunction(f -> f[0] * f[1]), new double[]{1.0, 0.0}, null, new double[]{1.0});
        });

        // test optimization with incorrect weights
        assertThrows(IllegalArgumentException.class, () -> {
            new AbstractLeastSquaresOptimizer().optimize(new SimpleVectorialFunction(f -> f[0] * f[1]), new double[]{1.0, 1.0}, new double[]{1.0, 2.0}, new double[]{1.0, 1.0});
        });

        // test optimization with incorrect start point
        assertThrows(IllegalArgumentException.class, () -> {
            new AbstractLeastSquaresOptimizer().optimize(new SimpleVectorialFunction(f -> f[0] * f[1]), new double[]{1.0, 1.0}, null, new double[]{0.0, 2.0});
        });

        // test optimization with incorrect function
        assertThrows(IllegalArgumentException.class, () -> {
            new AbstractLeastSquaresOptimizer().optimize(null, new double[]{1.0, 1.0}, null, new double[]{1.0, 1.0});
        });

        // test optimization with maximal number of iterations exceeded
        assertThrows(MaxIterationsExceededException.class, () -> {
            AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
            optimizer.setMaxIterations(0);
            optimizer.optimize(new SimpleVectorialFunction(f -> f[0] * f[1]), new double[]{1.0, 1.0}, null, new double[]{1.0, 1.0});
        });

        // test optimization with maximal number of evaluations exceeded
        assertThrows(MaxEvaluationsExceededException.class, () -> {
            AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
            optimizer.setMaxEvaluations(0);
            optimizer.optimize(new SimpleVectorialFunction(f -> f[0] * f[1]), new double[]{1.0, 1.0}, null, new double[]{1.0, 1.0});
        });

        // test optimization with no solution
        assertThrows(NoSolutionException.class, () -> {
            LinearObjectiveFunction linearObjectiveFunction = new LinearObjectiveFunction(
                    new double[][]{new double[]{-1.0, -1.0}, new double[]{1.0, 1.0}},
                    new double[]{0.0, 0.0},
                    new double[]{1.0, 1.0},
                    GoalType.MINIMIZE);
            new LevenbergMarquardtOptimizer().optimize(linearObjectiveFunction, new double[]{0.0, 0.0}, null);
        });

    }

}

class CustomLeastSquaresOptimizer extends AbstractLeastSquaresOptimizer {

    @Override
    protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        // implementation of the optimization algorithm goes here
        throw new UnsupportedOperationException();
    }

}