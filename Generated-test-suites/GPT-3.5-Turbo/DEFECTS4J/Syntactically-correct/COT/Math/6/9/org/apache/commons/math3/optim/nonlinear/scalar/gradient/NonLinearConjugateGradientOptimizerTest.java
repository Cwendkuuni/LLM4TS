import static org.junit.Assert.*;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

    /* Test Cases */

    // testConjugateGradientFormulaUpdateFormula
    @Test
    public void testConjugateGradientFormulaUpdateFormula() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertTrue(optimizer.updateFormula == ConjugateGradientFormula.FLETCHER_REEVES);
        
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        assertTrue(optimizer.updateFormula == ConjugateGradientFormula.POLAK_RIBIERE);
    }

    // testInitialStepNegative
    @Test
    public void testInitialStepNegative() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(-1.0);
        assertTrue(optimizer.initialStep == 1.0);
        
        optimizer.setInitialStep(-10.0);
        assertTrue(optimizer.initialStep == 1.0);
    }

    // testNBiggerThanNDividedByTwo
    @Test
    public void testNBiggerThanNDividedByTwo() {
        // n = 3, beta < 0
        // searchDirection array length is less than n
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.internalProcedure(new ConvergenceChecker<PointValuePair>() {
            public boolean converged(int count, PointValuePair previous, PointValuePair current) {
                return false;
            }
        }, new double[]{1.0, 2.0}, new double[]{1.0});
    }

    // testUpdateFormulaInvalid
    @Test
    public void testUpdateFormulaInvalid() {
        NonLinearConjugateGradientOptimizer optimizer = null;
        try {
            optimizer = new NonLinearConjugateGradientOptimizer(null);
            fail("Expected IllegalArgumentException to be thrown.");
        } catch(IllegalArgumentException e) {
            assertNull(optimizer);
        }
    }

    // testFindUpperBoundUnableToBracketOptimum
    @Test(expected = MathIllegalStateException.class)
    public void testFindUpperBoundUnableToBracketOptimum() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.findUpperBound(new UnivariateFunction() {
            public double value(double x) {
                return 0;
            }
        }, 0, 1);
    }

    // testPreconditionerIdentity
    @Test
    public void testPreconditionerIdentity() {
        NonLinearConjugateGradientOptimizer.IdentityPreconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
        double[] variables = new double[]{1.0, 2.0, 3.0};
        double[] r = new double[]{1.0, 2.0, 3.0};
        double[] result = preconditioner.precondition(variables, r);
        assertArrayEquals(result, r, 0);
    }

}