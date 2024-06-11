package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.junit.Test;

import org.apache.commons.cli.OptimizerTestUtils;
import org.apache.commons.cli.MultiStartUnivariateRealOptimizer;
import org.apache.commons.cli.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.NonUnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.NDerivativeUnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.Gradient;
import org.apache.commons.math3.optim.nonlinear.scalar.RidgeFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BroydenFletcherGoldfarbShanno;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMinimizationMultiStart() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(2, 3, new double[]{-1., 1., 1.});
        BaseUnivariateRealOptimizer optimizer = new BroydenFletcherGoldfarbShanno();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> mso = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new java.util.Random());
        mso.setMaxEvaluations(100);
        mso.optimize(f, GoalType.MINIMIZE, -10., 10.);
        PointValuePair[] optima = mso.getOptima();
        assertArrayEquals(new PointValuePair[]{new PointValuePair(0.4779762813826361, 0.1220463832192265),
                new PointValuePair(1.123584140658743, 0.0366597794808777),
                new PointValuePair(2.472162022985662, 0.0114464481765416)}, optima);
    }

    @Test
    public void testMaximizationMultiStart() throws Exception {
        UnivariateRealFunction f = new RidgeFunction(2);
        BaseUnivariateRealOptimizer optimizer = new BroydenFletcherGoldfarbShanno();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> mso = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new java.util.Random());
        mso.setMaxEvaluations(100);
        mso.optimize(f, GoalType.MAXIMIZE, -10., 10.);
        PointValuePair[] optima = mso.getOptima();
        assertArrayEquals(new PointValuePair[]{new PointValuePair(-0.6956730633344533, 3.1622776601683796),
                new PointValuePair(-1.228426601856143, 3.366893685352858),
                new PointValuePair(-1.7611791393778325, 3.561409606539348)}, optima);
    }

    @Test
    public void testNonConvergenceMultiStart() throws Exception {
        UnivariateRealFunction f = new NDerivativeUnivariateFunction(new RidgeFunction(2), 1);
        BaseUnivariateRealOptimizer optimizer = new BroydenFletcherGoldfarbShanno();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> mso = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new java.util.Random());
        mso.setMaxEvaluations(100);
        try {
            mso.optimize(f, GoalType.MINIMIZE, -10., 10.);
            assertFalse(true);
        } catch (ConvergenceException e) {
            assertNull(mso.getOptima());
        }
    }

    @Test
    public void testMinimizationWithCustomGoalTypeMultiStart() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(2, 3, new double[]{-1., 1., 1.});
        BaseUnivariateRealOptimizer optimizer = new BroydenFletcherGoldfarbShanno();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> mso = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new java.util.Random());
        GoalType goal = new GoalType() {
            @Override
            public String getGoalTypeName() {
                return "MINIMIZE_WITH_CUSTOM_NAME";
            }
        };
        mso.setMaxEvaluations(100);
        mso.optimize(f, goal, -10., 10.);
        PointValuePair[] optima = mso.getOptima();
        assertArrayEquals(new PointValuePair[]{new PointValuePair(0.4779762813826361, 0.1220463832192265),
                new PointValuePair(1.123584140658743, 0.0366597794808777),
                new PointValuePair(2.472162022985662, 0.0114464481765416)}, optima);
    }

    @Test
    public void testMaximizationWithCustomGoalTypeMultiStart() throws Exception {
        UnivariateRealFunction f = new RidgeFunction(2);
        BaseUnivariateRealOptimizer optimizer = new BroydenFletcherGoldfarbShanno();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> mso = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new java.util.Random());
        GoalType goal = new GoalType() {
            @Override
            public String getGoalTypeName() {
                return "MAXIMIZE_WITH_CUSTOM_NAME";
            }
        };
        mso.setMaxEvaluations(100);
        mso.optimize(f, goal, -10., 10.);
        PointValuePair[] optima = mso.getOptima();
        assertArrayEquals(new PointValuePair[]{new PointValuePair(-0.6956730633344533, 3.1622776601683796),
                new PointValuePair(-1.228426601856143, 3.366893685352858),
                new PointValuePair(-1.7611791393778325, 3.561409606539348)}, optima);
    }

    @Test
    public void testCustomInitialGuessMultiStart() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(2, 3, new double[]{-1., 1., 1.});
        BaseUnivariateRealOptimizer optimizer = new BroydenFletcherGoldfarbShanno();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> mso = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new java.util.Random());
        mso.setMaxEvaluations(100);
        InitialGuess initialGuess = new InitialGuess() {
            @Override
            public double[] getInitialGuess(int dimension) {
                double[] init = new double[dimension];
                init[0] = 2.5;
                return init;
            }
        };
        mso.setInitialGuess(initialGuess);
        mso.optimize(f, GoalType.MINIMIZE, -10., 10.);
        PointValuePair[] optima = mso.getOptima();
        assertArrayEquals(new PointValuePair[]{new PointValuePair(0.4779762813826361, 0.1220463832192265),
                new PointValuePair(1.123584140658743, 0.0366597794808777),
                new PointValuePair(2.472162022985662, 0.0114464481765416)}, optima);
    }

    @Test
    public void testCustomConvergenceCheckerMultiStart() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(2, 3, new double[]{-1., 1., 1.});
        BaseUnivariateRealOptimizer optimizer = new BroydenFletcherGoldfarbShanno();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> mso = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new java.util.Random());
        mso.setMaxEvaluations(100);
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean test(PointValuePair pointValuePair) {
                return pointValuePair.getValue() < 0.01;
            }
        };
        mso.setConvergenceChecker(convergenceChecker);
        mso.optimize(f, GoalType.MINIMIZE, -10., 10.);
        PointValuePair[] optima = mso.getOptima();
        assertArrayEquals(new PointValuePair[]{new PointValuePair(0.4779762813826361, 0.1220463832192265),
                new PointValuePair(1.123584140658743, 0.0366597794808777),
                new PointValuePair(2.472162022985662, 0.0114464481765416)}, optima);
    }
}