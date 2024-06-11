package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeThreshold;
import org.apache.commons.math3.optim.nonlinear.scalar.MaxEvalMaxIter;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleBoundingMethod;
import org.apache.commons.math3.optim.nonlinear.scalar.goaltype.Maximize;
import org.apache.commons.math3.optim.nonlinear.scalar.initialguess.Average;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.AbstractUnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.valuebound.ClosedInterval;
import org.apache.commons.math3.optim.initialguess.NondominatedSorting;
import org.apache.commons.math3.optim.nonlinear.scalar.goaltype.Minimize;

import org.apache.commons.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;

public class MultiStartUnivariateRealOptimizerTest {

    @SuppressWarnings("unchecked")
    private static final UnivariateFunction FUNC = new AbstractUnivariateFunction("Func") {
        public double value(double x) {
            return Math.pow(x, 2) + Math.sin(10 * x) + 1;
        }
    };

    @Test
    public void testOptimizer() throws Exception {
        BaseUnivariateRealOptimizer<UnivariateFunction> singleOptimizer = new SimpleBoundingMethod(new Maximize(1), new Average(1), new ClosedInterval(0, 10), new MaxEval(100), new GoalTypeThreshold(1e-6));
        MultiStartUnivariateRealOptimizer<UnivariateFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(singleOptimizer, 10, new Random());

        PointValuePair best = multiStartOptimizer.optimize(FUNC, new Minimize(1), 0, 10);
        assertNull(best);

        PointValuePair[] optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new PointValuePair[]{null}, optima);
    }

    @Test
    public void testOptimizerWithConvergenceChecker() throws Exception {
        BaseUnivariateRealOptimizer<UnivariateFunction> singleOptimizer = new SimpleBoundingMethod(new Maximize(1), new Average(1), new ClosedInterval(0, 10), new MaxEval(100), new GoalTypeThreshold(1e-6));

        // Set a simple convergence checker that always rejects
        ConvergenceChecker<PointValuePair> convergenceChecker = new BaseConvergenceChecker<PointValuePair>() {
            @Override
            public boolean test(PointValuePair point) {
                return false;
            }
        };
        singleOptimizer.setConvergenceChecker(convergenceChecker);

        MultiStartUnivariateRealOptimizer<UnivariateFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(singleOptimizer, 10, new Random());

        PointValuePair best = multiStartOptimizer.optimize(FUNC, new Minimize(1), 0, 10);
        assertNull(best);

        PointValuePair[] optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new PointValuePair[]{null, null, null, null, null, null, null, null, null, null}, optima);

        singleOptimizer.setConvergenceChecker(null);
    }

    @Test
    public void testOptimizerWithDifferentFunctionEvaluation() throws Exception {
        BaseUnivariateRealOptimizer<UnivariateFunction> singleOptimizer = new SimpleBoundingMethod(new Maximize(1), new Average(1), new ClosedInterval(0, 10), new MaxEval(100), new GoalTypeThreshold(1e-6));

        MultiStartUnivariateRealOptimizer<UnivariateFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(singleOptimizer, 10, new Random());

        PointValuePair best = multiStartOptimizer.optimize(FUNC, new Minimize(1), 0, 10);
        assertNull(best);

        PointValuePair[] optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new PointValuePair[]{null, null, null, null, null, null, null, null, null, null}, optima);

        int maxEvaluations = 5;
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        singleOptimizer.setMaxEvaluations(maxEvaluations);

        best = multiStartOptimizer.optimize(new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 0}), new Minimize(1), 0, 1);
        assertEquals(1d, best.getPoint()[0], 1e-6);
        assertEquals(0d, best.getValue(), 1e-6);

        optima = multiStartOptimizer.getOptima();
        PointValuePair[] expectedOptima = new PointValuePair[10];
        expectedOptima[0] = new PointValuePair(1d, 0d);
        expectedOptima[1] = new PointValuePair(0d, 0d);
        // ... the rest of the optima are not interesting for this test

        assertArrayEquals(expectedOptima, optima);
    }
}