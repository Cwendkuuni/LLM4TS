import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.util.FastMath;

public class MultiStartUnivariateRealOptimizerTest {

    private static class ExampleFunction implements UnivariateRealFunction {

        public double value(double x) {
            return 2 * x;
        }
    }

    private static class MockOptimizer implements BaseUnivariateRealOptimizer<ExampleFunction> {

        private double x;

        public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {}

        public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {return null;}

        public int getMaxEvaluations() {return 0;}

        public int getEvaluations() {return 0;}

        public void setMaxEvaluations(int maxEvaluations) {}

        public UnivariateRealPointValuePair optimize(final ExampleFunction f, final GoalType goal,
                                                     final double min, final double max)
            throws FunctionEvaluationException {
            return new UnivariateRealPointValuePair(f.value(0), 0);
        }

        public UnivariateRealPointValuePair optimize(final ExampleFunction f, final GoalType goal,
                                                     final double min, final double max,
                                                     final double startValue)
            throws FunctionEvaluationException {
            return new UnivariateRealPointValuePair(f.value(0), 0);
        }
    }

    @Test
    public void testGetMaxEvaluations() {
        MockOptimizer optimizer = new MockOptimizer();
        MultiStartUnivariateRealOptimizer<ExampleFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, null);
        multiStartOptimizer.setMaxEvaluations(100);
        assertEquals(100, multiStartOptimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        MockOptimizer optimizer = new MockOptimizer();
        MultiStartUnivariateRealOptimizer<ExampleFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, null);
        assertEquals(0, multiStartOptimizer.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        MockOptimizer optimizer = new MockOptimizer();
        MultiStartUnivariateRealOptimizer<ExampleFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, null);
        multiStartOptimizer.setMaxEvaluations(100);
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetOptima() throws FunctionEvaluationException {
        MockOptimizer optimizer = new MockOptimizer();
        MultiStartUnivariateRealOptimizer<ExampleFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, null);
        UnivariateRealPointValuePair optima = multiStartOptimizer.getOptima()[0];
        assertNull(optima);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaException() {
        MockOptimizer optimizer = new MockOptimizer();
        MultiStartUnivariateRealOptimizer<ExampleFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, null);
        multiStartOptimizer.getOptima();
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException {
        MockOptimizer optimizer = new MockOptimizer();
        MultiStartUnivariateRealOptimizer<ExampleFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, null);
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(new ExampleFunction(), GoalType.MINIMIZE, 0.0, 1.0);
        assertEquals(0.0, result.getValue(), 1e-5);
        assertEquals(0.0, result.getPoint(), 1e-5);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeException() throws FunctionEvaluationException {
        MockOptimizer optimizer = new MockOptimizer();
        MultiStartUnivariateRealOptimizer<ExampleFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, null);
        multiStartOptimizer.optimize(new ExampleFunction(), GoalType.MINIMIZE, 0.0, 1.0, 2.0);
    }

}