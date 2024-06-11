import org.junit.Test;
import org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import static org.junit.Assert.assertEquals;

public class NonLinearConjugateGradientOptimizerTest {
    
    @Test
    public void testDoOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        
        double[] startPoint = new double[2];
        startPoint[0] = 1.0;
        startPoint[1] = 2.0;
        optimizer.setStartPoint(startPoint);
        
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        PointValuePair result = optimizer.optimize();
        
        double[] expected = new double[2];
        expected[0] = 0.0;
        expected[1] = 0.0;
        
        for (int i = 0; i < 2; i++) {
            assertEquals(expected[i], result.getPoint()[i], 1e-5);
        }
    }
}