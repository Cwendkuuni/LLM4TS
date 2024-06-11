import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    // Test optimize method
    @Test
    public void testOptimize() throws Exception {
        // Create the optimizer
        BaseUnivariateRealOptimizer<FUNC> optimizer = new MockOptimizer();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<FUNC>(optimizer, 5, new RandomGenerator());

        // Create the objective function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        };

        // Run the optimize method
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;
        multiStartOptimizer.optimize(f, goal, min, max);

        // Verify the result
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        for (int i = 0; i < optima.length; i++) {
            if (optima[i] != null) {
                assert (optima[i].getValue() >= min && optima[i].getValue() <= max);
            }
        }
    }

    // Mock Optimizer class for testing
    private class MockOptimizer implements BaseUnivariateRealOptimizer<FUNC> {

        private int evaluations;

        public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
        }

        public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
            return null;
        }

        public int getMaxEvaluations() {
            return 0;
        }

        public int getEvaluations() {
            return evaluations;
        }

        public void setMaxEvaluations(int maxEvaluations) {
        }

        public UnivariateRealPointValuePair optimize(FUNC f, GoalType goal, double min, double max)
                throws FunctionEvaluationException {
            evaluations++;
            return new UnivariateRealPointValuePair(0.0, 0.0);
        }
    }
}