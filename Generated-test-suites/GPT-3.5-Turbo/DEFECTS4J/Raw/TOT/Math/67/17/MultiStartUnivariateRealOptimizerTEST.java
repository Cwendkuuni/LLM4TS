import org.apache.commons.math.optimization.*;
import org.apache.commons.math.analysis.*;
import org.apache.commons.math.random.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {
    
    private final double EPSILON = 1e-10;
    
    // Test the optimize method with a quadratic function and minimize goal type
    @Test
    public void testOptimizeQuadraticFunctionMinimize() {
        UnivariateRealOptimizer optimizer = new BisectionUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new RandomDataGenerator());
        
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x + 2 * x - 3;
            }
        };
        
        double min = -10.0;
        double max = 10.0;
        GoalType goalType = GoalType.MINIMIZE;
        
        try {
            double result = multiStartOptimizer.optimize(function, goalType, min, max);
            assertEquals(-2.0, result, EPSILON);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception should not be thrown");
        }
    }
    
    // Test the optimize method with a quadratic function and maximize goal type
    @Test
    public void testOptimizeQuadraticFunctionMaximize() {
        UnivariateRealOptimizer optimizer = new BisectionUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new RandomDataGenerator());
        
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return -x * x + 2 * x - 3;
            }
        };
        
        double min = -10.0;
        double max = 10.0;
        GoalType goalType = GoalType.MAXIMIZE;
        
        try {
            double result = multiStartOptimizer.optimize(function, goalType, min, max);
            assertEquals(4.0, result, EPSILON);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception should not be thrown");
        }
    }
    
    // Test the optimize method with a function that throws FunctionEvaluationException
    @Test
    public void testOptimizeFunctionEvaluationException() {
        UnivariateRealOptimizer optimizer = new BisectionUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new RandomDataGenerator());
        
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        
        double min = -10.0;
        double max = 10.0;
        GoalType goalType = GoalType.MINIMIZE;
        
        try {
            multiStartOptimizer.optimize(function, goalType, min, max);
            fail("FunctionEvaluationException should be thrown");
        } catch (ConvergenceException e) {
            fail("ConvergenceException should not be thrown");
        } catch (FunctionEvaluationException e) {
            // Pass
        }
    }
    
    // Test the optimize method with a function that throws ConvergenceException
    @Test
    public void testOptimizeConvergenceException() {
        UnivariateRealOptimizer optimizer = new BisectionUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new RandomDataGenerator());
        
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws ConvergenceException {
                throw new ConvergenceException();
            }
        };
        
        double min = -10.0;
        double max = 10.0;
        GoalType goalType = GoalType.MINIMIZE;
        
        try {
            multiStartOptimizer.optimize(function, goalType, min, max);
            fail("ConvergenceException should be thrown");
        } catch (ConvergenceException e) {
            // Pass
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException should not be thrown");
        }
    }
    
    // Test the getOptima method
    @Test
    public void testGetOptima() {
        UnivariateRealOptimizer optimizer = new BisectionUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new RandomDataGenerator());
        
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        
        double min = -10.0;
        double max = 10.0;
        GoalType goalType = GoalType.MINIMIZE;
        
        try {
            multiStartOptimizer.optimize(function, goalType, min, max);
            
            double[] optima = multiStartOptimizer.getOptima();
            assertEquals(5, optima.length);
            assertEquals(0.0, optima[0], EPSILON);
            
            double[] optimaValues = multiStartOptimizer.getOptimaValues();
            assertEquals(5, optimaValues.length);
            assertEquals(0.0, optimaValues[0], EPSILON);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception should not be thrown");
        }
    }
}