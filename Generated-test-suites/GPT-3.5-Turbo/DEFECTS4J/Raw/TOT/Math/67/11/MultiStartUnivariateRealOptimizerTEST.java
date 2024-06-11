import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.UnivariateRealOptimizer;
import org.apache.commons.math.random.RandomGenerator;
import org.mockito.Mockito;

public class MultiStartUnivariateRealOptimizerTest {

    // Test getMaximalIterationCount method
    @Test 
    public void testGetMaximalIterationCount() {
        UnivariateRealOptimizer optimizer = Mockito.mock(UnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, null);
        
        assertEquals(Integer.MAX_VALUE, multiStartOptimizer.getMaximalIterationCount());
    }

    // Test setMaximalIterationCount method
    @Test
    public void testSetMaximalIterationCount() {
        UnivariateRealOptimizer optimizer = Mockito.mock(UnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, null);

        multiStartOptimizer.setMaximalIterationCount(10);
        assertEquals(10, multiStartOptimizer.getMaximalIterationCount());
    }
    
    // Test getMaxEvaluations method
    @Test
    public void testGetMaxEvaluations() {
        UnivariateRealOptimizer optimizer = Mockito.mock(UnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, null);
        
        assertEquals(Integer.MAX_VALUE, multiStartOptimizer.getMaxEvaluations());
    }

    // Test setMaxEvaluations method
    @Test
    public void testSetMaxEvaluations() {
        UnivariateRealOptimizer optimizer = Mockito.mock(UnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, null);

        multiStartOptimizer.setMaxEvaluations(1000);
        assertEquals(1000, multiStartOptimizer.getMaxEvaluations());
    }

    // Test resetMaximalIterationCount method
    @Test
    public void testResetMaximalIterationCount() {
        UnivariateRealOptimizer optimizer = Mockito.mock(UnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, null);

        multiStartOptimizer.setMaximalIterationCount(10);
        multiStartOptimizer.resetMaximalIterationCount();
        assertEquals(Integer.MAX_VALUE, multiStartOptimizer.getMaximalIterationCount());
    }

    // Test optimize method
    @Test
    public void testOptimize() {
        UnivariateRealOptimizer optimizer = Mockito.mock(UnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, null);

        // Set up the mock object
        Mockito.when(optimizer.optimize(Mockito.any(UnivariateRealFunction.class), Mockito.any(), Mockito.anyDouble(), Mockito.anyDouble())).thenReturn(2.0);
        Mockito.when(optimizer.getFunctionValue()).thenReturn(1.0);
        Mockito.when(optimizer.getIterationCount()).thenReturn(10);
        Mockito.when(optimizer.getEvaluations()).thenReturn(100);

        // Call the method under test
        double result = multiStartOptimizer.optimize(null, null, 0.0, 10.0);

        // Check the result
        assertEquals(2.0, result, 0.001);
        assertEquals(10, multiStartOptimizer.getIterationCount());
        assertEquals(100, multiStartOptimizer.getEvaluations());
    }

    // Test resetAbsoluteAccuracy method
    @Test
    public void testResetAbsoluteAccuracy() {
        UnivariateRealOptimizer optimizer = Mockito.mock(UnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, null);

        multiStartOptimizer.resetAbsoluteAccuracy();
        Mockito.verify(optimizer).resetAbsoluteAccuracy();
    }

    // Test resetRelativeAccuracy method
    @Test
    public void testResetRelativeAccuracy() {
        UnivariateRealOptimizer optimizer = Mockito.mock(UnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, null);

        multiStartOptimizer.resetRelativeAccuracy();
        Mockito.verify(optimizer).resetRelativeAccuracy();
    }

    // Test setAbsoluteAccuracy method
    @Test
    public void testSetAbsoluteAccuracy() {
        UnivariateRealOptimizer optimizer = Mockito.mock(UnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, null);

        multiStartOptimizer.setAbsoluteAccuracy(0.001);
        Mockito.verify(optimizer).setAbsoluteAccuracy(0.001);
    }

    // Test setRelativeAccuracy method
    @Test
    public void testSetRelativeAccuracy() {
        UnivariateRealOptimizer optimizer = Mockito.mock(UnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, null);

        multiStartOptimizer.setRelativeAccuracy(0.01);
        Mockito.verify(optimizer).setRelativeAccuracy(0.01);
    }
}