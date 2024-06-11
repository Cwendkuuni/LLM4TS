import static org.junit.Assert.*;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.random.RandomDataGenerator;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealFunction function;
    private MultiStartUnivariateRealOptimizer optimizer;

    @Before
    public void setUp() {
        function = new TestFunction();
        optimizer = new MultiStartUnivariateRealOptimizer(new TestOptimizer(), 3, new RandomDataGenerator());
    }

    @Test
    public void testOptimize() {
        double result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 3);
        assertEquals(0, result, 1e-5);
    }

    @Test
    public void testOptimize_MaxIterationsReached() {
        optimizer.setMaximalIterationCount(100);
        try {
            optimizer.optimize(function, GoalType.MINIMIZE, 0, 3);
            fail("Expected ConvergenceException but it was not thrown");
        } catch (ConvergenceException e) {
            assertEquals("Maximal number of iterations exceeded", e.getMessage());
        }
    }

    @Test
    public void testOptimize_MaxEvaluationsReached() {
        optimizer.setMaxEvaluations(50);
        try {
            optimizer.optimize(function, GoalType.MINIMIZE, 0, 3);
            fail("Expected FunctionEvaluationException but it was not thrown");
        } catch (FunctionEvaluationException e) {
            assertEquals("Maximal number of evaluations exceeded", e.getMessage());
        }
    }

    @Test
    public void testGetOptima() {
        optimizer.optimize(function, GoalType.MINIMIZE, 0, 3);
        double[] optima = optimizer.getOptima();
        assertEquals(3, optima.length);
        assertEquals(0, optima[0], 1e-5);
        assertTrue(Double.isNaN(optima[1]));
        assertTrue(Double.isNaN(optima[2]));
    }

    @Test
    public void testGetOptimaValues() {
        optimizer.optimize(function, GoalType.MINIMIZE, 0, 3);
        double[] optimaValues = optimizer.getOptimaValues();
        assertEquals(3, optimaValues.length);
        assertEquals(0, optimaValues[0], 1e-5);
        assertTrue(Double.isNaN(optimaValues[1]));
        assertTrue(Double.isNaN(optimaValues[2]));
    }

    private class TestFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x * x;
        }
    }

    private class TestOptimizer implements UnivariateRealOptimizer {
        public double getFunctionValue() {
            return 0;
        }
        public double getResult() {
            return 0;
        }
        public double getAbsoluteAccuracy() {
            return 0;
        }
        public int getIterationCount() {
            return 0;
        }
        public int getMaximalIterationCount() {
            return 0;
        }
        public int getMaxEvaluations() {
            return 0;
        }
        public int getEvaluations() {
            return 0;
        }
        public double getRelativeAccuracy() {
            return 0;
        }
        public void resetAbsoluteAccuracy() {
            
        }
        public void resetMaximalIterationCount() {
            
        }
        public void resetRelativeAccuracy() {
            
        }
        public void setAbsoluteAccuracy(double accuracy) {
            
        }
        public void setMaximalIterationCount(int count) {
            
        }
        public void setMaxEvaluations(int maxEvaluations) {
            
        }
        public void setRelativeAccuracy(double accuracy) {
            
        }
        public double optimize(UnivariateRealFunction f, GoalType goalType, double min, double max)
                throws ConvergenceException, FunctionEvaluationException {
            return 0;
        }
        public double optimize(UnivariateRealFunction f, GoalType goalType,
                double min, double max, double startValue)
                throws ConvergenceException, FunctionEvaluationException {
            return 0;
        }
    }

}