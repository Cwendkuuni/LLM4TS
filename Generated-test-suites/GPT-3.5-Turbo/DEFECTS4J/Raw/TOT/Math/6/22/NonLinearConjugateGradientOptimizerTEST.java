import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testOptimize_minimumValue() {
        ConjugateGradientFormula formula = ConjugateGradientFormula.POLAK_RIBIERE;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(formula);
        
        double[] startPoint = {1.0, 2.0, 3.0};
        double[] expectedPoint = {0.0, 0.0, 0.0};
        double expectedValue = 0.0;
        
        PointValuePair result = optimizer.optimize(100, objFunc, startPoint, GoalType.MINIMIZE);
        double[] actualPoint = result.getPoint();
        double actualValue = result.getValue();
        
        assertArrayEquals(expectedPoint, actualPoint, 1e-10);
        assertEquals(expectedValue, actualValue, 1e-10);
    }
    
    @Test
    public void testOptimize_maximumValue() {
        ConjugateGradientFormula formula = ConjugateGradientFormula.POLAK_RIBIERE;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(formula);
        
        double[] startPoint = {-1.0, -2.0, -3.0};
        double[] expectedPoint = {0.0, 0.0, 0.0};
        double expectedValue = 0.0;
        
        PointValuePair result = optimizer.optimize(100, objFunc, startPoint, GoalType.MAXIMIZE);
        double[] actualPoint = result.getPoint();
        double actualValue = result.getValue();
        
        assertArrayEquals(expectedPoint, actualPoint, 1e-10);
        assertEquals(expectedValue, actualValue, 1e-10);
    }
    
    @Test
    public void testOptimize_localMinimum() {
        ConjugateGradientFormula formula = ConjugateGradientFormula.POLAK_RIBIERE;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(formula);
        
        double[] startPoint = {-2.0, -1.0, 0.0, 1.0, 2.0};
        double[] expectedPoint = {-1.0, 0.0, 1.0, 2.0, 3.0};
        double expectedValue = 0.0;
        
        PointValuePair result = optimizer.optimize(100, objFunc, startPoint, GoalType.MINIMIZE);
        double[] actualPoint = result.getPoint();
        double actualValue = result.getValue();
        
        assertArrayEquals(expectedPoint, actualPoint, 1e-10);
        assertEquals(expectedValue, actualValue, 1e-10);
    }
    
    @Test
    public void testOptimize_localMaximum() {
        ConjugateGradientFormula formula = ConjugateGradientFormula.POLAK_RIBIERE;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(formula);
        
        double[] startPoint = {2.0, 1.0, 0.0, -1.0, -2.0};
        double[] expectedPoint = {3.0, 2.0, 1.0, 0.0, -1.0};
        double expectedValue = 0.0;
        
        PointValuePair result = optimizer.optimize(100, objFunc, startPoint, GoalType.MAXIMIZE);
        double[] actualPoint = result.getPoint();
        double actualValue = result.getValue();
        
        assertArrayEquals(expectedPoint, actualPoint, 1e-10);
        assertEquals(expectedValue, actualValue, 1e-10);
    }
    
    @Test
    public void testOptimize_invalidFormula() {
        ConjugateGradientFormula formula = ConjugateGradientFormula.CONJUGATE_GRADIENT;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(formula);
        
        double[] startPoint = {1.0, 2.0, 3.0};
        
        try {
            PointValuePair result = optimizer.optimize(100, objFunc, startPoint, GoalType.MINIMIZE);
            fail("Expected MathIllegalStateException");
        } catch (MathIllegalStateException ex) {
            assertEquals(LocalizedFormats.UNABLE_TO_BRACKET_OPTIMUM_IN_LINE_SEARCH, ex.getPattern());
        }
    }
    
    private final UnivariateFunction objFunc = new UnivariateFunction() {
        public double value(double x) {
            return FastMath.pow(x, 2);
        }
    };
}