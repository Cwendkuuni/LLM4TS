import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testConstructor() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNotNull(optimizer);
    }

    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(0.5);
        assertEquals(0.5, optimizer.getInitialStep(), 0.0001);
    }

    @Test
    public void testFindUpperBound() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        LineSearchFunction lsf = optimizer.new LineSearchFunction(new double[]{1, 2, 3});
        double upperBound = optimizer.findUpperBound(lsf, 0, 1);
        assertTrue(upperBound > 0);
    }

    @Test
    public void testDoOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.optimize(10, new ObjectiveFunction(), GoalType.MINIMIZE,
            new NelderMeadSimplex(new double[][] {
                { 0.0, 0.0 }, { 1.0, 0.0 }, { 0.0, 1.0 }
            }));
        PointValuePair result = optimizer.getResult();
        assertNotNull(result);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testFindUpperBoundException() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        LineSearchFunction lsf = optimizer.new LineSearchFunction(new double[]{1, 2, 3});
        optimizer.findUpperBound(lsf, 0, -1);
    }

    @Test
    public void testIdentityPreconditioner() {
        NonLinearConjugateGradientOptimizer.IdentityPreconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
        double[] variables = {1, 2, 3};
        double[] r = {4, 5, 6};
        double[] result = preconditioner.precondition(variables, r);
        assertArrayEquals(r, result, 0.0001);
    }

    @Test
    public void testLineSearchFunction() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        LineSearchFunction lsf = optimizer.new LineSearchFunction(new double[]{1, 2, 3});
        double value = lsf.value(0.5);
        assertTrue(value != 0);
    }
}