import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {
    
    private BaseOptimizer<Integer> optimizer;
    
    @Before
    public void setUp() {
        optimizer = new BaseOptimizerImpl<>();
    }
    
    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(1000, maxEvaluations);
    }
    
    @Test
    public void testGetEvaluationsBeforeOptimize() {
        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);
    }
    
    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<Integer> convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull(convergenceChecker);
    }
    
    @Test
    public void testGetEvaluationsAfterOptimize() {
        optimizer.optimize();
        int evaluations = optimizer.getEvaluations();
        assertEquals(1, evaluations);
    }
    
    @Test
    public void testOptimize() {
        // Typical use case - optimize a simple quadratic function
        // with constraints
        
        QuadraticFunction function = new QuadraticFunction();
        Constraints constraints = new Constraints(-10, 10);
        
        optimizer.setObjectiveFunction(function);
        optimizer.setConstraints(constraints);
        
        optimizer.optimize();
        
        double result = optimizer.getResult();
        assertEquals(0, result, 0.01);
    }
    
    @Test
    public void testOptimizeWithUnboundedConstraints() {
        // Edge case - optimize a function with unbounded constraints
        
        LinearFunction function = new LinearFunction();
        Constraints constraints = new Constraints(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        
        optimizer.setObjectiveFunction(function);
        optimizer.setConstraints(constraints);
        
        optimizer.optimize();
        
        double result = optimizer.getResult();
        assertTrue(Double.isInfinite(result));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testOptimizeWithErrorScenario() {
        // Error scenario - try to optimize without setting objective function
        
        Constraints constraints = new Constraints(-100, 100);
        
        optimizer.setConstraints(constraints);
        
        optimizer.optimize();
    }
}

class BaseOptimizerImpl<PAIR> implements BaseOptimizer<PAIR> {
    
    private int maxEvaluations = 1000;
    private int evaluations = 0;
    private ConvergenceChecker<PAIR> convergenceChecker;
    
    @Override
    public int getMaxEvaluations() {
        return maxEvaluations;
    }
    
    @Override
    public int getEvaluations() {
        return evaluations;
    }
    
    @Override
    public ConvergenceChecker<PAIR> getConvergenceChecker() {
        return convergenceChecker;
    }
    
    public void setObjectiveFunction(ObjectiveFunction<PAIR> objectiveFunction) {
        // Implementation not shown
    }
    
    public void setConstraints(Constraints constraints) {
        // Implementation not shown
    }
    
    public void optimize() {
        // Implementation not shown
    }
    
    public double getResult() {
        // Implementation not shown
    }
}

class QuadraticFunction implements ObjectiveFunction<Integer> {
    // Implementation not shown
}

class LinearFunction implements ObjectiveFunction<Double> {
    // Implementation not shown
}

class Constraints {
    
    private double lowerBound;
    private double upperBound;
    
    public Constraints(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }
    
    // Getter and setter methods not shown
}

interface ObjectiveFunction<PAIR> {
    // Method signature not shown
}