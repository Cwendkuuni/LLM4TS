package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.initializer.ArrayRealVectorInitializer;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.simple.SimpleXOptimizer;
import org.apache.commons.math3.optim.simple.PowellSimplexOptimizer;
import org.apache.commons.math3.optim.simple.NelderMeadSimplexOptimizer;
import org.apache.commons.math3.optim.initializer.SimpleXInitialGuess;
import org.apache.commons.math3.optim.goaltype.Maximize;
import org.apache.commons.math3.optim.goaltype.Minimize;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.PointValuePairList;
import org.apache.commons.math3.optim.maxeval.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;

public class SimplexOptimizerTest {

    @org.junit.Test
    public void testMinimizationSimpleFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return FunctionUtils.pow(FunctionUtils.sum(x), 2);
            }
        };
        PointValuePair bestSolution = new PointValuePair(new double[] {0.0, 0.0}, 0.0);

        SimpleXOptimizer optimizer = new PowellSimplexOptimizer(new SimpleValueChecker(1e-8, 1e-8));
        PointValuePair result = optimizer.optimize(new MaxEval(100), f, new Minimize(), new double[] {1.0, 1.0});
        assertEquals(bestSolution, result);
    }

    @org.junit.Test
    public void testMinimizationFunctionWithMultiplePeaks() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                double sum = FunctionUtils.sum(x);
                return Math.exp(-sum) * Math.sin(sum);
            }
        };
        PointValuePair bestSolution = new PointValuePair(new double[] {-0.5 * Math.PI, -0.5 * Math.PI}, -1.0);

        SimpleXOptimizer optimizer = new NelderMeadSimplexOptimizer(new SimpleValueChecker(1e-8, 1e-8));
        PointValuePair result = optimizer.optimize(new MaxEval(100), f, new Minimize(), new double[] {0.0, 0.0});
        assertEquals(bestSolution, result);
    }

    @org.junit.Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        MultivariateFunction f = null;
        PointValuePair bestSolution = new PointValuePair(new double[] {0.0, 0.0}, 0.0);
        SimpleXOptimizer optimizer = new PowellSimplexOptimizer(new SimpleValueChecker(1e-8, 1e-8));
        optimizer.optimize(new MaxEval(100), f, new Minimize(), new double[] {0.0, 0.0});
    }

    @org.junit.Test(expected = NullArgumentException.class)
    public void testNullInitialGuess() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return FunctionUtils.pow(FunctionUtils.sum(x), 2);
            }
        };
        SimpleXOptimizer optimizer = new PowellSimplexOptimizer(new SimpleValueChecker(1e-8, 1e-8));
        optimizer.optimize(new MaxEval(100), f, new Minimize(), null);
    }

    @org.junit.Test
    public void testMinimizationFunctionWithBounds() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return FunctionUtils.pow(FunctionUtils.sum(x), 2);
            }
        };

        double[] bounds = new double[] {-1.0, -1.0, 1.0, 1.0};
        PointValuePair bestSolution = new PointValuePair(new double[] {0.0, 0.0}, 0.0);

        SimpleXOptimizer optimizer = new PowellSimplexOptimizer(new SimpleValueChecker(1e-8, 1e-8), new ArrayRealVectorInitializer(bounds));
        PointValuePair result = optimizer.optimize(new MaxEval(100), f, new Minimize(), bounds);
        assertEquals(bestSolution, result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return FunctionUtils.pow(FunctionUtils.sum(x), 2);
            }
        };
        SimpleXOptimizer optimizer = new PowellSimplexOptimizer(new SimpleValueChecker(1e-8, 1e-8));
        optimizer.optimize(new MaxEval(100), f, new GoalType[] {GoalType.MAXIMIZE}, new double[] {0.0, 0.0});
    }

    @org.junit.Test
    public void testMultipleCallsToOptimizeWithSameParameters() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return FunctionUtils.pow(FunctionUtils.sum(x), 2);
            }
        };
        PointValuePair bestSolution = new PointValuePair(new double[] {0.0, 0.0}, 0.0);

        SimpleXOptimizer optimizer = new PowellSimplexOptimizer(new SimpleValueChecker(1e-8, 1e-8));
        PointValuePair result1 = optimizer.optimize(new MaxEval(100), f, new Minimize(), new double[] {1.0, 1.0});
        PointValuePair result2 = optimizer.optimize(new MaxEval(100), f, new Minimize(), new double[] {1.0, 1.0});
        assertEquals(bestSolution, result1);
        assertEquals(bestSolution, result2);
    }

    @org.junit.Test
    public void testMaximizationFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return FunctionUtils.pow(FunctionUtils.sum(x), 2);
            }
        };
        PointValuePair bestSolution = new PointValuePair(new double[] {0.0, 0.0}, 0.0);

        SimpleXOptimizer optimizer = new PowellSimplexOptimizer(new SimpleValueChecker(1e-8, 1e-8));
        PointValuePair result = optimizer.optimize(new MaxEval(100), f, new Maximize(), new double[] {1.0, 1.0});
        assertEquals(bestSolution, result);
    }

    @org.junit.Test
    public void testOptimizationData() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return FunctionUtils.pow(FunctionUtils.sum(x), 2);
            }
        };
        PointValuePair bestSolution = new PointValuePair(new double[] {0.0, 0.0}, 0.0);

        SimpleXOptimizer optimizer = new PowellSimplexOptimizer(new SimpleValueChecker(1e-8, 1e-8));
        PointValuePair result = optimizer.optimize(new MaxEval(100), f, new Minimize(), new double[] {1.0, 1.0}, new SimpleXOptimizer.OptimizationData() {{
                setInitialGuess(new SimpleXInitialGuess(new double[] {1.5, 1.5}));
                setSimplex(new SimpleXSimplex());
            }});
        assertEquals(bestSolution, result);
    }

    @org.junit.Test
    public void testMultipleOptimizationsWithDifferentFunctions() {
        MultivariateFunction f1 = new MultivariateFunction() {
            public double value(double[] x) {
                return FunctionUtils.pow(FunctionUtils.sum(x), 2);
            }
        };
        MultivariateFunction f2 = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.exp(-FunctionUtils.sum(x)) * Math.sin(FunctionUtils.sum(x));
            }
        };
        PointValuePair bestSolution1 = new PointValuePair(new double[] {0.0, 0.0}, 0.0);
        PointValuePair bestSolution2 = new PointValuePair(new double[] {-0.5 * Math.PI, -0.5 * Math.PI}, -1.0);

        SimpleXOptimizer optimizer = new PowellSimplexOptimizer(new SimpleValueChecker(1e-8, 1e-8));
        PointValuePair result1 = optimizer.optimize(new MaxEval(100), f1, new Minimize(), new double[] {1.0, 1.0});
        PointValuePair result2 = optimizer.optimize(new MaxEval(100), f2, new Minimize(), new double[] {0.0, 0.0});
        assertEquals(bestSolution1, result1);
        assertEquals(bestSolution2, result2);
    }
}