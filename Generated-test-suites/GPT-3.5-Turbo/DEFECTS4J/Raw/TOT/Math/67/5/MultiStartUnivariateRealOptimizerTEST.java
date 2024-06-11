import org.apache.commons.math.optimization.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.UnivariateRealOptimizer;
import org.apache.commons.math.FunctionEvaluationException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;
    private UnivariateRealOptimizer singleStartOptimizer;

    @Before
    public void setUp() {
        singleStartOptimizer = new DummyUnivariateRealOptimizer();
        optimizer = new MultiStartUnivariateRealOptimizer(singleStartOptimizer, 3, new DummyRandomGenerator());
    }

    @Test
    public void testGetFunctionValue() throws FunctionEvaluationException {
        double expected = 5.0;
        singleStartOptimizer.setFunctionValue(expected);
        double actual = optimizer.getFunctionValue();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testGetResult() throws FunctionEvaluationException {
        double expected = 10.0;
        singleStartOptimizer.setResult(expected);
        double actual = optimizer.getResult();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testGetAbsoluteAccuracy() {
        double expected = 0.001;
        singleStartOptimizer.setAbsoluteAccuracy(expected);
        double actual = optimizer.getAbsoluteAccuracy();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testGetIterationCount() {
        int expected = 100;
        singleStartOptimizer.setIterationCount(expected);
        int actual = optimizer.getIterationCount();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaximalIterationCount() {
        int expected = 500;
        optimizer.setMaximalIterationCount(expected);
        int actual = optimizer.getMaximalIterationCount();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaxEvaluations() {
        int expected = 1000;
        optimizer.setMaxEvaluations(expected);
        int actual = optimizer.getMaxEvaluations();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetEvaluations() {
        int expected = 5000;
        singleStartOptimizer.setEvaluations(expected);
        int actual = optimizer.getEvaluations();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetRelativeAccuracy() {
        double expected = 0.01;
        singleStartOptimizer.setRelativeAccuracy(expected);
        double actual = optimizer.getRelativeAccuracy();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testSetAbsoluteAccuracy() {
        double expected = 0.001;
        optimizer.setAbsoluteAccuracy(expected);
        double actual = singleStartOptimizer.getAbsoluteAccuracy();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testSetMaximalIterationCount() {
        int expected = 500;
        optimizer.setMaximalIterationCount(expected);
        int actual = optimizer.getMaximalIterationCount();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetMaxEvaluations() {
        int expected = 1000;
        optimizer.setMaxEvaluations(expected);
        int actual = optimizer.getMaxEvaluations();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetRelativeAccuracy() {
        double expected = 0.01;
        optimizer.setRelativeAccuracy(expected);
        double actual = singleStartOptimizer.getRelativeAccuracy();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testGetOptima() {
        int expectedLength = 3;
        double[] expected = new double[expectedLength];
        for (int i = 0; i < expectedLength; i++) {
            expected[i] = i;
        }
        double[] actual = optimizer.getOptima();
        assertArrayEquals(expected, actual, 0.0);
    }

    @Test
    public void testGetOptimaValues() {
        int expectedLength = 3;
        double[] expected = new double[expectedLength];
        for (int i = 0; i < expectedLength; i++) {
            expected[i] = i;
        }
        double[] actual = optimizer.getOptimaValues();
        assertArrayEquals(expected, actual, 0.0);
    }

}