import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {
    private NonLinearConjugateGradientOptimizer optimizer;
    
    @Before
    public void setup() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    }
    
    @Test
    public void testDoOptimize() {
        double[] startPoint = {1.0, 2.0, 3.0};
        GoalType goal = GoalType.MINIMIZE;
        int maxEvaluations = 1000;
        double[] result = optimizer.doOptimize();
        assertNotNull(result);
    }
    
    @Test
    public void testSetInitialStep() {
        optimizer.setInitialStep(0.5);
        assertEquals(0.5, optimizer.getInitialStep(), 0.0001);
    }
    
    @Test
    public void testFindUpperBound() {
        double[] variables = {1.0, 2.0, 3.0};
        double a = 0.0;
        double h = 0.5;
        double result = optimizer.findUpperBound(new LineSearchFunction(variables), a, h);
        assertTrue(result > a);
    }
    
    @Test(expected = MathIllegalStateException.class)
    public void testFindUpperBound_Exception() {
        double[] variables = {1.0, 2.0, 3.0};
        double a = 0.0;
        double h = 0.5;
        optimizer.findUpperBound(new LineSearchFunction(variables), a, h);
    }
    
    @Test
    public void testPreconditioner() {
        NonLinearConjugateGradientOptimizer.IdentityPreconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
        double[] variables = {1.0, 2.0, 3.0};
        double[] r = preconditioner.precondition(variables, variables);
        assertArrayEquals(variables, r, 0.0001);
    }
}