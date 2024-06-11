package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.goal.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeCriteria;
import org.apache.commons.math3.optim.nonlinear.scalar.multistart.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.multistart.RandomStartStrategy;
import org.apache.commons.math3.special.E;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartUnivariateRealOptimizer() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-E.EULER) * Math.sin(Math.PI * x / 2.0) * Math.sin(Math.PI * x / 2.0);
            }

            @Override
            public int getDimension() {
                return 1;
            }
        };

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleUnivariateRealOptimizer(function),
                10,
                new RandomStartStrategy()
        );

        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        PointValuePair solution = optimizer.optimize(function, goalType, min, max);

        assertEquals(0.7415677585582231, solution.getPoint(), 1e-5);
        assertEquals(-0.5641895340053835, solution.getValue(), 1e-5);

        assertArrayEquals(new double[]{0.7415677585582231, -0.5641895340053835, 0.5641895340053835}, optimizer.getOptima(), 1e-5);
        assertArrayEquals(new double[]{-0.5641895340053835, 0.5641895340053835, -0.5641895340053835}, optimizer.getOptimaValues(), 1e-5);

        // test with maxima
        GoalType goalTypeMax = GoalType.MAXIMIZE;
        PointValuePair solutionMax = optimizer.optimize(function, goalTypeMax, min, max);
        assertEquals(2.405753822121202, solutionMax.getPoint(), 1e-5);
        assertEquals(2.405753822121202, solutionMax.getValue(), 1e-5);

        // test with initial guess
        optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleUnivariateRealOptimizer(function),
                10,
                new RandomStartStrategy(),
                1.0
        );
        solution = optimizer.optimize(function, goalType, min, max);
        assertEquals(0.7415677585582231, solution.getPoint(), 1e-5);
        assertEquals(-0.5641895340053835, solution.getValue(), 1e-5);

        // test with max iterations
        optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleUnivariateRealOptimizer(function),
                10,
                10,
                5
        );
        try {
            optimizer.optimize(function, goalType, min, max);
            fail("Should have thrown an exception");
        } catch (MaxEval e) {
            // expected
        }

        // test with max evaluations
        optimizer = new MultiStartUnivariateRealOptimizer(
                new SimpleUnivariateRealOptimizer(function),
                10,
                new RandomStartStrategy(),
                100
        );
        optimizer.setMaxEvaluations(2);
        solution = optimizer.optimize(function, goalType, min, max);
        assertEquals(0.7415677585582231, solution.getPoint(), 1e-5);
        assertEquals(-0.5641895340053835, solution.getValue(), 1e-5);

        // test with convergence exception
        UnivariateFunction functionException = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new ArithmeticException("Test exception");
            }

            @Override
            public int getDimension() {
                return 1;
            }
        };
        MultiStartUnivariateRealOptimizer optimizerException = new MultiStartUnivariateRealOptimizer(
                new SimpleUnivariateRealOptimizer(functionException),
                10,
                new RandomStartStrategy()
        );
        try {
            optimizerException.optimize(functionException, goalType, min, max);
            fail("Should have thrown an exception");
        } catch (ConvergenceException e) {
            // expected
        }

        // test with function evaluation exception
        UnivariateFunction functionEvalException = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Test exception");
            }

            @Override
            public int getDimension() {
                return 1;
            }
        };
        MultiStartUnivariateRealOptimizer optimizerEvalException = new MultiStartUnivariateRealOptimizer(
                new SimpleUnivariateRealOptimizer(functionEvalException),
                10,
                new RandomStartStrategy()
        );
        try {
            optimizerEvalException.optimize(functionEvalException, goalType, min, max);
            fail("Should have thrown an exception");
        } catch (FunctionEvaluationException e) {
            // expected
        }

    }

}

class SimpleUnivariateRealOptimizer implements UnivariateRealOptimizer {

    private final UnivariateFunction function;

    public SimpleUnivariateRealOptimizer(UnivariateFunction function) {
        this.function = function;
    }

    @Override
    public double getFunctionValue() {
        return function.value(getResult());
    }

    @Override
    public double getResult() {
        throw new UnsupportedOperationException("This method is not supported");
    }

    @Override
    public double getAbsoluteAccuracy() {
        return 1e-6;
    }

    @Override
    public int getIterationCount() {
        return 0;
    }

    @Override
    public int getMaximalIterationCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getMaxEvaluations() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getEvaluations() {
        return 0;
    }

    @Override
    public double getRelativeAccuracy() {
        return 1e-6;
    }

    @Override
    public void resetAbsoluteAccuracy() {
        // empty
    }

    @Override
    public void resetMaximalIterationCount() {
        // empty
    }

    @Override
    public void resetRelativeAccuracy() {
        // empty
    }

    @Override
    public void setAbsoluteAccuracy(double accuracy) {
        // empty
    }

    @Override
    public void setMaximalIterationCount(int count) {
        // empty
    }

    @Override
    public void setMaxEvaluations(int maxEvaluations) {
        // empty
    }

    @Override
    public void setRelativeAccuracy(double accuracy) {
        // empty
    }

    @Override
    public double optimize(UnivariateRealFunction function, GoalType goalType, double min, double max) throws ConvergenceException, FunctionEvaluationException {
        double x = min;
        for (int i = 0; i < 1000; i++) {
            double fx = function.value(x);
            if (goalType == GoalType.MINIMIZE && fx < getFunctionValue() ||
                    goalType == GoalType.MAXIMIZE && fx > getFunctionValue()) {
                setResult(x);
                return x;
            }
            x += 0.1;
        }
        throw new ConvergenceException("Did not converge within 1000 iterations");
    }

    @Override
    public double optimize(UnivariateRealFunction function, GoalType goalType, double min, double max, double startValue) throws ConvergenceException, FunctionEvaluationException {
        setResult(startValue);
        return optimize(function, goalType, min, max);
    }
}