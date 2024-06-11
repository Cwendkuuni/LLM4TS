import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.FunctionEvaluationException;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.util.FastMath;
import org.junit.BeforeClass;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {
    
    private static MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    
    @BeforeClass
    public static void setup() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> singleOptimizer = new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
                // do nothing
            }

            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
                return null;
            }

            public int getMaxEvaluations() {
                return 0;
            }

            public int getEvaluations() {
                return 0;
            }

            public void setMaxEvaluations(int maxEvaluations) {
                // do nothing
            }

            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException, ConvergenceException {
                return null;
            }

            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max, double startValue) throws FunctionEvaluationException, ConvergenceException {
                return null;
            }
        };
        int starts = 5;
        RandomGenerator generator = null; // implement this or use a mock
        optimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(singleOptimizer, starts, generator);
    }
    
    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checker = null; // implement this or use a mock
        optimizer.setConvergenceChecker(checker);
    }
    
    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checker = optimizer.getConvergenceChecker();
        assertNull(checker);
    }
    
    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(0, maxEvaluations);
    }
    
    @Test
    public void testGetEvaluations() {
        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);
    }
    
    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 100;
        optimizer.setMaxEvaluations(maxEvaluations);
        int actualMaxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(maxEvaluations, actualMaxEvaluations);
    }
    
    @Test
    public void testGetOptima() {
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertNull(optima);
    }
    
    @Test
    public void testOptimize() {
        UnivariateRealFunction f = null; // implement this or use a mock
        GoalType goal = GoalType.MINIMIZE;
        double min = 0;
        double max = 1;
        
        try {
            UnivariateRealPointValuePair result = optimizer.optimize(f, goal, min, max);
        } catch (FunctionEvaluationException e) {
            // handle exception
        } catch (ConvergenceException e) {
            // handle exception
        }
    }
    
    @Test
    public void testOptimizeWithStartValue() {
        UnivariateRealFunction f = null; // implement this or use a mock
        GoalType goal = GoalType.MINIMIZE;
        double min = 0;
        double max = 1;
        double startValue = 0.5;
        
        try {
            UnivariateRealPointValuePair result = optimizer.optimize(f, goal, min, max, startValue);
        } catch (FunctionEvaluationException e) {
            // handle exception
        } catch (ConvergenceException e) {
            // handle exception
        }
    }
}