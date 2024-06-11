import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testSetInitialStep() {
        double initialStep = -1;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer();
        optimizer.setInitialStep(initialStep);
        assertEquals(1.0, optimizer.getInitialStep(), 0.0001);
    }

    @Test
    public void testFindUpperBound() {
        double a = 0;
        double h = 1;
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x - 1;
            }
        };
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer();
        double upperBound = optimizer.findUpperBound(f, a, h);
        assertEquals(2.0, upperBound, 0.0001);
    }

    @Test
    public void testIdentityPreconditioner() {
        double[] variables = {1, 2, 3};
        double[] r = {4, 5, 6};
        NonLinearConjugateGradientOptimizer.IdentityPreconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
        double[] result = preconditioner.precondition(variables, r);
        assertArrayEquals(r, result, 0.0001);
    }

    @Test
    public void testLineSearchFunction() {
        double[] searchDirection = {1, 1, 1};
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer();
        NonLinearConjugateGradientOptimizer.LineSearchFunction lineSearchFunction = optimizer.new LineSearchFunction(searchDirection);
        double result = lineSearchFunction.value(2);
        assertEquals(6.0, result, 0.0001);
    }
}