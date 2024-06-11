import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testOptimizerInitialization() {
        // Test initialization with Fletcher-Reeves formula
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNotNull(optimizer);
    }

    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(0.5);
        assertEquals(0.5, optimizer.getInitialStep(), 0.001);
    }

    @Test
    public void testFindUpperBound() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        LineSearchFunction lsf = optimizer.new LineSearchFunction(new double[]{1, 1});
        double upperBound = optimizer.findUpperBound(lsf, 0, 1);
        assertTrue(upperBound > 0);
    }

    @Test
    public void testOptimizerOptimize() {
        ConjugateGradientFormula formula = ConjugateGradientFormula.FLETCHER_REEVES;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(formula);
        optimizer.setInitialStep(0.1);
        optimizer.setMaxEvaluations(1000);
        optimizer.setStartPoint(new double[]{1, 1});

        PointValuePair result = optimizer.optimize();
        assertNotNull(result);
    }

}