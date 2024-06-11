import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer;

public class NonLinearConjugateGradientOptimizerTest {
    
    @Test
    public void testConstructor() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer();
        Assert.assertNotNull(optimizer);
    }
    
    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer();
        optimizer.setInitialStep(0.5);
        Assert.assertEquals(0.5, optimizer.getInitialStep(), 0.0);
    }
    
    @Test
    public void testDoOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer();
        double[] startPoint = {1.0, 2.0, 3.0};
        optimizer.setStartPoint(startPoint);
        
        PointValuePair result = optimizer.doOptimize();
        
        Assert.assertNotNull(result);
        Assert.assertArrayEquals(startPoint, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 0.0);
    }
    
    @Test
    public void testIdentityPreconditioner() {
        NonLinearConjugateGradientOptimizer.IdentityPreconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
        double[] variables = {1.0, 2.0, 3.0};
        double[] r = {4.0, 5.0, 6.0};
        double[] precond = preconditioner.precondition(variables, r);
        
        Assert.assertNotNull(precond);
        Assert.assertArrayEquals(r, precond, 0.0);
    }
    
    @Test
    public void testLineSearchFunctionValue() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer();
        NonLinearConjugateGradientOptimizer.LineSearchFunction lineSearchFunction = optimizer.new LineSearchFunction(new double[]{1.0, 2.0, 3.0});
        
        double result = lineSearchFunction.value(1.5);
        
        Assert.assertEquals(0.0, result, 0.0);
    }
}