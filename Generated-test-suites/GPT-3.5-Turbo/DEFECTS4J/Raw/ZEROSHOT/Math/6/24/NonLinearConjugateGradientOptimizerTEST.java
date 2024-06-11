import static org.junit.Assert.*;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setInitialStep(0.5);
        assertEquals(0.5, optimizer.getInitialStep(), 0.0);
    }

    @Test
    public void testDoOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setStartPoint(new double[]{1, 2, 3});
        optimizer.setMaxEvaluations(1000);
        optimizer.setConvergenceChecker(new SimpleValueChecker(1e-6));

        PointValuePair result = optimizer.doOptimize();

        assertEquals(3, result.getPoint().length);
        assertEquals(0, result.getPoint()[0], 1e-6);
        assertEquals(0, result.getPoint()[1], 1e-6);
        assertEquals(0, result.getPoint()[2], 1e-6);
        assertEquals(0, optimizer.computeObjectiveValue(result.getPoint()), 1e-6);
    }

    @Test
    public void testPrecondition() {
        NonLinearConjugateGradientOptimizer.IdentityPreconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
        double[] variables = {1, 2, 3};
        double[] r = {4, 5, 6};

        double[] result = preconditioner.precondition(variables, r);

        assertArrayEquals(r, result, 0.0);
        assertNotSame(r, result);
    }

    @Test
    public void testLineSearchFunction() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        double[] searchDirection = {1, 2, 3};
        NonLinearConjugateGradientOptimizer.LineSearchFunction lineSearchFunction =
                optimizer.new LineSearchFunction(searchDirection);

        double result = lineSearchFunction.value(0.5);

        assertEquals(0, result, 1e-6);
    }
}