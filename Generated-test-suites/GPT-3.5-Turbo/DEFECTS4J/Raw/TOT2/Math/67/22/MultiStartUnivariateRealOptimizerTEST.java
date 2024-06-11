import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    // Testing getFunctionValue method
    @Test
    public void testGetFunctionValue() {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(Mockito.mock(UnivariateRealOptimizer.class), 1, Mockito.mock(RandomGenerator.class));
        assertEquals(Double.NaN, optimizer.getFunctionValue(), 0.001);
    }

    // Testing getResult method
    @Test
    public void testGetResult() {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(Mockito.mock(UnivariateRealOptimizer.class), 1, Mockito.mock(RandomGenerator.class));
        assertEquals(Double.NaN, optimizer.getResult(), 0.001);
    }

    // Testing getAbsoluteAccuracy method
    @Test
    public void testGetAbsoluteAccuracy() {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(Mockito.mock(UnivariateRealOptimizer.class), 1, Mockito.mock(RandomGenerator.class));
        assertEquals(Double.NaN, optimizer.getAbsoluteAccuracy(), 0.001);
    }

    // Testing getIterationCount method
    @Test
    public void testGetIterationCount() {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(Mockito.mock(UnivariateRealOptimizer.class), 1, Mockito.mock(RandomGenerator.class));
        assertEquals(0, optimizer.getIterationCount());
    }

    // Testing getMaximalIterationCount method
    @Test
    public void testGetMaximalIterationCount() {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(Mockito.mock(UnivariateRealOptimizer.class), 1, Mockito.mock(RandomGenerator.class));
        assertEquals(Integer.MAX_VALUE, optimizer.getMaximalIterationCount());
    }

    // Testing getMaxEvaluations method
    @Test
    public void testGetMaxEvaluations() {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(Mockito.mock(UnivariateRealOptimizer.class), 1, Mockito.mock(RandomGenerator.class));
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
    }

    // Testing getEvaluations method
    @Test
    public void testGetEvaluations() {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(Mockito.mock(UnivariateRealOptimizer.class), 1, Mockito.mock(RandomGenerator.class));
        assertEquals(0, optimizer.getEvaluations());
    }

    // Testing optimize method
    @Test
    public void testOptimize() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(Mockito.mock(UnivariateRealOptimizer.class), 1, new JDKRandomGenerator());
        try {
            optimizer.optimize(function, GoalType.MINIMIZE, -1, 1);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            e.printStackTrace();
        }
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();
        assertEquals(0.0, optima[0], 0.001);
        assertEquals(0.0, optimaValues[0], 0.001);
    }
}