import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.FunctionEvaluationException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.util.FastMath;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {
    
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    private final double EPSILON = 1e-10;

    @Before
    public void setup() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> underlyingOptimizer = new UnivariateRealOptimizerStub();
        optimizer = new MultiStartUnivariateRealOptimizer<>(underlyingOptimizer, 3, new RandomGeneratorStub());
        ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker = createConvergenceCheckerStub();
        optimizer.setConvergenceChecker(convergenceChecker);
    }

    @Test
    public void testOptimize_SingleStart() throws FunctionEvaluationException {
        UnivariateRealFunction f = createTestFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;

        UnivariateRealPointValuePair result = optimizer.optimize(f, goal, min, max, startValue);

        assertNotNull(result);
        assertEquals(0.0, result.getValue(), EPSILON);
        assertEquals(startValue, result.getPoint(), EPSILON);
    }

    @Test
    public void testOptimize_MultiStart() throws FunctionEvaluationException {
        UnivariateRealFunction f = createTestFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;

        UnivariateRealPointValuePair result = optimizer.optimize(f, goal, min, max);

        assertNotNull(result);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimize_NoConvergence() throws FunctionEvaluationException {
        UnivariateRealFunction f = createTestFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;

        optimizer.optimize(f, goal, min, max);
        optimizer.optimize(f, goal, min, max);
        optimizer.optimize(f, goal, min, max);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptima_NotCalled() {
        optimizer.getOptima();
    }

    @Test
    public void testGetOptima_Called() throws FunctionEvaluationException {
        UnivariateRealFunction f = createTestFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;

        optimizer.optimize(f, goal, min, max);

        assertNotNull(optimizer.getOptima());
        assertEquals(3, optimizer.getOptima().length);
    }

    private UnivariateRealFunction createTestFunction() {
        return new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return FastMath.pow((x-1), 2);
            }
        };
    }

    private ConvergenceChecker<UnivariateRealPointValuePair> createConvergenceCheckerStub() {
        return new ConvergenceChecker<UnivariateRealPointValuePair>() {
            public boolean converged(int iteration, UnivariateRealPointValuePair previous,
                                     UnivariateRealPointValuePair current) {
                return false;
            }
        };
    }
}

class UnivariateRealOptimizerStub implements BaseUnivariateRealOptimizer<UnivariateRealFunction> {

    private int maxEvaluations;
    private int evaluations;

    @Override
    public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
    }

    @Override
    public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
        return null;
    }

    @Override
    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    @Override
    public int getEvaluations() {
        return evaluations;
    }

    @Override
    public void setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    @Override
    public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException {
        evaluations++;
        return new UnivariateRealPointValuePair(0.0, 0.0);
    }
}

class RandomGeneratorStub implements RandomGenerator {

    @Override
    public void setSeed(int seed) {
    }

    @Override
    public void setSeed(int[] seed) {
    }

    @Override
    public void setSeed(long seed) {
    }

    @Override
    public void nextBytes(byte[] bytes) {
    }

    @Override
    public int nextInt() {
        return 0;
    }

    @Override
    public int nextInt(int n) {
        return 0;
    }

    @Override
    public long nextLong() {
        return 0;
    }

    @Override
    public boolean nextBoolean() {
        return false;
    }

    @Override
    public float nextFloat() {
        return 0;
    }

    @Override
    public double nextDouble() {
        return 0;
    }

    @Override
    public double nextGaussian() {
        return 0;
    }
}