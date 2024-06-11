import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testConstructorWithUpdateFormula() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNotNull(optimizer);
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.updateFormula);
        assertTrue(optimizer.preconditioner instanceof NonLinearConjugateGradientOptimizer.IdentityPreconditioner);
        assertTrue(optimizer.solver instanceof BrentSolver);
        assertEquals(1.0, optimizer.initialStep, 0.0);
    }

    @Test
    public void testConstructorWithUpdateFormulaAndChecker() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE, checker);
        assertNotNull(optimizer);
        assertEquals(ConjugateGradientFormula.POLAK_RIBIERE, optimizer.updateFormula);
        assertSame(checker, optimizer.getConvergenceChecker());
        assertTrue(optimizer.preconditioner instanceof NonLinearConjugateGradientOptimizer.IdentityPreconditioner);
        assertTrue(optimizer.solver instanceof BrentSolver);
        assertEquals(1.0, optimizer.initialStep, 0.0);
    }

    @Test
    public void testConstructorWithUpdateFormulaCheckerAndLineSearchSolver() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        BrentSolver lineSearchSolver = new BrentSolver();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, lineSearchSolver);
        assertNotNull(optimizer);
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.updateFormula);
        assertSame(checker, optimizer.getConvergenceChecker());
        assertSame(lineSearchSolver, optimizer.solver);
        assertTrue(optimizer.preconditioner instanceof NonLinearConjugateGradientOptimizer.IdentityPreconditioner);
        assertEquals(1.0, optimizer.initialStep, 0.0);
    }

    @Test
    public void testConstructorWithAllParameters() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        BrentSolver lineSearchSolver = new BrentSolver();
        NonLinearConjugateGradientOptimizer.IdentityPreconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE, checker, lineSearchSolver, preconditioner);
        assertNotNull(optimizer);
        assertEquals(ConjugateGradientFormula.POLAK_RIBIERE, optimizer.updateFormula);
        assertSame(checker, optimizer.getConvergenceChecker());
        assertSame(lineSearchSolver, optimizer.solver);
        assertSame(preconditioner, optimizer.preconditioner);
        assertEquals(1.0, optimizer.initialStep, 0.0);
    }

    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertEquals(1.0, optimizer.initialStep, 0.0);
        optimizer.setInitialStep(3.0);
        assertEquals(3.0, optimizer.initialStep, 0.0);
    }

    // TODO: Add test cases for the optimize method

    @Test
    public void testIdentityPreconditioner() {
        NonLinearConjugateGradientOptimizer.IdentityPreconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
        double[] variables = new double[]{1.0, 2.0, 3.0};
        double[] r = new double[]{4.0, 5.0, 6.0};
        double[] result = preconditioner.precondition(variables, r);
        assertArrayEquals(r, result, 0.0);
    }

    @Test
    public void testLineSearchFunctionValue() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        NonLinearConjugateGradientOptimizer.LineSearchFunction function = optimizer.new LineSearchFunction(new double[]{1.0, 2.0, 3.0});
        double result = function.value(0.5);
        assertEquals(0.5 * 1.0 + 0.5 * 2.0 + 0.5 * 3.0, result, 0.0);
    }

}