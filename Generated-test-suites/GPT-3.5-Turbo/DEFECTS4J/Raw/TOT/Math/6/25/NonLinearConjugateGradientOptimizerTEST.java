import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    // Test the constructor with default parameters
    @Test
    public void testConstructor() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNotNull(optimizer);
    }

    // Test setInitialStep method with positive value
    @Test
    public void testSetInitialStep_positiveValue() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        optimizer.setInitialStep(0.5);
        assertEquals(0.5, optimizer.getInitialStep(), 0);
    }

    // Test setInitialStep method with zero value
    @Test
    public void testSetInitialStep_zeroValue() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(0);
        assertEquals(1.0, optimizer.getInitialStep(), 0);
    }

    // Test doOptimize method with minimum optimization
    @Test
    public void testDoOptimize_minimum() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        double[] startPoint = new double[]{0.0};
        double[] expected = new double[]{0.0};
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.optimize();
        
        assertArrayEquals(expected, optimizer.getOptimum().getPoint(), 0);
    }

    // Test doOptimize method with maximum optimization
    @Test
    public void testDoOptimize_maximum() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        double[] startPoint = new double[]{1.0};
        double[] expected = new double[]{1.0};
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.optimize();
        
        assertArrayEquals(expected, optimizer.getOptimum().getPoint(), 0);
    }

    // Test findUpperBound method for bracketing a root between a and b
    @Test
    public void testFindUpperBound() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE);
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x - 5;
            }
        };

        try {
            double upperBound = optimizer.findUpperBound(f, 0, 10);
            assertEquals(5.0, upperBound, 0);
        } catch (MathIllegalStateException e) {
            fail("Exception should not be thrown");
        }
    }

    // Test findUpperBound method when no bracket can be found
    @Test(expected = MathIllegalStateException.class)
    public void testFindUpperBound_noBracket() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.pow(x, 3);
            }
        };

        optimizer.findUpperBound(f, 0, 10);
    }

    // Test precondition method with identity preconditioner
    @Test
    public void testIdentityPreconditioner() {
        NonLinearConjugateGradientOptimizer.IdentityPreconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
        double[] variables = new double[]{1.0, 2.0};
        double[] gradient = new double[]{2.0, 3.0};
        double[] preconditioned = preconditioner.precondition(variables, gradient);
        assertArrayEquals(gradient, preconditioned, 0);
    }

    // Test LineSearchFunction class
    @Test
    public void testLineSearchFunction() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        double[] searchDirection = new double[]{1.0, 2.0};
        NonLinearConjugateGradientOptimizer.LineSearchFunction lineSearchFunction = optimizer.new LineSearchFunction(searchDirection);
        double value = lineSearchFunction.value(0.5);
        assertEquals(2.5, value, 0);
    }

}