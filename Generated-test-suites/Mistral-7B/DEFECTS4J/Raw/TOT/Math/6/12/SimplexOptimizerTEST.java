package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeWeightedSum;
import org.apache.commons.math3.optim.nonlinear.vector.GoalTypeMinVector;
import org.apache.commons.math3.optim.nonlinear.vector.goaltypes.VectorValueType;
import org.apache.commons.math3.optim.nonlinear.vector.multivariate.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.multivariate.SimpleVectorConstraint;
import org.apache.commons.math3.optim.search.AbstractSimplex;
import org.apache.commons.math3.optim.search.BruteforceSimplex;
import org.apache.commons.math3.optim.search. NelderMeadSimplex;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link SimplexOptimizer}.<br>
 * This test file uses a simplex optimizer with a bruteforce simplex and
 * a Nelder-Mead simplex.
 */
public class SimplexOptimizerTest {

    private MultivariateFunction f;
    private SimplexOptimizer opt;

    @Before
    public void init() {
        f = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] - 1.0, 2.0) + Math.pow(point[1] - 1.0, 2.0)
                    + Math.pow(point[2] - 1.0, 2.0);
            }
        };

        opt = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
    }

    @Test
    public void testBruteforce() {
        final double[] startPoint = new double[]{ 1.0, 1.0, 1.0 };
        final GoalType goalType = GoalType.MINIMIZE;
        final int maxEval = 100;

        final OptimizationData data = new OptimizationData();
        data.setInitialGuess(new InitialGuess(startPoint));
        data.setFunction(f);
        data.setGoalType(goalType);

        final AbstractSimplex simplex = new BruteforceSimplex();
        opt.setSimplex(simplexFitnessEvaluator(simplex));

        PointValuePair result = opt.optimize(maxEval, f, goalType, data);

        assertEquals(GoalType.MINIMIZE, goalType);
        assertArrayEquals(new double[]{ 0.0, 0.0, 0.0 }, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testNelderMead() {
        final double[] startPoint = new double[]{ 1.0, 1.0, 1.0 };
        final GoalType goalType = new GoalTypeMinVector(new VectorValueType[]{
            new GoalTypeWeightedSum(new double[]{0.5, 0.5, 0.5})
        });
        final int maxEval = 100;

        final OptimizationData data = new OptimizationData();
        data.setInitialGuess(new InitialGuess(startPoint));
        data.setFunction(f);
        data.setGoalType(goalType);

        final AbstractSimplex simplex = new NelderMeadSimplex();
        opt.setSimplex(simplexFitnessEvaluator(simplex));

        PointValuePair result = opt.optimize(maxEval, f, goalType, data);

        assertEquals(goalType, result.getGoalType());
        assertArrayEquals(new double[]{ 0.0, 0.0, 0.0 }, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        final double[] startPoint = new double[]{ 1.0, 1.0, 1.0 };
        final GoalType goalType = GoalType.MINIMIZE;
        final int maxEval = 100;

        final OptimizationData data = new OptimizationData();
        data.setInitialGuess(new InitialGuess(startPoint));
        data.setFunction(null);
        data.setGoalType(goalType);

        opt.optimize(maxEval, f, goalType, data);
    }

    private AbstractSimplex simplexFitnessEvaluator(AbstractSimplex simplex) {
        return new AbstractSimplex() {
            @Override
            public void build(double[] initialPoint) {
                simplex.build(initialPoint);
            }

            @Override
            public void evaluate(MultivariateFunction function,
                                 Comparator<PointValuePair> comparator) {
                simplex.evaluate(function, comparator);
            }

            @Override
            public void iterate(MultivariateFunction function,
                                Comparator<PointValuePair> comparator) {
                simplex.iterate(function, comparator);
            }
        };
    }
}