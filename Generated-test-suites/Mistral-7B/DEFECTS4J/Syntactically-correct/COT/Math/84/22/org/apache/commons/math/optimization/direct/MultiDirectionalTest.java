package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.GradientSUSQP;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NonlinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NonlinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NonlinearPointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NonlinearSimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NonlinearUnconstrainedOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleXSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.convergence.AbstractStoppingCondition;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.convergence.RhoX;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.convergence.SimpleLineSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.linear.LUDecomposition;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.linear.LUDecompositionException;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.linear.LUSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.linear.LinAlgException;
import org.apache.commons.math3.optim.PointValuePairCollection;
import org.junit.Test;

import org.apache.commons.math.optimization.direct.MultiDirectional;

/**
 * Test cases for the MultiDirectional class
 */
public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalConstructor() {
        MultiDirectional optimizer = new MultiDirectional();
        assert(optimizer.getKhi() == 2.0);
        assert(optimizer.getGamma() == 0.5);

        MultiDirectional optimizerWithCustomParameters = new MultiDirectional(3.0, 0.8);
        assert(optimizerWithCustomParameters.getKhi() == 3.0);
        assert(optimizerWithCustomParameters.getGamma() == 0.8);
    }

    @Test
    public void testIterateSimplex() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        double[] initialX = {1.0, 2.0};
        SimpleBounds bounds = new SimpleBounds(0.0, 10.0, initialX.length);
        ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) + Math.sin(x[1]), 2.0);
            }

            @Override
            public int getNumberOfVariables() {
                return initialX.length;
            }
        };

        GradientSUSQP optimizerAlg = new GradientSUSQP();
        optimizerAlg.setMaxIterations(1000);
        optimizerAlg.setConvergenceChecker(new RhoX(1e-6, 1e-6));

        optimizer.setMaxIterations(1000);
        optimizer.setConvergenceChecker(new AbstractStoppingCondition() {
            @Override
            public boolean test(double fx, double x[]) {
                return optimizerAlg.isConverged();
            }
        });

        PointValuePair initialPoint = new PointValuePair(initialX, objectiveFunction.value(initialX));
        optimizer.optimize(objectiveFunction, bounds, initialPoint, GoalType.MINIMIZE);

        PointValuePair bestPoint = optimizer.getSolution();
        assertEquals(Arrays.toString(initialX), Arrays.toString(bestPoint.getPoint()), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateSimplexWithNullComparator() {
        MultiDirectional optimizer = new MultiDirectional();
        optimizer.iterateSimplex(null);
    }

    @Test
    public void testEvaluateNewSimplex() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        double[] initialX = {1.0, 2.0};
        double[] reflectionX = {3.0, 4.0};
        double coeff = 1.5;

        SimpleBounds bounds = new SimpleBounds(0.0, 10.0, initialX.length);
        ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) + Math.sin(x[1]), 2.0);
            }

            @Override
            public int getNumberOfVariables() {
                return initialX.length;
            }
        };

        GradientSUSQP optimizerAlg = new GradientSUSQP();
        optimizerAlg.setMaxIterations(1000);
        optimizerAlg.setConvergenceChecker(new RhoX(1e-6, 1e-6));

        optimizer.setMaxIterations(1000);
        optimizer.setConvergenceChecker(new AbstractStoppingCondition() {
            @Override
            public boolean test(double fx, double x[]) {
                return optimizerAlg.isConverged();
            }
        });

        RealPointValuePair[] original = new RealPointValuePair[] {
            new RealPointValuePair(new double[] {initialX[0], initialX[1]}, Double.NaN, false),
            new RealPointValuePair(new double[] {reflectionX[0], reflectionX[1]}, Double.NaN, false)
        };

        RealPointValuePair result = optimizer.evaluateNewSimplex(original, coeff, null);
        assertEquals(result.getValue(), Double.NaN);

        PointValuePair initialPoint = new PointValuePair(initialX, objectiveFunction.value(initialX));
        optimizer.optimize(objectiveFunction, bounds, initialPoint, GoalType.MINIMIZE);

        RealPointValuePair bestPoint = optimizer.getSolution();
        PointValuePair reflectionPoint = new PointValuePair(reflectionX, objectiveFunction.value(reflectionX));
        RealPointValuePair transformedReflectedPoint = optimizer.getTransformedPoint(bestPoint, reflectionPoint, coeff);
        RealPointValuePair result2 = optimizer.evaluateNewSimplex(original, coeff, null);
        assertEquals(result2.getValue(), transformedReflectedPoint.getValue(), 0.001);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testEvaluateNewSimplexWithFunctionEvaluationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        double[] initialX = {1.0, 2.0};
        double[] reflectionX = {3.0, 4.0};
        double coeff = 1.5;

        ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                throw new FunctionEvaluationException("Function evaluation error");
            }

            @Override
            public int getNumberOfVariables() {
                return initialX.length;
            }
        };

        RealPointValuePair[] original = new RealPointValuePair[] {
            new RealPointValuePair(new double[] {initialX[0], initialX[1]}, Double.NaN, false),
            new RealPointValuePair(new double[] {reflectionX[0], reflectionX[1]}, Double.NaN, false)
        };

        optimizer.evaluateNewSimplex(original, coeff, null);
    }

    @Test(expected = OptimizationException.class)
    public void testEvaluateNewSimplexWithOptimizationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        double[] initialX = {1.0, 2.0};
        double[] reflectionX = {3.0, 4.0};
        double coeff = 1.5;

        ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                throw new OptimizationException("Optimization error");
            }

            @Override
            public int getNumberOfVariables() {
                return initialX.length;
            }
        };

        RealPointValuePair[] original = new RealPointValuePair[] {
            new RealPointValuePair(new double[] {initialX[0], initialX[1]}, Double.NaN, false),
            new RealPointValuePair(new double[] {reflectionX[0], reflectionX[1]}, Double.NaN, false)
        };

        optimizer.evaluateNewSimplex(original, coeff, null);
    }

    @Test
    public void testGetKhi() {
        MultiDirectional optimizer = new MultiDirectional();
        assertEquals(optimizer.getKhi(), 2.0, 0.001);
    }

    @Test
    public void testSetKhi() {
        MultiDirectional optimizer = new MultiDirectional();
        optimizer.setKhi(3.0);
        assertEquals(optimizer.getKhi(), 3.0, 0.001);
    }

    @Test
    public void testGetGamma() {
        MultiDirectional optimizer = new MultiDirectional();
        assertEquals(optimizer.getGamma(), 0.5, 0.001);
    }

    @Test
    public void testSetGamma() {
        MultiDirectional optimizer = new MultiDirectional();
        optimizer.setGamma(0.8);
        assertEquals(optimizer.getGamma(), 0.8, 0.001);
    }
}