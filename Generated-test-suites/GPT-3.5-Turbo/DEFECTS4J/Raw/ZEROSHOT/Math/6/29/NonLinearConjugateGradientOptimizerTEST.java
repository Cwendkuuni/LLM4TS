import static org.junit.Assert.*;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(2.0);
        assertEquals(2.0, optimizer.initialStep, 0.0001);
    }

    @Test
    public void testSetInitialStepNegative() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(-1.0);
        assertEquals(1.0, optimizer.initialStep, 0.0001);
    }

    @Test
    public void testDoOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(1.0);
        optimizer.setMaxEvaluations(1000);
        double[] startPoint = new double[]{0, 0};
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setConvergenceChecker(new SimpleValueChecker());
        PointValuePair result = optimizer.doOptimize();
        double[] expectedPoint = new double[]{0, 0};
        PointValuePair expected = new PointValuePair(expectedPoint, 0.0);
        assertEquals(expected, result);
    }
}