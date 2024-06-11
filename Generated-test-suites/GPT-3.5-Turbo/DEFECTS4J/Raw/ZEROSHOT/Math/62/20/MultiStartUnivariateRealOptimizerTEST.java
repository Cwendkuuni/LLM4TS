import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.util.FastMath;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    private MockOptimizer underlyingOptimizer;

    @Before
    public void setUp() {
        underlyingOptimizer = new MockOptimizer();
        optimizer = new MultiStartUnivariateRealOptimizer<>(underlyingOptimizer, 5, null);
    }

    // Test optimize method when the underlying optimizer throws a FunctionEvaluationException
    // Ensure that an array of optima is returned with null values for the failed starts
    @Test
    public void testOptimizeWithFunctionEvaluationException() throws FunctionEvaluationException {
        underlyingOptimizer.setThrowFunctionEvaluationException(true);
        UnivariateRealPointValuePair result = optimizer.optimize(null, GoalType.MINIMIZE, 0, 1);
        assertNotNull(result);
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertEquals(optimizer.starts, optima.length);
        for (int i = 0; i < optimizer.starts; i++) {
            if (i == 0) {
                assertNotNull(optima[i]);
            } else {
                assertNull(optima[i]);
            }
        }
    }

    // Test optimize method when the underlying optimizer throws a ConvergenceException
    // Ensure that an array of optima is returned with null values for the failed starts
    @Test
    public void testOptimizeWithConvergenceException() throws FunctionEvaluationException {
        underlyingOptimizer.setThrowConvergenceException(true);
        UnivariateRealPointValuePair result = optimizer.optimize(null, GoalType.MINIMIZE, 0, 1);
        assertNotNull(result);
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertEquals(optimizer.starts, optima.length);
        for (int i = 0; i < optimizer.starts; i++) {
            if (i == 0) {
                assertNotNull(optima[i]);
            } else {
                assertNull(optima[i]);
            }
        }
    }

    // Test optimize method when the underlying optimizer converges for all starts
    // Ensure that the result is the best optima and all other optima are ordered by their objective value
    @Test
    public void testOptimizeConvergence() throws FunctionEvaluationException {
        underlyingOptimizer.setThrowFunctionEvaluationException(false);
        underlyingOptimizer.setThrowConvergenceException(false);
        double[] objectiveValues = {1.0, 2.0, 0.5, 3.0, 2.5};
        underlyingOptimizer.setObjectiveValues(objectiveValues);
        UnivariateRealPointValuePair result = optimizer.optimize(null, GoalType.MINIMIZE, 0, 1);
        assertNotNull(result);
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertEquals(optimizer.starts, optima.length);
        assertEquals(result, optima[0]);
        for (int i = 1; i < optimizer.starts; i++) {
            assertNotNull(optima[i]);
            assertTrue(optima[i-1].getValue() <= optima[i].getValue());
        }
    }

    // Test optimize method when the underlying optimizer does not converge for any start
    // Ensure that a ConvergenceException is thrown
    @Test(expected = ConvergenceException.class)
    public void testOptimizeNoConvergence() throws FunctionEvaluationException {
        underlyingOptimizer.setThrowFunctionEvaluationException(false);
        underlyingOptimizer.setThrowConvergenceException(true);
        optimizer.optimize(null, GoalType.MINIMIZE, 0, 1);
    }

    // Mock optimizer class to simulate behavior of underlying optimizer
    private class MockOptimizer implements BaseUnivariateRealOptimizer<UnivariateRealFunction> {

        private boolean throwFunctionEvaluationException;
        private boolean throwConvergenceException;
        private double[] objectiveValues;

        public void setThrowFunctionEvaluationException(boolean throwException) {
            throwFunctionEvaluationException = throwException;
        }

        public void setThrowConvergenceException(boolean throwException) {
            throwConvergenceException = throwException;
        }

        public void setObjectiveValues(double[] objectiveValues) {
            this.objectiveValues = objectiveValues;
        }

        @Override
        public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public int getMaxEvaluations() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int getEvaluations() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void setMaxEvaluations(int maxEvaluations) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max)
                throws FunctionEvaluationException {
            if (throwFunctionEvaluationException) {
                throw new FunctionEvaluationException(null);
            }
            if (throwConvergenceException) {
                throw new ConvergenceException(null);
            }
            return new UnivariateRealPointValuePair(null, objectiveValues[getEvaluations()]);
        }
        
    }

}