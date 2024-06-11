package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValuesDifferentiableVectorFunction;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceCheckerFactory;
import org.apache.commons.math3.optim.nonlinear.convergence.SimpleConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.optim.GoalType;
import org.apache.commons.math3.optim.nonlinear.optim.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.optim.PointValuePairList;
import org.apache.commons.math3.optim.nonlinear.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.optim.SimpleOptimizationRequest;
import org.apache.commons.math3.optim.nonlinear.optim.SimpleVectorConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.optim.SimpleVectorOptimizer;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.point.RealPointValuePair;
import org.apache.commons.math3.optim.point.RealVector;
import org.apache.commons.math3.optim.point.VectorValuePair;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateFunctionValues;
import org.apache.commons.math3.optim.univariate.interpolators.SplineInterpolator;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math3.optim.BaseOptimizer;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.convergence.SimpleConvergenceChecker;

public class BaseOptimizerTest {

    private BaseOptimizer<PointValuePair> optimizer;
    private ConvergenceChecker<PointValuePair> convergenceChecker;

    @Before
    public void setUp() {
        optimizer = new SimpleVectorOptimizer<PointValuePair>(getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        // Test default value
        BaseOptimizer<PointValuePair> optimizer = new SimpleVectorOptimizer<PointValuePair>(getConvergenceChecker());
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertTrue(MaxEval.DEFAULT_MAX_EVALS > maxEvaluations);

        // Test value set
        optimizer = new SimpleVectorOptimizer<PointValuePair>(getConvergenceChecker(), 100);
        maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(100, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        // Test initial value
        BaseOptimizer<PointValuePair> optimizer = new SimpleVectorOptimizer<PointValuePair>(getConvergenceChecker());
        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);

        // Test value after optimization
        optimizer.optimize(new SimpleOptimizationRequest<PointValuePair>(new SimpleValuesDifferentiableVectorFunction(
                new SimpleUnivariateFunction("sin", x -> Math.sin(x)), new SimpleBounds(0.0, 10.0), new InitialGuess(1.0),
                GoalType.MINIMIZE), null));
        evaluations = optimizer.getEvaluations();
        int expectedEvaluations = Math.toIntExact(SplineInterpolator.getSplineInterpolator(
                new UnivariateFunctionValues(new UnivariateFunction("sin", x -> Math.sin(x))).getFunctionValues())
                .getInterpolationPoints().size());
        assertEquals(expectedEvaluations, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        // Test default value
        BaseOptimizer<PointValuePair> optimizer = new SimpleVectorOptimizer<PointValuePair>(null);
        ConvergenceChecker<PointValuePair> defaultChecker = optimizer.getConvergenceChecker();
        assertTrue(defaultChecker instanceof SimpleConvergenceChecker);

        // Test value set
        ConvergenceChecker<PointValuePair> customChecker = new SimpleConvergenceChecker(0.001, 10);
        optimizer = new SimpleVectorOptimizer<PointValuePair>(customChecker);
        assertNotNull(optimizer.getConvergenceChecker());
        assertNotSame(defaultChecker, optimizer.getConvergenceChecker());
    }

    private ConvergenceChecker<PointValuePair> getConvergenceChecker() {
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleConvergenceChecker(0.001, 10);
        return convergenceChecker;
    }

    private static class SimpleUnivariateFunction implements UnivariateFunction {
        private final String name;
        private final UnivariateFunction function;

        public SimpleUnivariateFunction(String name, UnivariateFunction function) {
            this.name = name;
            this.function = function;
        }

        public double value(double x) {
            return function.value(x);
        }

        public String toString() {
            return name;
        }
    }
}