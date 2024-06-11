import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Typical use case
        double[] startPoint = {1.0, 2.0, 3.0};
        double[] expectedPoint = {0.5, 1.5, 2.5};
        double expectedObjectiveValue = 0.0;
        
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setStartPoint(startPoint);
        PointValuePair result = optimizer.optimize();
        
        double[] actualPoint = result.getPoint();
        double actualObjectiveValue = result.getValue();
        
        assertEquals(expectedObjectiveValue, actualObjectiveValue, 1e-5);
        assertArrayEquals(expectedPoint, actualPoint, 1e-5);
    }
    
    @Test(expected = MathIllegalStateException.class)
    public void testFindUpperBound() {
        UnivariateFunction constantFunction = new UnivariateFunction() {
            public double value(double x) {
                return 1.0;
            }
        };
        
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        double upperBound = optimizer.findUpperBound(constantFunction, 0.0, 1.0);
    }
    
    @Test
    public void testSetInitialStep() {
        double expectedInitialStep = 1.5;
        
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setInitialStep(1.5);
        double actualInitialStep = optimizer.getInitialStep();
        
        assertEquals(expectedInitialStep, actualInitialStep, 1e-5);
    }
}