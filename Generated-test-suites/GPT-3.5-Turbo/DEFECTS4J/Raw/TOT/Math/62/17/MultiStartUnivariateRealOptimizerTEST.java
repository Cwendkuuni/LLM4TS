import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.FunctionEvaluationException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.RandomDataGenerator;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.util.FastMath;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    private UnivariateRealTestFunction testFunction;
    private GoalType goal;
    private double min;
    private double max;
    private double startValue;

    @Before
    public void setUp() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> singleStartOptimizer = new DummyUnivariateRealOptimizer();
        RandomGenerator generator = new RandomDataGenerator();
        optimizer = new MultiStartUnivariateRealOptimizer<>(singleStartOptimizer, 5, generator);
        testFunction = new UnivariateRealTestFunction();
        goal = GoalType.MINIMIZE;
        min = -1.0;
        max = 1.0;
        startValue = 0.5;
    }

    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker = new DummyConvergenceChecker<>();
        optimizer.setConvergenceChecker(convergenceChecker);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = 100;
        optimizer.setMaxEvaluations(maxEvaluations);
        assertEquals(maxEvaluations, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() throws FunctionEvaluationException {
        optimizer.optimize(testFunction, goal, min, max, startValue);
        assertEquals(5 * DummyUnivariateRealOptimizer.EVALUATIONS_PER_START, optimizer.getEvaluations());
    }

    @Test
    public void testGetOptima() throws FunctionEvaluationException {
        UnivariateRealPointValuePair optimum1 = optimizer.optimize(testFunction, goal, min, max, startValue);
        UnivariateRealPointValuePair optimum2 = new UnivariateRealPointValuePair(3.0, -1.0);
        UnivariateRealPointValuePair[] optima = new UnivariateRealPointValuePair[]{optimum1, optimum2};
        optimizer.setOptima(optima);
        UnivariateRealPointValuePair[] foundOptima = optimizer.getOptima();
        assertNotNull(foundOptima);
        assertEquals(optima.length, foundOptima.length);
        assertEquals(optima[0], foundOptima[0]);
        assertEquals(optima[1], foundOptima[1]);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaWithNoOptimization() {
        optimizer.getOptima();
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException {
        UnivariateRealPointValuePair optimum = optimizer.optimize(testFunction, goal, min, max, startValue);
        assertNotNull(optimum);
        assertEquals(0.0, optimum.getPoint(), 1e-15);
        assertEquals(3.0, optimum.getValue(), 1e-15);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeThrowsConvergenceException() throws FunctionEvaluationException {
        optimizer.optimize(testFunction, goal, -5.0, -5.0, startValue);
    }

    private class DummyUnivariateRealOptimizer implements BaseUnivariateRealOptimizer<UnivariateRealFunction> {

        public static final int EVALUATIONS_PER_START = 10;

        private int maxEvaluations;

        private ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker;

        @Override
        public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
            this.convergenceChecker = checker;
        }

        @Override
        public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
            return convergenceChecker;
        }

        @Override
        public int getMaxEvaluations() {
            return maxEvaluations;
        }

        @Override
        public void setMaxEvaluations(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        @Override
        public int getEvaluations() {
            return EVALUATIONS_PER_START;
        }

        @Override
        public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException {
            if (min == max) {
                throw new FunctionEvaluationException(LocalizedFormats.SAME_START_END_POINTS, min, max);
            }
            return new UnivariateRealPointValuePair(0.0, 3.0);
        }
    }

    private class DummyConvergenceChecker<T> implements ConvergenceChecker<T> {
        @Override
        public boolean converged(int iteration, T previous, T current) {
            return false;
        }
    }

    private class UnivariateRealTestFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) throws FunctionEvaluationException {
            return FastMath.pow(x, 2) - 1.0;
        }
    }

}