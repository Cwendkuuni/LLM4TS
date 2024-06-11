import org.junit.Test;
import org.junit.Assert;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(2.0);
        double initialStep = optimizer.initialStep;
        Assert.assertEquals(2.0, initialStep, 0.0);
    }

    @Test
    public void testFindUpperBound() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        UnivariateFunction f = new LineSearchFunction(new double[] {1.0, 2.0});
        double upperBound = optimizer.findUpperBound(f, 0.0, 1.0);
        Assert.assertEquals(1.0, upperBound, 0.0);
    }

    @Test
    public void testValue() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        double[] variables = new double[] {1.0, 2.0};
        double[] r = new double[] {3.0, 4.0};
        double value = optimizer.new LineSearchFunction(variables).value(2.0);
        Assert.assertEquals(0.0, value, 0.0);
    }
}