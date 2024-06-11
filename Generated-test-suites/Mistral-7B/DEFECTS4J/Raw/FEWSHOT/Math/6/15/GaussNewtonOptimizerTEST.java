package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ObjectiveFunction;
import org.apache.commons.math3.optimization.linear.GaussNewtonOptimizer;
import org.apache.commons.math3.optimization.linear.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optimization.linear.LinearConstraints;
import org.apache.commons.math3.optimization.linear.NonNegativeConstraint;
import org.apache.commons.math3.optimization.linear.StoppingCriteria;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizerWithLUDecomposition() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, null);
        ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(RealVector vector) {
                double x = vector.getEntry(0);
                return Math.pow(Math.PI - x, 2) + Math.pow(Math.E - x, 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };

        double[] x = new double[]{1.0};
        double[] expected = new double[]{Math.E - Math.log(Math.E), Math.PI - Math.PI / 2};

        optimizer.optimize(objectiveFunction, new PointValuePair(new ArrayRealVector(x), new ArrayRealVector(new double[]{0.0})),
                new StoppingCriteria(100), new LinearConstraints());

        assertArrayEquals(expected, x, 0.0001);
    }

    @Test
    public void testGaussNewtonOptimizerWithQRDecomposition() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, null);
        ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(RealVector vector) {
                double x = vector.getEntry(0);
                return Math.pow(Math.PI - x, 2) + Math.pow(Math.E - x, 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };

        double[] x = new double[]{1.0};
        double[] expected = new double[]{Math.E - Math.log(Math.E), Math.PI - Math.PI / 2};

        optimizer.optimize(objectiveFunction, new PointValuePair(new ArrayRealVector(x), new ArrayRealVector(new double[]{0.0})),
                new StoppingCriteria(100), new LinearConstraints());

        assertArrayEquals(expected, x, 0.0001);
    }

    @Test
    public void testGaussNewtonOptimizerWithConstraints() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, null);
        ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(RealVector vector) {
                double x = vector.getEntry(0);
                return x * x;
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };

        LinearConstraints constraints = new LinearConstraints();
        constraints.add(new NonNegativeConstraint(0));

        double[] x = new double[]{1.0};
        double expected = 0.0;

        optimizer.optimize(objectiveFunction, new PointValuePair(new ArrayRealVector(x), new ArrayRealVector(new double[]{0.0})),
                new StoppingCriteria(100), constraints);

        assertEquals(expected, x[0], 0.0001);
    }

    @Test
    public void testGaussNewtonOptimizerWithLevenbergMarquardtOptimizer() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, null);
        ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(RealVector vector) {
                double x = vector.getEntry(0);
                return Math.pow(Math.PI - x, 2) + Math.pow(Math.E - x, 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };

        double[] x = new double[]{1.0};
        double[] expected = new double[]{Math.E - Math.log(Math.E), Math.PI - Math.PI / 2};

        LevenbergMarquardtOptimizer lmo = new LevenbergMarquardtOptimizer(10, 1e-6, 1e-6, 1e-6);
        PointValuePair initialGuess = new PointValuePair(new ArrayRealVector(x), new ArrayRealVector(new double[]{0.0}));
        PointValuePair result = lmo.optimize(objectiveFunction, initialGuess, new StoppingCriteria(100), new LinearConstraints());

        assertArrayEquals(expected, result.getPoint().toArray(), 0.0001);
        optimizer.optimize(objectiveFunction, initialGuess, new StoppingCriteria(100), new LinearConstraints());
        PointVectorValuePair optimizerResult = optimizer.getLastIterate();
        assertArrayEquals(expected, optimizerResult.getPoint().toArray(), 0.0001);
    }

}