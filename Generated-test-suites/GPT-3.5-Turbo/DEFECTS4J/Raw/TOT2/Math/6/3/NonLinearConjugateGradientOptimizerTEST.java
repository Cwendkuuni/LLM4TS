import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {
    
    @Test
    public void testConstructorWithUpdateFormula() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNotNull(optimizer);
    }

    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(0.5);
        assertEquals(0.5, optimizer.getInitialStep(), 0.0);
    }

    @Test
    public void testFindUpperBound() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        UnivariateFunction f = x -> x * x - 4;
        double upperBound = optimizer.findUpperBound(f, 0, 5);
        assertEquals(2.0, upperBound, 0.0);
    }

    @Test
    public void testDoOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(0.5);
        optimizer.setMaxEvaluations(1000);
        optimizer.setStartPoint(new double[]{1.0, 2.0});
        optimizer.setGoalType(GoalType.MINIMIZE);
        PointValuePair result = optimizer.optimize();
        assertNotNull(result);
        assertEquals(2.0, result.getPoint()[0], 0.0);
        assertEquals(2.0, result.getPoint()[1], 0.0);
    }

}