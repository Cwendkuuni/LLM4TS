import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    // Create a test object
    MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 0, null);

    // Test setConvergenceChecker() method
    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checker = null;
        optimizer.setConvergenceChecker(checker);
    }

    // Test getConvergenceChecker() method
    @Test
    public void testGetConvergenceChecker() {
        optimizer.getConvergenceChecker();
    }

    // Test getMaxEvaluations() method
    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = optimizer.getMaxEvaluations();
    }

    // Test getEvaluations() method
    @Test
    public void testGetEvaluations() {
        int evaluations = optimizer.getEvaluations();
    }

    // Test setMaxEvaluations() method
    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 100;
        optimizer.setMaxEvaluations(maxEvaluations);
    }

    // Test getOptima() method
    @Test
    public void testGetOptima() {
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
    }

    // Test optimize() method
    @Test
    public void testOptimize1() throws FunctionEvaluationException {
        UnivariateRealFunction f = null;
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;
        optimizer.optimize(f, goal, min, max);
    }

    // Test optimize() method
    @Test
    public void testOptimize2() throws FunctionEvaluationException {
        UnivariateRealFunction f = null;
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;
        optimizer.optimize(f, goal, min, max, startValue);
    }
}