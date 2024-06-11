import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {
    
    @Test
    public void testDoOptimize_MinimizeObjectiveFunction() {
        // Arrange
        double[] startPoint = {1.0, 2.0, 3.0};
        double[] expectedPointFletcherReeves = {0.0, 0.0, 0.0};
        double[] expectedPointPolakRibiere = {0.0, 0.0, 0.0};
        
        NonLinearConjugateGradientOptimizer.ConjugateGradientFormula fletcherReevesFormula = 
            NonLinearConjugateGradientOptimizer.ConjugateGradientFormula.FLETCHER_REEVES;
        
        NonLinearConjugateGradientOptimizer.ConjugateGradientFormula polakRibiereFormula = 
            NonLinearConjugateGradientOptimizer.ConjugateGradientFormula.POLAK_RIBIERE;
        
        NonLinearConjugateGradientOptimizer optimizerFletcherReeves = 
            new NonLinearConjugateGradientOptimizer(fletcherReevesFormula);
        
        NonLinearConjugateGradientOptimizer optimizerPolakRibiere = 
            new NonLinearConjugateGradientOptimizer(polakRibiereFormula);
        
        optimizerFletcherReeves.setMaxEvaluations(500);
        optimizerPolakRibiere.setMaxEvaluations(500);
        
        // Act
        PointValuePair resultFletcherReeves = optimizerFletcherReeves.optimize(startPoint);
        PointValuePair resultPolakRibiere = optimizerPolakRibiere.optimize(startPoint);
        
        // Assert
        assertArrayEquals(expectedPointFletcherReeves, resultFletcherReeves.getPoint(), 0.0001);
        assertArrayEquals(expectedPointPolakRibiere, resultPolakRibiere.getPoint(), 0.0001);
    }
    
    @Test
    public void testDoOptimize_MaximizeObjectiveFunction() {
        // Arrange
        double[] startPoint = {-1.0, -2.0, -3.0};
        double[] expectedPointFletcherReeves = {0.0, 0.0, 0.0};
        double[] expectedPointPolakRibiere = {0.0, 0.0, 0.0};
        
        NonLinearConjugateGradientOptimizer.ConjugateGradientFormula fletcherReevesFormula = 
            NonLinearConjugateGradientOptimizer.ConjugateGradientFormula.FLETCHER_REEVES;
        
        NonLinearConjugateGradientOptimizer.ConjugateGradientFormula polakRibiereFormula = 
            NonLinearConjugateGradientOptimizer.ConjugateGradientFormula.POLAK_RIBIERE;
        
        NonLinearConjugateGradientOptimizer optimizerFletcherReeves = 
            new NonLinearConjugateGradientOptimizer(fletcherReevesFormula);
        
        NonLinearConjugateGradientOptimizer optimizerPolakRibiere = 
            new NonLinearConjugateGradientOptimizer(polakRibiereFormula);
        
        optimizerFletcherReeves.setGoalType(GoalType.MAXIMIZE);
        optimizerPolakRibiere.setGoalType(GoalType.MAXIMIZE);
        
        optimizerFletcherReeves.setMaxEvaluations(500);
        optimizerPolakRibiere.setMaxEvaluations(500);
        
        // Act
        PointValuePair resultFletcherReeves = optimizerFletcherReeves.optimize(startPoint);
        PointValuePair resultPolakRibiere = optimizerPolakRibiere.optimize(startPoint);
        
        // Assert
        assertArrayEquals(expectedPointFletcherReeves, resultFletcherReeves.getPoint(), 0.0001);
        assertArrayEquals(expectedPointPolakRibiere, resultPolakRibiere.getPoint(), 0.0001);
    }
    
    @Test
    public void testDoOptimize_InvalidInputs() {
        // Arrange
        double[] startPoint = {1.0, 2.0};
        
        NonLinearConjugateGradientOptimizer.ConjugateGradientFormula fletcherReevesFormula = 
            NonLinearConjugateGradientOptimizer.ConjugateGradientFormula.FLETCHER_REEVES;
        
        NonLinearConjugateGradientOptimizer optimizer = 
            new NonLinearConjugateGradientOptimizer(fletcherReevesFormula);
        
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(startPoint));
    }
}