import static org.junit.Assert.*;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(2.0);
        double initialStep = optimizer.getInitialStep();
        assertEquals(2.0, initialStep, 0.0);
    }

    @Test
    public void testDoOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        double[] startPoint = {0.5, 0.5};
        optimizer.setStartPoint(startPoint);
        optimizer.setMaxEvaluations(1000);

        PointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        assertNotNull(result.getPoint());
        assertNotNull(result.getValue());
    }
}