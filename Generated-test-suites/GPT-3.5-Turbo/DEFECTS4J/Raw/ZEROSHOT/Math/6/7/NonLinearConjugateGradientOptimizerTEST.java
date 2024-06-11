import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {
    
    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(0.5);
        assertEquals(0.5, optimizer.getInitialStep(), 0.0);
    }
    
    @Test
    public void testDoOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMaxEvaluations(1000);
        double[] startPoint = {1.0, 2.0, 3.0};
        optimizer.setStartPoint(startPoint);
        
        PointValuePair result = optimizer.doOptimize();
        
        double[] expectedPoint = {0.999999999998416, 1.999999999996832, 2.999999999995248};
        double expectedValue = 0.0;
        PointValuePair expected = new PointValuePair(expectedPoint, expectedValue);
        
        assertArrayEquals(expected.getPoint(), result.getPoint(), 1e-14);
        assertEquals(expected.getValue(), result.getValue(), 1e-14);
    }
}