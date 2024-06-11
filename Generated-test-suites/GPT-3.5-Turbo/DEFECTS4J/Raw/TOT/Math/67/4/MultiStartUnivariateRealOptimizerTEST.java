import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new MultiStartUnivariateRealOptimizer(new UnivariateRealOptimizerImpl(), 3, new RandomGeneratorImpl());
    }

    @Test
    public void testGetFunctionValue() {
        assertEquals(0.0, optimizer.getFunctionValue(), 0.0);
    }

    @Test
    public void testGetResult() {
        assertEquals(0.0, optimizer.getResult(), 0.0);
    }

    @Test
    public void testGetAbsoluteAccuracy() {
        assertEquals(1e-6, optimizer.getAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testGetIterationCount() {
        assertEquals(0, optimizer.getIterationCount());
    }

    @Test
    public void testGetMaximalIterationCount() {
        assertEquals(Integer.MAX_VALUE, optimizer.getMaximalIterationCount());
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetRelativeAccuracy() {
        assertEquals(1e-6, optimizer.getRelativeAccuracy(), 0.0);
    }

    @Test
    public void testResetAbsoluteAccuracy() {
        optimizer.setAbsoluteAccuracy(0.1);
        optimizer.resetAbsoluteAccuracy();
        assertEquals(1e-6, optimizer.getAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testResetMaximalIterationCount() {
        optimizer.setMaximalIterationCount(100);
        optimizer.resetMaximalIterationCount();
        assertEquals(Integer.MAX_VALUE, optimizer.getMaximalIterationCount());
    }

    @Test
    public void testResetRelativeAccuracy() {
        optimizer.setRelativeAccuracy(0.1);
        optimizer.resetRelativeAccuracy();
        assertEquals(1e-6, optimizer.getRelativeAccuracy(), 0.0);
    }

    @Test
    public void testSetAbsoluteAccuracy() {
        optimizer.setAbsoluteAccuracy(0.1);
        assertEquals(0.1, optimizer.getAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testSetMaximalIterationCount() {
        optimizer.setMaximalIterationCount(100);
        assertEquals(100, optimizer.getMaximalIterationCount());
    }

    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(100);
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testSetRelativeAccuracy() {
        optimizer.setRelativeAccuracy(0.1);
        assertEquals(0.1, optimizer.getRelativeAccuracy(), 0.0);
    }

    @Test
    public void testGetOptima() {
        double[] optima = optimizer.getOptima();
        assertEquals(3, optima.length);
        assertEquals(Double.NaN, optima[0], 0.0);
        assertEquals(Double.NaN, optima[1], 0.0);
        assertEquals(Double.NaN, optima[2], 0.0);
    }

    @Test
    public void testGetOptimaValues() {
        double[] optimaValues = optimizer.getOptimaValues();
        assertEquals(3, optimaValues.length);
        assertEquals(Double.NaN, optimaValues[0], 0.0);
        assertEquals(Double.NaN, optimaValues[1], 0.0);
        assertEquals(Double.NaN, optimaValues[2], 0.0);
    }

    @Test
    public void testOptimize() {
        double result = optimizer.optimize(new UnivariateRealFunctionImpl(), GoalType.MINIMIZE, 0.0, 1.0);
        assertEquals(0.0, result, 0.0);
        assertEquals(3, optimizer.getIterationCount());
        assertEquals(30, optimizer.getEvaluations());

        double[] optima = optimizer.getOptima();
        assertEquals(3, optima.length);
        assertEquals(0.0, optima[0], 0.0);
        assertEquals(Double.NaN, optima[1], 0.0);
        assertEquals(Double.NaN, optima[2], 0.0);

        double[] optimaValues = optimizer.getOptimaValues();
        assertEquals(3, optimaValues.length);
        assertEquals(0.0, optimaValues[0], 0.0);
        assertEquals(Double.NaN, optimaValues[1], 0.0);
        assertEquals(Double.NaN, optimaValues[2], 0.0);
    }

    @Test
    public void testOptimize_startValue() {
        double result = optimizer.optimize(new UnivariateRealFunctionImpl(), GoalType.MAXIMIZE, 0.0, 1.0, 0.5);
        assertEquals(1.0, result, 0.0);
        assertEquals(3, optimizer.getIterationCount());
        assertEquals(30, optimizer.getEvaluations());

        double[] optima = optimizer.getOptima();
        assertEquals(3, optima.length);
        assertEquals(1.0, optima[0], 0.0);
        assertEquals(Double.NaN, optima[1], 0.0);
        assertEquals(Double.NaN, optima[2], 0.0);

        double[] optimaValues = optimizer.getOptimaValues();
        assertEquals(3, optimaValues.length);
        assertEquals(1.0, optimaValues[0], 0.0);
        assertEquals(Double.NaN, optimaValues[1], 0.0);
        assertEquals(Double.NaN, optimaValues[2], 0.0);
    }

    @Test(expected = OptimizationException.class)
    public void testOptimize_noConvergence() {
        optimizer.optimize(new UnivariateRealFunctionImpl(), GoalType.MAXIMIZE, 0.0, 1.0);
    }
}