package org.apache.commons.cli.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.multivariate.LinearConstraint;
import org.apache.commons.math3.optim.multivariate.LinearConstraintSet;
import org.apache.commons.math3.optim.multivariate.NonLinearConstraints;
import org.apache.commons.math3.optim.multivariate.NonNegativeConstraints;
import org.apache.commons.math3.optim.multivariate.PointValuePairCollection;
import org.apache.commons.math3.optim.multivariate.RealVector;
import org.apache.commons.math3.optim.multivariate.SimpleBounds;
import org.apache.commons.math3.optim.multivariate.SimpleVector;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.NonlinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.NonlinearConstraints;
import org.apache.commons.math3.optim.nonlinear.scalar.cholesky.CholeskyDecomposition;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleSimplex;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testSimplexOptimizer() {
        // Test empty constructor
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNull(optimizer.getSimplex());

        // Test constructor with SimpleValueChecker
        optimizer = new SimplexOptimizer(new SimpleValueChecker());
        assertNull(optimizer.getSimplex());

        // Test constructor with SimpleValueChecker and arguments
        optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6), new SimpleSimplex(2));
        assertNotNull(optimizer.getSimplex());
        assertEquals(2, optimizer.getSimplex().getSize());

        // Test setSimplex
        optimizer.setSimplex(new SimpleSimplex(3));
        assertNotNull(optimizer.getSimplex());
        assertEquals(3, optimizer.getSimplex().getSize());

        // Test optimize with no arguments
        optimizer = new SimplexOptimizer(new SimpleValueChecker(), new SimpleSimplex(3));
        try {
            optimizer.optimize(10, new ObjectiveFunction() {
                @Override
                public double value(RealVector x) {
                    return 0;
                }
            }, GoalType.MINIMIZE, null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }

        // Test optimize with start point
        optimizer.setStartPoint(new SimpleVector(3, 1.0, 2.0));
        PointValuePair result = optimizer.optimize(10, new LinearObjectiveFunction(new double[]{-1, -1}, new double[]{1, 1}), GoalType.MINIMIZE, null);
        assertNotNull(result);
        assertEquals(2, result.getPoint().getDimension());
        assertEquals(-1.0, result.getValue(), 1e-6);

        // Test optimize with bounds
        optimizer.setStartPoint(new SimpleVector(3, 0.0, 0.0));
        result = optimizer.optimize(10, new LinearObjectiveFunction(new double[]{-1, -1}, new double[]{1, 1}), GoalType.MINIMIZE, new OptimizationData[]{new OptimizationData() {
            @Override
            public InitialGuess getInitialGuess() {
                return new SimpleBounds(new double[]{-1, -1}, new double[]{1, 1});
            }
        }});
        assertNotNull(result);
        assertEquals(2, result.getPoint().getDimension());
        assertEquals(-1.0, result.getValue(), 1e-6);

        // Test optimize with non-linear constraints
        optimizer.setStartPoint(new SimpleVector(3, 0.0, 0.0));
        LinearConstraintSet linearConstraints = new LinearConstraintSet();
        linearConstraints.add(new LinearConstraint(new double[]{1, 1}, 1, Relationship.LEQ));
        NonlinearConstraint nonLinearConstraint = new NonlinearConstraint(new NonlinearFunction() {
            @Override
            public double value(RealVector x) {
                return Math.exp(FunctionUtils.dotProduct(x, new double[]{1, 1}));
            }

            @Override
            public RealVector gradient(RealVector x) {
                return new SimpleVector(2, Math.exp(FunctionUtils.dotProduct(x, new double[]{1, 1})) * new SimpleVector(1, 1));
            }
        });
        NonlinearConstraints nonlinearConstraints = new NonlinearConstraints();
        nonlinearConstraints.add(nonLinearConstraint);
        PointValuePairCollection solution = optimizer.optimize(10, new ObjectiveFunction() {
            @Override
            public double value(RealVector x) {
                return FunctionUtils.dotProduct(x, new double[]{1, 1}) + 1;
            }
        }, GoalType.MINIMIZE, new OptimizationData[]{new OptimizationData() {
            @Override
            public InitialGuess getInitialGuess() {
                return new SimpleBounds(new double[]{-1, -1}, new double[]{1, 1});
            }

            @Override
            public Constraints getConstraints() {
                return new Constraints() {
                    @Override
                    public LinearConstraintSet getLinearConstraints() {
                        return linearConstraints;
                    }

                    @Override
                    public NonlinearConstraints getNonlinearConstraints() {
                        return nonlinearConstraints;
                    }
                };
            }
        }});
        assertNotNull(solution);
        assertEquals(2, solution.getPoint().getDimension());
        assertEquals(0, solution.getValue(), 1e-6);

        // Test optimize with non-negative constraints
        optimizer.setStartPoint(new SimpleVector(3, 0.0, 0.0));
        result = optimizer.optimize(10, new LinearObjectiveFunction(new double[]{-1, -1}, new double[]{1, 1}), GoalType.MINIMIZE, new OptimizationData[]{new OptimizationData() {
            @Override
            public InitialGuess getInitialGuess() {
                return new SimpleBounds(new double[]{0, 0}, new double[]{1, 1});
            }

            @Override
            public Constraints getConstraints() {
                return new Constraints() {
                    @Override
                    public NonNegativeConstraints getNonNegativeConstraints() {
                        return new NonNegativeConstraints(new int[]{0, 1});
                    }
                };
            }
        }});
        assertNotNull(result);
        assertEquals(2, result.getPoint().getDimension());
        assertEquals(0.0, result.getValue(), 1e-6);
    }
}