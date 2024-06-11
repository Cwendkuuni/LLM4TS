
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.*;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    private int maxEvaluations = 1000;
    private int starts = 5;

    @Before
    public void setUp() {
        optimizer = new MultiStartUnivariateRealOptimizer<>(new UnivariateRealOptimizerImpl(), starts, null);
    }

    @Test
    public void setConvergenceChecker() {
        assertNull(optimizer.getConvergenceChecker());
        optimizer.setConvergenceChecker(new SimpleUnivariateRealPointChecker(1e-6, 1e-6));
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void getConvergenceChecker() {
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void getMaxEvaluations() {
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void getEvaluations() {
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void setMaxEvaluations() {
        optimizer.setMaxEvaluations(maxEvaluations);
        assertEquals(maxEvaluations, optimizer.getMaxEvaluations());
    }

    @Test
    public void getOptima() {
        assertNull(optimizer.getOptima());
    }

    @Test
    public void optimizeWithBounds() throws FunctionEvaluationException {
        UnivariateRealPointValuePair result = optimizer.optimize(new UnivariateRealFunctionImpl(),
                GoalType.MINIMIZE, -10, 10);
        assertNotNull(result);
        assertEquals(0, result.getPoint(), 1e-6);
    }

    @Test
    public void optimizeWithStartValue() throws FunctionEvaluationException {
        UnivariateRealPointValuePair result = optimizer.optimize(new UnivariateRealFunctionImpl(),
                GoalType.MINIMIZE, -10, 10, 5);
        assertNotNull(result);
        assertEquals(0, result.getPoint(), 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void optimizeWithNoConvergence() throws FunctionEvaluationException {
        optimizer.optimize(new UnivariateRealFunctionImpl(),
                GoalType.MINIMIZE, -10, -5);
    }

    private class UnivariateRealOptimizerImpl implements BaseUnivariateRealOptimizer<UnivariateRealFunction> {
        private int evaluations = 0;

        @Override
        public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {

        }

        @Override
        public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
            return null;
        }

        @Override
        public int getMaxEvaluations() {
            return 0;
        }

        @Override
        public int getEvaluations() {
            return evaluations;
        }

        @Override
        public void setMaxEvaluations(int maxEvaluations) {

        }

        @Override
        public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException, ConvergenceException {
            evaluations++;
            return new UnivariateRealPointValuePair(0, 0);
        }
    }

    private class UnivariateRealFunctionImpl implements UnivariateRealFunction {

        @Override
        public double value(double x) throws FunctionEvaluationException {
            return 0;
        }
    }
}

