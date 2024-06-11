package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.BaseUnivariateOptimizer;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#MultiStartUnivariateRealOptimizer(BaseUnivariateRealOptimizer, int, RandomGenerator)} .
     */
    @Test
    public void testMultiStartUnivariateRealOptimizerConstruction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        BaseUnivariateOptimizer optimizer = new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
            @Override
            public PointValuePair optimize(UnivariateRealFunction fun, GoalType goal, double initialX, double boundary)
                    throws FunctionEvaluationException {
                return new PointValuePair(initialX, fun.value(initialX));
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomGenerator());
        assertNotNull(multiStart);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#setConvergenceChecker(ConvergenceChecker)} .
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetConvergenceChecker() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        BaseUnivariateRealOptimizer optimizer = new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
            @Override
            public PointValuePair optimize(UnivariateRealFunction fun, GoalType goal, double initialX, double boundary)
                    throws FunctionEvaluationException {
                return new PointValuePair(initialX, fun.value(initialX));
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomGenerator());
        multiStart.setConvergenceChecker(null);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getConvergenceChecker()} .
     */
    @Test
    public void testGetConvergenceChecker() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        BaseUnivariateRealOptimizer optimizer = new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
            @Override
            public PointValuePair optimize(UnivariateRealFunction fun, GoalType goal, double initialX, double boundary)
                    throws FunctionEvaluationException {
                return new PointValuePair(initialX, fun.value(initialX));
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomGenerator());
        assertNull(multiStart.getConvergenceChecker());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getMaxEvaluations()} .
     */
    @Test
    public void testGetMaxEvaluations() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        BaseUnivariateRealOptimizer optimizer = new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
            @Override
            public PointValuePair optimize(UnivariateRealFunction fun, GoalType goal, double initialX, double boundary)
                    throws FunctionEvaluationException {
                return new PointValuePair(initialX, fun.value(initialX));
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomGenerator());
        assertEquals(0, multiStart.getMaxEvaluations());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getEvaluations()} .
     */
    @Test
    public void testGetEvaluations() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        BaseUnivariateRealOptimizer optimizer = new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
            @Override
            public PointValuePair optimize(UnivariateRealFunction fun, GoalType goal, double initialX, double boundary)
                    throws FunctionEvaluationException {
                return new PointValuePair(initialX, fun.value(initialX));
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomGenerator());
        assertEquals(0, multiStart.getEvaluations());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#setMaxEvaluations(int)} .
     */
    @Test
    public void testSetMaxEvaluations() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        BaseUnivariateRealOptimizer optimizer = new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
            @Override
            public PointValuePair optimize(UnivariateRealFunction fun, GoalType goal, double initialX, double boundary)
                    throws FunctionEvaluationException {
                return new PointValuePair(initialX, fun.value(initialX));
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomGenerator());
        multiStart.setMaxEvaluations(100);
        assertEquals(100, multiStart.getMaxEvaluations());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)} .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeNullFunction() {
        UnivariateRealFunction f = null;
        BaseUnivariateRealOptimizer optimizer = new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
            @Override
            public PointValuePair optimize(UnivariateRealFunction fun, GoalType goal, double initialX, double boundary)
                    throws FunctionEvaluationException {
                return new PointValuePair(initialX, fun.value(initialX));
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomGenerator());
        multiStart.optimize(f, GoalType.MINIMIZE, 0.0, 1.0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)} .
     */
    @Test
    public void testOptimizeSimpleFunction() throws ConvergenceException, FunctionEvaluationException, InterruptedException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        BaseUnivariateRealOptimizer optimizer = new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
            @Override
            public PointValuePair optimize(UnivariateRealFunction fun, GoalType goal, double initialX, double boundary)
                    throws FunctionEvaluationException {
                return new PointValuePair(initialX, fun.value(initialX));
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomGenerator());
        double[] expected = {1.0, 4.0, 9.0, 16.0, 25.0, 36.0, 49.0, 64.0, 81.0, 100.0};
        for (int i = 0; i < 10; i++) {
            PointValuePair result = multiStart.optimize(f, GoalType.MINIMIZE, 0.0, 100.0);
            assertEquals(expected[i], result.getPoint(), 1e-8);
            assertEquals(expected[i] * expected[i], result.getValue(), 1e-8);
        }
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)} .
     */
    @Test
    public void testOptimizeSimpleFunctionWithStartValue() throws ConvergenceException, FunctionEvaluationException, InterruptedException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        BaseUnivariateRealOptimizer optimizer = new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
            @Override
            public PointValuePair optimize(UnivariateRealFunction fun, GoalType goal, double initialX, double boundary)
                    throws FunctionEvaluationException {
                return new PointValuePair(initialX, fun.value(initialX));
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomGenerator());
        double[] expected = {4.0, 9.0, 16.0, 25.0, 36.0, 49.0, 64.0, 81.0, 100.0, 121.0};
        for (int i = 0; i < 10; i++) {
            double startValue = i * 2.0;
            PointValuePair result = multiStart.optimize(f, GoalType.MINIMIZE, startValue, 100.0, startValue);
            assertEquals(expected[i], result.getPoint(), 1e-8);
            assertEquals(expected[i] * expected[i], result.getValue(), 1e-8);
        }
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptima()} .
     */
    @Test
    public void testGetOptima() throws ConvergenceException, FunctionEvaluationException, InterruptedException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        BaseUnivariateRealOptimizer optimizer = new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
            @Override
            public PointValuePair optimize(UnivariateRealFunction fun, GoalType goal, double initialX, double boundary)
                    throws FunctionEvaluationException {
                return new PointValuePair(initialX, fun.value(initialX));
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomGenerator());
        PointValuePair[] expected = new PointValuePair[10];
        for (int i = 0; i < 10; i++) {
            PointValuePair result = multiStart.optimize(f, GoalType.MINIMIZE, 0.0, 100.0);
            expected[i] = result;
        }
        PointValuePair[] optima = multiStart.getOptima();
        assertArrayEquals(expected, optima);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)} with maxEvaluations.
     */
    @Test(expected = MaxEvalExceededException.class)
    public void testOptimizeWithMaxEvaluations() throws FunctionEvaluationException, InterruptedException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.log(Math.abs(x - 1.0));
            }
        };
        BaseUnivariateRealOptimizer optimizer = new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
            @Override
            public PointValuePair optimize(UnivariateRealFunction fun, GoalType goal, double initialX, double boundary)
                    throws FunctionEvaluationException {
                return new PointValuePair(initialX, fun.value(initialX));
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomGenerator());
        multiStart.setMaxEvaluations(1);
        multiStart.optimize(f, GoalType.MINIMIZE, 0.0, 1.0);
    }

}