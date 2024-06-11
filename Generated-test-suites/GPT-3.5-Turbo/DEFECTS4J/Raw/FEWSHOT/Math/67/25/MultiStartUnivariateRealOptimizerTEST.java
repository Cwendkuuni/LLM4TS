import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {
    
    private MultiStartUnivariateRealOptimizer optimizer;
    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        optimizer = new MultiStartUnivariateRealOptimizer(new DefaultUnivariateRealOptimizer(), 5, new RandomGenerator());
    }
    
    @Test
    public void testOptimizerOptimizeWithMinimization() {
        try {
            double result = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
            assertNotNull(result);
            assertTrue(result >= 0.0 && result <= 1.0);
        } catch (ConvergenceException e) {
            fail("ConvergenceException thrown");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException thrown");
        }
    }
    
    @Test
    public void testOptimizerOptimizeWithMaximization() {
        try {
            double result = optimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 1.0);
            assertNotNull(result);
            assertTrue(result >= 0.0 && result <= 1.0);
        } catch (ConvergenceException e) {
            fail("ConvergenceException thrown");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException thrown");
        }
    }
    
    @Test(expected = ConvergenceException.class)
    public void testOptimizerOptimizeWithFailedConvergence() throws ConvergenceException {
        optimizer = new MultiStartUnivariateRealOptimizer(new DefaultUnivariateRealOptimizer(), 5, new RandomGenerator());
        optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) {
                return Double.POSITIVE_INFINITY;
            }
        }, GoalType.MINIMIZE, 0.0, 1.0);
    }
    
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizerOptimizeWithFunctionEvaluationException() throws FunctionEvaluationException {
        optimizer = new MultiStartUnivariateRealOptimizer(new DefaultUnivariateRealOptimizer(), 5, new RandomGenerator());
        optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) {
                throw new FunctionEvaluationException(x);
            }
        }, GoalType.MINIMIZE, 0.0, 1.0);
    }

}