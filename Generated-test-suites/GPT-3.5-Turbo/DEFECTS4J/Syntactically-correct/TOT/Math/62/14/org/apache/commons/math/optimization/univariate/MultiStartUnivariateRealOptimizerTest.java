import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer;

    @Before
    public void setUp() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> baseOptimizer = new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
            public UnivariateRealPointValuePair optimize(UnivariateRealFunction univariateRealFunction, GoalType goalType, double v, double v1) throws FunctionEvaluationException {
                return null;
            }

            public UnivariateRealPointValuePair optimize(UnivariateRealFunction univariateRealFunction, GoalType goalType, double v, double v1, double v2) throws FunctionEvaluationException {
                return null;
            }

            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker) {
            }

            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
                return null;
            }

            public int getMaxEvaluations() {
                return 0;
            }

            public int getEvaluations() {
                return 0;
            }

            public void setMaxEvaluations(int i) {
            }
        };
        optimizer = new MultiStartUnivariateRealOptimizer<>(baseOptimizer, 3, null);
    }

    @Test
    public void testOptimize_SingleStart_OptimizerCalledOnce() throws FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double v) {
                return 0;
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        double min = -10;
        double max = 10;

        UnivariateRealPointValuePair result = optimizer.optimize(function, goalType, min, max);

        assertEquals(1, optimizer.getEvaluations());
        assertNotNull(result);
    }

    @Test
    public void testOptimize_MultiStart_OptimizerCalledMultipleTimes() throws FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double v) {
                return 0;
            }
        };

        GoalType goalType = GoalType.MAXIMIZE;
        double min = -10;
        double max = 10;

        UnivariateRealPointValuePair result = optimizer.optimize(function, goalType, min, max, 5.0);

        assertEquals(3, optimizer.getEvaluations());
        assertNotNull(result);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimize_NoConvergence_ExceptionThrown() throws FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double v) {
                return 0;
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        double min = -10;
        double max = 10;

        optimizer.optimize(function, goalType, min, max);
    }
}