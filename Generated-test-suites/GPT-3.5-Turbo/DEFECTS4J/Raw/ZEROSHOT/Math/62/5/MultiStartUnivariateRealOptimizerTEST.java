import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer;

    @Before
    public void setUp() {
        optimizer = new MultiStartUnivariateRealOptimizer<>(new BaseUnivariateRealOptimizerMock(), 3, null);
    }

    @Test
    public void testSetConvergenceChecker() {
        assertNull(optimizer.getConvergenceChecker());

        ConvergenceChecker<UnivariateRealPointValuePair> checker = new ConvergenceCheckerMock<>();
        optimizer.setConvergenceChecker(checker);

        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(0, optimizer.getMaxEvaluations());

        optimizer.setMaxEvaluations(100);

        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testOptimize() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunctionMock();
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;

        UnivariateRealPointValuePair result = optimizer.optimize(f, goal, min, max);

        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptima() throws Exception {
        optimizer.getOptima();
    }

    private static class BaseUnivariateRealOptimizerMock implements BaseUnivariateRealOptimizer<UnivariateRealFunction> {

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
            return 0;
        }

        @Override
        public void setMaxEvaluations(int maxEvaluations) {

        }

        @Override
        public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) throws Exception {
            return new UnivariateRealPointValuePair(0.0, 0.0);
        }
    }

    private static class UnivariateRealFunctionMock implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return 0.0;
        }
    }

    private static class ConvergenceCheckerMock<T> implements ConvergenceChecker<T> {

        @Override
        public boolean converged(int iteration, T previous, T current) {
            return false;
        }
    }
}