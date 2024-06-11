import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.random.RandomGenerator;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;
    private UnivariateRealFunction function;
    private RandomGenerator generator;
    
    @Before
    public void setUp() {
        // create a single-start optimizer to wrap
        optimizer = new MultiStartUnivariateRealOptimizer(new UnivariateRealOptimizer(), 3, new RandomGenerator());
        
        // create a dummy function
        function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        
        // create a random generator
        generator = new RandomGenerator();
    }
    
    @Test
    public void testGetFunctionValue() {
        // test if the function value is correct
        double result = optimizer.getFunctionValue();
        assertEquals(0.0, result, 0.0);
    }
    
    @Test
    public void testGetResult() {
        // test if the result is correct
        double result = optimizer.getResult();
        assertEquals(0.0, result, 0.0);
    }
    
    @Test
    public void testGetAbsoluteAccuracy() {
        // test if the absolute accuracy is correct
        double accuracy = optimizer.getAbsoluteAccuracy();
        assertEquals(0.0, accuracy, 0.0);
    }
    
    @Test
    public void testGetIterationCount() {
        // test if the iteration count is correct
        int count = optimizer.getIterationCount();
        assertEquals(0, count);
    }
    
    @Test
    public void testGetMaximalIterationCount() {
        // test if the maximal iteration count is correct
        int count = optimizer.getMaximalIterationCount();
        assertEquals(Integer.MAX_VALUE, count);
    }
    
    @Test
    public void testGetMaxEvaluations() {
        // test if the max evaluations is correct
        int evaluations = optimizer.getMaxEvaluations();
        assertEquals(Integer.MAX_VALUE, evaluations);
    }
    
    @Test
    public void testGetEvaluations() {
        // test if the evaluations is correct
        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);
    }
    
    @Test
    public void testGetRelativeAccuracy() {
        // test if the relative accuracy is correct
        double accuracy = optimizer.getRelativeAccuracy();
        assertEquals(0.0, accuracy, 0.0);
    }
    
    @Test
    public void testResetAbsoluteAccuracy() {
        // test if resetting the absolute accuracy works
        optimizer.resetAbsoluteAccuracy();
        double accuracy = optimizer.getAbsoluteAccuracy();
        assertEquals(0.0, accuracy, 0.0);
    }
    
    @Test
    public void testResetMaximalIterationCount() {
        // test if resetting the maximal iteration count works
        optimizer.resetMaximalIterationCount();
        int count = optimizer.getMaximalIterationCount();
        assertEquals(Integer.MAX_VALUE, count);
    }
    
    @Test
    public void testResetRelativeAccuracy() {
        // test if resetting the relative accuracy works
        optimizer.resetRelativeAccuracy();
        double accuracy = optimizer.getRelativeAccuracy();
        assertEquals(0.0, accuracy, 0.0);
    }
    
    @Test
    public void testSetAbsoluteAccuracy() {
        // test if setting the absolute accuracy works
        optimizer.setAbsoluteAccuracy(0.1);
        double accuracy = optimizer.getAbsoluteAccuracy();
        assertEquals(0.1, accuracy, 0.0);
    }
    
    @Test
    public void testSetMaximalIterationCount() {
        // test if setting the maximal iteration count works
        optimizer.setMaximalIterationCount(100);
        int count = optimizer.getMaximalIterationCount();
        assertEquals(100, count);
    }
    
    @Test
    public void testSetMaxEvaluations() {
        // test if setting the max evaluations works
        optimizer.setMaxEvaluations(1000);
        int evaluations = optimizer.getMaxEvaluations();
        assertEquals(1000, evaluations);
    }
    
    @Test
    public void testSetRelativeAccuracy() {
        // test if setting the relative accuracy works
        optimizer.setRelativeAccuracy(0.01);
        double accuracy = optimizer.getRelativeAccuracy();
        assertEquals(0.01, accuracy, 0.0);
    }
    
    @Test
    public void testGetOptima() {
        try {
            double[] optima = optimizer.getOptima();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected exception
        }
    }
    
    @Test
    public void testGetOptimaValues() {
        try {
            double[] optimaValues = optimizer.getOptimaValues();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected exception
        }
    }
    
    @Test
    public void testOptimize() {
        try {
            double result = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);
            assertEquals(0.0, result, 0.0);
        } catch (ConvergenceException e) {
            fail("ConvergenceException thrown");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException thrown");
        }
    }
    
    @Test
    public void testOptimizeWithStartValue() {
        try {
            double result = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0, 5.0);
            assertEquals(0.0, result, 0.0);
        } catch (ConvergenceException e) {
            fail("ConvergenceException thrown");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException thrown");
        }
    }
}