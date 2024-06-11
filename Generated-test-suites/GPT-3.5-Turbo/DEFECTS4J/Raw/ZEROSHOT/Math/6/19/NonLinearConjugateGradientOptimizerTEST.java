import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {
    
    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(0.5);
        assertEquals(0.5, optimizer.getInitialStep(), 0.0001);
    }

    @Test
    public void testDoOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setMaxEvaluations(1000);
        optimizer.setConvergenceChecker(new SimpleValueChecker(0.0001));
        optimizer.setStartPoint(new double[]{1, 2, 3});
        optimizer.setGoalType(GoalType.MINIMIZE);
        PointValuePair result = optimizer.doOptimize();
        assertEquals(0.0, result.getValue(), 0.0001);
        assertArrayEquals(new double[]{0, 0, 0}, result.getPoint(), 0.0001);
    }
    
    @Test
    public void testLineSearchFunction() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setStartPoint(new double[]{1, 2, 3});
        NonLinearConjugateGradientOptimizer.LineSearchFunction function = optimizer.new LineSearchFunction(new double[]{1, 1, 1});
        assertEquals(6.0, function.value(1.0), 0.0001);
        assertEquals(3.0, function.value(2.0), 0.0001);
    }
}