import static org.junit.Assert.*;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testConstructorWithDefaultUpdateFormula() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithCustomUpdateFormulaAndChecker() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE, new SimpleValueChecker());
        assertNotNull(optimizer);
    }

    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(2.5);
        assertEquals(2.5, optimizer.getInitialStep(), 0.0);
    }

    @Test
    public void testOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setMaxEvaluations(1000);
        optimizer.setStartPoint(new double[]{1.0, -2.0});
        optimizer.setGoalType(GoalType.MINIMIZE);

        PointValuePair result = optimizer.optimize();

        assertEquals(0.0, result.getPoint()[0], 0.0001);
        assertEquals(0.0, result.getPoint()[1], 0.0001);
    }

    @Test
    public void testFindUpperBound() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        LineSearchFunction lsf = optimizer.new LineSearchFunction(new double[]{1.0, 2.5});
        double upperBound = optimizer.findUpperBound(lsf, 0, 1.0);
        assertTrue(upperBound > 0);
    }
}