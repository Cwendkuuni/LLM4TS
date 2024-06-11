import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.util.FastMath;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {
    private MultiStartUnivariateRealOptimizer<FakeUnivariateRealFunction> optimizer;
    private FakeUnivariateRealFunction function;
    private RandomGenerator generator;
    
    @Before
    public void setup() {
        optimizer = new MultiStartUnivariateRealOptimizer<FakeUnivariateRealFunction>(FakeSingleStartOptimizer.INSTANCE, 3, null);
        function = new FakeUnivariateRealFunction();
        generator = new FakeRandomGenerator();
    }
    
    @Test
    public void testGetConvergenceChecker() {
        assertNull(optimizer.getConvergenceChecker());
        
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new FakeConvergenceChecker();
        optimizer.setConvergenceChecker(checker);
        
        assertEquals(checker, optimizer.getConvergenceChecker());
    }
    
    @Test
    public void testGetMaxEvaluations() {
        assertEquals(0, optimizer.getMaxEvaluations());
        
        optimizer.setMaxEvaluations(100);
        
        assertEquals(100, optimizer.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        assertEquals(0, optimizer.getEvaluations());
        
        optimizer.optimize(function, GoalType.MINIMIZE, 0, 1);
        
        assertEquals(0, optimizer.getEvaluations());
    }
    
    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(100);
        
        assertEquals(100, optimizer.getMaxEvaluations());
    }
    
    @Test
    public void testGetOptima_NoOptimumYet() {
        try {
            optimizer.getOptima();
            fail("Expected MathIllegalStateException");
        } catch (MathIllegalStateException e) {
            assertEquals(LocalizedFormats.NO_OPTIMUM_COMPUTED_YET, e.getPattern());
        }
    }
    
    @Test
    public void testOptimize_NoConvergenceWithAnyStartPoint() {
        optimizer = new MultiStartUnivariateRealOptimizer<FakeUnivariateRealFunction>(FakeSingleStartOptimizer.EXCEPTION_OPTIMIZER, 3, null);
        
        try {
            optimizer.optimize(function, GoalType.MINIMIZE, 0, 1);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            assertEquals(LocalizedFormats.NO_CONVERGENCE_WITH_ANY_START_POINT, e.getPattern());
            assertEquals(3, e.getArguments()[0]);
        }
    }
    
    @Test
    public void testOptimize_Minimize() {
        UnivariateRealPointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 1);
        
        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }
    
    @Test
    public void testOptimize_Maximize() {
        UnivariateRealPointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, 0, 1);
        
        assertNotNull(result);
        assertEquals(1.0, result.getPoint(), 0.001);
        assertEquals(1.0, result.getValue(), 0.001);
    }
}