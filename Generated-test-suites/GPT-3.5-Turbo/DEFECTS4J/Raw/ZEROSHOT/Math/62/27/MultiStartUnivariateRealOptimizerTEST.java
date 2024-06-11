import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testSetConvergenceChecker() {
        // Create the optimizer and the convergence checker
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new DummyOptimizer();
        ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker = new DummyConvergenceChecker();
        
        // Create the MultiStartUnivariateRealOptimizer
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = 
            new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 5, null);
        
        // Set the convergence checker
        multiStartOptimizer.setConvergenceChecker(convergenceChecker);
        
        // Check that the convergence checker was correctly set
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceChecker() {
        // Create the optimizer and the convergence checker
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new DummyOptimizer();
        ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker = new DummyConvergenceChecker();
        
        // Create the MultiStartUnivariateRealOptimizer
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = 
            new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 5, null);
        
        // Set the convergence checker
        optimizer.setConvergenceChecker(convergenceChecker);
        
        // Check that the convergence checker was correctly retrieved
        assertEquals(convergenceChecker, multiStartOptimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        // Create the optimizer
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new DummyOptimizer();
        
        // Create the MultiStartUnivariateRealOptimizer
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = 
            new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 5, null);
        
        // Set the max evaluations
        optimizer.setMaxEvaluations(100);
        
        // Check that the max evaluations was correctly retrieved
        assertEquals(100, multiStartOptimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        // Create the optimizer
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new DummyOptimizer();
        
        // Create the MultiStartUnivariateRealOptimizer
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = 
            new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 5, null);
        
        // Set the evaluations
        optimizer.setEvaluations(50);
        
        // Check that the evaluations was correctly retrieved
        assertEquals(50, multiStartOptimizer.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        // Create the optimizer
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new DummyOptimizer();
        
        // Create the MultiStartUnivariateRealOptimizer
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = 
            new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 5, null);
        
        // Set the max evaluations
        multiStartOptimizer.setMaxEvaluations(200);
        
        // Check that the max evaluations was correctly set
        assertEquals(200, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetOptima() {
        // Create the optimizer
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new DummyOptimizer();
        
        // Create the MultiStartUnivariateRealOptimizer
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = 
            new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 5, null);
        
        // Create the optima array
        UnivariateRealPointValuePair[] optima = new UnivariateRealPointValuePair[5];
        for (int i = 0; i < 5; i++) {
            optima[i] = new UnivariateRealPointValuePair(i, i);
        }
        
        // Set the optima array
        multiStartOptimizer.setOptima(optima);
        
        // Check that the optima array was correctly retrieved
        assertArrayEquals(optima, multiStartOptimizer.getOptima());
    }

    @Test
    public void testOptimize() {
        // Create the optimizer
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new DummyOptimizer();
        
        // Create the MultiStartUnivariateRealOptimizer
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = 
            new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 5, null);
        
        // Create the objective function
        UnivariateRealFunction f = new DummyFunction();
        
        // Set the evaluations
        optimizer.setEvaluations(50);
        
        // Optimize the function
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(f, GoalType.MINIMIZE, 0, 10, 5);
        
        // Check that the result is correct
        assertEquals(0.0, result.getPoint(), 0);
        assertEquals(0.0, result.getValue(), 0);
    }
    
    // Dummy implementation of the BaseUnivariateRealOptimizer interface for testing purposes
    private class DummyOptimizer implements BaseUnivariateRealOptimizer<UnivariateRealFunction> {

        public void setEvaluations(int evaluations) {
            // Dummy implementation
        }

        public void setOptima(UnivariateRealPointValuePair[] optima) {
            // Dummy implementation
        }

        public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
            // Dummy implementation
        }

        public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
            return null; // Dummy implementation
        }

        public int getMaxEvaluations() {
            return 0; // Dummy implementation
        }

        public int getEvaluations() {
            return 0; // Dummy implementation
        }

        public void setMaxEvaluations(int maxEvaluations) {
            // Dummy implementation
        }

        public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max)
            throws FunctionEvaluationException {
            return new UnivariateRealPointValuePair(0.0, 0.0); // Dummy implementation
        }

        public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max,
            double startValue) throws FunctionEvaluationException {
            return new UnivariateRealPointValuePair(0.0, 0.0); // Dummy implementation
        }
    }
    
    // Dummy implementation of the ConvergenceChecker interface for testing purposes
    private class DummyConvergenceChecker implements ConvergenceChecker<UnivariateRealPointValuePair> {

        public boolean converged(int iteration, UnivariateRealPointValuePair previous,
            UnivariateRealPointValuePair current) {
            return true; // Dummy implementation
        }
    }
    
    // Dummy implementation of the UnivariateRealFunction interface for testing purposes
    private class DummyFunction implements UnivariateRealFunction {

        public double value(double x) {
            return 0.0; // Dummy implementation
        }
    }
}